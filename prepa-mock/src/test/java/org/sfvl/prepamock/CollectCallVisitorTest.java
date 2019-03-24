package org.sfvl.prepamock;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

public class CollectCallVisitorTest {

    private CollectCallVisitor visitor;

    @Before
    public void createVisitor() {
        visitor = Mockito.mock(CollectCallVisitor.class,
                Mockito.withSettings().spiedInstance(new CollectCallVisitor())
                        .defaultAnswer(new TraceAnswer(true)));
    }

    @Test
    public void should_find_method_name_called() throws Exception {

        CallCollector collector = collectFromFile("Sample1.java");

        assertEquals("close", collector.getCall(0).getMethodName());
        assertEquals("connection", collector.getCall(0).getCallerName());
        assertEquals("Connection", collector.getCall(0).getCallerType());
        assertEquals("java.sql.Connection", collector.getCall(0).getCallerFullType());
        assertEquals(MethodCall.NO_RETURN, collector.getCall(0).getReturnType());
    }

    private CallCollector collectFromFile(String filename) throws FileNotFoundException {
        FileInputStream in = new FileInputStream("src/test/java/org/sfvl/resources/" + filename);
        CompilationUnit cu = JavaParser.parse(in);

        CallCollector collector = new CallCollector();
        cu.accept(visitor, collector);
        return collector;
    }

    @Test
    public void should_find_return_type_of_a_method() throws Exception {

        CallCollector collector = collectFromFile("Sample3.java");

        assertEquals("createStatement", collector.getCall(0).getMethodName());
        assertEquals("connection", collector.getCall(0).getCallerName());
        assertEquals("Connection", collector.getCall(0).getCallerType());
        assertEquals("java.sql.Connection", collector.getCall(0).getCallerFullType());
        assertEquals("java.sql.Statement", collector.getCall(0).getReturnType());

    }


    @Test
    public void should_parse_when_exception_instruction() throws Exception {

        CallCollector collector = collectFromFile("Sample4.java");

        assertEquals("createStatement", collector.getCall(0).getMethodName());
        assertEquals("connection", collector.getCall(0).getCallerName());
        assertEquals("Connection", collector.getCall(0).getCallerType());
        assertEquals("java.sql.Connection", collector.getCall(0).getCallerFullType());
        assertEquals("java.sql.Statement", collector.getCall(0).getReturnType());
    }


    @Test
    public void should_find_when_return_type_is_not_used() throws Exception {

        CallCollector collector = collectFromFile("Sample5.java");

        assertEquals("createStatement", collector.getCall(0).getMethodName());
        assertEquals("connection", collector.getCall(0).getCallerName());
        assertEquals("Connection", collector.getCall(0).getCallerType());
        assertEquals("java.sql.Connection", collector.getCall(0).getCallerFullType());
        assertEquals("java.sql.Statement", collector.getCall(0).getReturnType());
    }

    @Test
    public void should_method_when_call_a_method_from_the_class() throws Exception {

        CallCollector collector = collectFromFile("Sample6.java");

        Assert.assertTrue(collector.calls.isEmpty());
    }

    @Test
    public void should_find_type_from_import_with_wild_card() throws Exception {

        CallCollector collector = collectFromFile("Sample7.java");

        assertEquals("close", collector.getCall(0).getMethodName());
        assertEquals("connection", collector.getCall(0).getCallerName());
        assertEquals("Connection", collector.getCall(0).getCallerType());
        assertEquals("java.sql.Connection", collector.getCall(0).getCallerFullType());
        assertEquals(MethodCall.NO_RETURN, collector.getCall(0).getReturnType());
    }

    @Test
    public void should_find_caller_type_that_is_an_argument() throws Exception {

        CallCollector collector = collectFromFile("Sample8.java");

        MethodCall callInsert = getCall(collector, "java.sql.Connection.close");
        assertEquals("close", callInsert.getMethodName());
        assertEquals("connection", callInsert.getCallerName());
        assertEquals("Connection", callInsert.getCallerType());
        assertEquals("java.sql.Connection", callInsert.getCallerFullType());
        assertEquals(MethodCall.NO_RETURN, callInsert.getReturnType());
    }

    @Test
    public void should_find_caller_inner_class_type_that_is_an_argument() throws Exception {

        CallCollector collector = collectFromFile("Sample11.java");

        MethodCall call = collector.calls.get(0);
        assertEquals("close", call.getMethodName());
        assertEquals("builder", call.getCallerName());
        assertEquals("RequestBuilder", call.getCallerType());
        assertEquals("org.sfvl.resources.Sample11$RequestBuilder", call.getCallerFullType());
        assertEquals(MethodCall.NO_RETURN, call.getReturnType());
    }

    @Test
    public void should_parse_only_method_specify() throws Exception {

        FileInputStream in = new FileInputStream("src/test/java/org/sfvl/resources/Sample9.java");
        CompilationUnit cu = JavaParser.parse(in);

        visitor = Mockito.mock(CollectCallVisitor.class,
                Mockito.withSettings().spiedInstance(new CollectCallVisitor("insertToRecord"))
                        .defaultAnswer(new TraceAnswer(true)));

        CallCollector collector = new CallCollector();
        cu.accept(visitor, collector);

        assertEquals(1, collector.calls.size());

        MethodCall callInsert = collector.calls.get(0);
        assertEquals("close", callInsert.getMethodName());
        assertEquals("connection", callInsert.getCallerName());
        assertEquals("Connection", callInsert.getCallerType());
        assertEquals("java.sql.Connection", callInsert.getCallerFullType());
        assertEquals(MethodCall.NO_RETURN, callInsert.getReturnType());
    }

    @Test
    public void should_parse_argument() throws Exception {

        CallCollector collector = collectFromFile("Sample10.java");


        MethodCall call = collector.calls.get(0);
        assertEquals(1, call.getArguments().length);
        assertEquals("sql", call.getArguments()[0]);
    }

    private MethodCall getCall(CallCollector collector, String methodName) {
        return collector.calls.stream()
                .filter(call -> (call.getCallerFullType() + "." + call.getMethodName()).equals(methodName))
                .findFirst().get();
    }

}
