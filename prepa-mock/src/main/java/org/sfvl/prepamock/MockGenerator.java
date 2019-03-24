package org.sfvl.prepamock;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MockGenerator {


    public String generate(CallCollector collector) {

        String generateDeclaration = collector.calls.stream()
                .map(call -> isCommentType(call) + "private " + formatFullName(call) + " " + call.getCallerName() + ";")
                .distinct()
                .collect(Collectors.joining("\n"));

        String generateMockCreation = collector.calls.stream()
                .map(call -> isCommentType(call) + call.getCallerName() + " = Mockito.mock(" + formatFullName(call) + ".class, mockSettings);")
                .distinct()
                .collect(Collectors.joining("\n"));

        String generateWhen = collector.calls.stream()
                .map(call -> isCommentType(call) + generateWhenInstruction(call))
                .collect(Collectors.joining("\n"));

        return Arrays.asList(generateDeclaration,
                "@Before",
                "public void init() throws SQLException {",
                "    MockSettings mockSettings = Mockito.withSettings().invocationListeners(listenerRecorded);",
                generateMockCreation,
                generateWhen,
                "}")
                .stream().collect(Collectors.joining("\n"));
    }

    private String formatFullName(MethodCall call) {
        return call.getCallerFullType().replaceAll("\\$", ".");
    }

    private String isCommentType(MethodCall call) {
        return call.getCallerFullType().equals(CollectCallVisitor.UNDEFINED_TYPE) ? "//":"";
    }

    private String generateWhenInstruction(MethodCall call) {
        String arguments = Arrays.stream(call.getArguments()).map(a -> "Mockito.any()").collect(Collectors.joining(", "));
        if (call.hasReturnType()) {
            return "Mockito.when(" + call.getCallerName() + "." + call.getMethodName() + "(" + arguments + ")).thenReturn(" + formatReturnType(call) + ");";
        } else {
            return "Mockito.doNothing().when(" + call.getCallerName() + ")." + call.getMethodName() + "(" + arguments + ");";
        }
    }

    private static int intValue;
    private static boolean booleanValue;

    private String formatReturnType(MethodCall call) {
        String returnType = call.getReturnType();
        if (returnType == null) {
            return null;
        }
        switch (returnType) {
            case "boolean":
                return Boolean.toString(booleanValue);
//            case "byte":
//                return byte.class;
//            case "short":
//                return short.class;

            case "int":
                return String.valueOf(intValue);
//            case "long":
//                return long.class;
//            case "float":
//                return float.class;
//            case "double":
//                return double.class;
//            case "char":
//                return char.class;
//            case "void":
//                return void.class;
        }

        return "Mockito.mock(" + returnType + ".class, mockSettings)";
    }

}
