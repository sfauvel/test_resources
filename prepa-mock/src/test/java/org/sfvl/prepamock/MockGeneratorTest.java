package org.sfvl.prepamock;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MockGeneratorTest {

    private CallCollector collector = new CallCollector();
    private MockGenerator generator = new MockGenerator();

    @Test
    public void should_generate_WHEN_instruction_when_no_arguments() {
        collector.addCall(new MethodCall("call", "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("Mockito.doNothing().when(obj).call();"));
    }

    @Test
    public void should_comment_WHEN_instruction_when_undefined_type() {
        collector.addCall(new MethodCall("call", "obj", CollectCallVisitor.UNDEFINED_TYPE, CollectCallVisitor.UNDEFINED_TYPE));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("//Mockito.doNothing().when(obj).call();"));
    }

    @Test
    public void should_generate_WHEN_instruction_when_has_one_argument() {
        collector.addCall(new MethodCall("call", new String[]{"arg"}, "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("Mockito.doNothing().when(obj).call(Mockito.any());"));
    }

    @Test
    public void should_generate_WHEN_instruction_when_has_two_arguments() {
        collector.addCall(new MethodCall("call", new String[]{"arg1", "arg2"}, "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("Mockito.doNothing().when(obj).call(Mockito.any(), Mockito.any());"));
    }

    @Test
    public void should_generate_WHEN_instruction_when_return_something() {
        collector.addCall(new MethodCall("call", "org.lang.Integer", new String[]{}, "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("Mockito.when(obj.call()).thenReturn(Mockito.mock(org.lang.Integer.class, mockSettings));"));
//        Assert.assertTrue(result, result.contains("Mockito.when(obj.call()).thenReturn(Mockito.mock())"));
    }

    @Test
    public void should_generate_WHEN_instruction_when_has_two_arguments_and_return_something() {
        collector.addCall(new MethodCall("call", "org.lang.Integer", new String[]{"arg1", "arg2"}, "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("Mockito.when(obj.call(Mockito.any(), Mockito.any())).thenReturn(Mockito.mock(org.lang.Integer.class, mockSettings));"));
    }

    @Test
    public void should_generate_WHEN_instruction_when_return_a_primitive_int() {
        collector.addCall(new MethodCall("call", "int", new String[]{"arg1"}, "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("Mockito.when(obj.call(Mockito.any())).thenReturn(0);"));
    }

    /**
     * Why return type could be null ?
     */
    @Test
    public void should_generate_WHEN_instruction_when_return_type_undefined() {
        collector.addCall(new MethodCall("call", null, new String[]{"arg1"}, "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("Mockito.when(obj.call(Mockito.any())).thenReturn(null);"));
    }

    @Test
    public void should_generate_mock_creation() {
        collector.addCall(new MethodCall("call", "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("obj = Mockito.mock(org.lang.String.class, mockSettings);"));
    }

    @Test
    public void should_generate_mock_creation_with_inner_class() {
        collector.addCall(new MethodCall("call", "obj", "Request", "org.jdbc.Connection$Request"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("obj = Mockito.mock(org.jdbc.Connection.Request.class, mockSettings);"));
    }

    @Test
    public void should_generate_mock_creation_only_once() {
        collector.addCall(new MethodCall("call", "obj", "String", "org.lang.String"));
        collector.addCall(new MethodCall("call", "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        long countDeclaration = Arrays.stream(result.split("\n"))
                .filter(line -> line.contains("obj = Mockito.mock"))
                .count();

        Assert.assertEquals(result, 1, countDeclaration);
    }

    @Test
    public void should_comment_mock_creation_with_undefined_type() {
        collector.addCall(new MethodCall("call", "obj", CollectCallVisitor.UNDEFINED_TYPE, CollectCallVisitor.UNDEFINED_TYPE));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("//obj = Mockito.mock(" + CollectCallVisitor.UNDEFINED_TYPE + ".class, mockSettings);"));
    }

    @Test
    public void should_generate_declaration() {
        collector.addCall(new MethodCall("call", "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("private org.lang.String obj;"));
    }

    @Test
    public void should_generate_declaration_with_inner_class() {
        collector.addCall(new MethodCall("call", "obj", "Request", "org.jdbc.Connection$Request"));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("private org.jdbc.Connection.Request obj;"));
    }

    @Test
    public void should_generate_declaration_only_once() {
        collector.addCall(new MethodCall("call", "obj", "String", "org.lang.String"));
        collector.addCall(new MethodCall("call", "obj", "String", "org.lang.String"));

        String result = generator.generate(collector);
        long countDeclaration = Arrays.stream(result.split("\n"))
                .filter(line -> line.contains("private org.lang.String obj;"))
                .count();

        Assert.assertEquals(result, 1, countDeclaration);
    }

    @Test
    public void should_comment_declaration_when_undefined_type() {
        collector.addCall(new MethodCall("call", "obj", CollectCallVisitor.UNDEFINED_TYPE, CollectCallVisitor.UNDEFINED_TYPE));

        String result = generator.generate(collector);
        Assert.assertTrue(result, result.contains("//private " + CollectCallVisitor.UNDEFINED_TYPE + " obj;"));
    }

}
