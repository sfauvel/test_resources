package org.sfvl.prepamock;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CollectCallVisitor extends VoidVisitorAdapter<CallCollector> {

    public static final String UNDEFINED_TYPE = "Type undefined";
    final Set<String> imports = new HashSet<>();
    final Map<String, String> variables = new HashMap<>();
    private final String methodToParse;

    private String currentScope;
    private boolean isInnerClass = false;

    public CollectCallVisitor() {
        this(null);
    }

    public CollectCallVisitor(String methodToParse) {
        this.methodToParse = methodToParse;
    }

    @Override
    public void visit(final CompilationUnit n, final CallCollector collector) {
        currentScope = n.getPackageDeclaration().map(p -> p.getNameAsString()).orElse("");
        super.visit(n, collector);
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration n, final CallCollector collector) {
        String oldScope = currentScope;
        currentScope = oldScope + (isInnerClass?"$":".") + n.getNameAsString();
        isInnerClass = true;
        imports.add(currentScope);
        super.visit(n, collector);
        currentScope = oldScope;
    }

    @Override
    public void visit(final MethodDeclaration n, final CallCollector collector) {
        if (methodToParse != null && !n.getNameAsString().equals(methodToParse)) {
            return;
        }
        NodeList<Parameter> parameters = n.getParameters();

        parameters.forEach(p -> {
            variables.put(p.getName().asString(), p.getType().asString());
        });
        super.visit(n, collector);
    }

    @Override
    public void visit(final MethodCallExpr n, final CallCollector collector) {

        if (n.getScope().isPresent()) {
            String methodName = n.getName().asString();

            String callerName = n.getScope().get().toString();
            Optional<String> callerType = getIfExists(variables, callerName);
            Optional<String> fullTypeName = callerType.flatMap(type -> getFullTypeName(type, imports));
            Optional<String> returnType = fullTypeName.map(f -> getReturnType(f, methodName));

            String[] arguments = n.getArguments().stream().map(a -> a.toString()).collect(Collectors.toList()).toArray(new String[0]);

            MethodCall currentMethodCall = new MethodCall(
                    methodName,
                    returnType.orElse(null),
                    arguments,
                    callerName,
                    callerType.orElse(UNDEFINED_TYPE),
                    fullTypeName.orElse(UNDEFINED_TYPE)
            );
            collector.addCall(currentMethodCall);
        }

        super.visit(n, collector);
    }

    private Optional<String> getIfExists(Map<String, String> variables, String key) {
        if (variables.containsKey(key)) {
            return Optional.of(variables.get(key));
        } else {
            return Optional.empty();
        }
    }

    private String getReturnType(String fullCallerType, String methodName) {
        try {
            Class<?> clazz = Class.forName(fullCallerType);
            Optional<Method> method = Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getName().equals(methodName))
                    .findFirst();
            if (!method.isPresent()) {
                throw new RuntimeException("Method '" + methodName + "' does not exists on type '" + fullCallerType + " '.");
            }

            return method.get().getReturnType().getCanonicalName();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Type '" + fullCallerType + "' does not exists.");
        }
    }

    @Override
    public void visit(final ImportDeclaration n, final CallCollector collector) {
        imports.add(n.getNameAsString() + (n.isAsterisk() ? ".*" : ""));
        super.visit(n, collector);
    }


    @Override
    public void visit(final VariableDeclarator n, final CallCollector collector) {
        variables.put(n.getName().asString(), n.getType().asString());
        super.visit(n, collector);
    }

    private Optional<String> getFullTypeName(String name, Set<String> imports) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        return imports.stream().map(importName -> {
            String fullname = null;
            if (importName.endsWith("*")) {
                String searchName = importName.replace("*", name);
                try {
                    Class.forName(searchName, false, classLoader);
                    fullname = searchName;
                } catch (ClassNotFoundException e) {
                }
            } else {

                if (importName.endsWith("." + name) || importName.endsWith("$" + name)) {
                    fullname = importName;
                }
            }
            return fullname;
        }).filter(Objects::nonNull).findFirst();
    }

}
