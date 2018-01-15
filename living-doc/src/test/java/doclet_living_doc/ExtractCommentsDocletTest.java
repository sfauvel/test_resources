package doclet_living_doc;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class ExtractCommentsDocletTest {

	@Test
	public void should_encapsulate_with_source_directive() throws IOException {
		Writer writer = new StringWriter();
		ExtractCommentsDoclet doclet = new ExtractCommentsDoclet(writer, null);
		doclet.printSource(joinLines(
				"boolean my_test() {",
				"  return true",
				"}"));
		
		assertEquals(joinLines(
				"\n[source,java]",
				"----",
				"boolean my_test() {",
				"  return true",
				"}",
				"----\n\n"), 
				writer.toString());
	}
	
	@Test
	public void should_move_source_to_the_left_when_only_whitespaces() throws IOException {
		Writer writer = new StringWriter();
		ExtractCommentsDoclet doclet = new ExtractCommentsDoclet(writer, null);
		doclet.printSource(joinLines(
				"   boolean my_test() {",
				"     return true",
				"   }"));
		
		assertEquals(joinLines(
				"\n[source,java]",
				"----",
				"boolean my_test() {",
				"  return true",
				"}",
				"----\n\n"), 
				writer.toString());
	}
	
	@Test
	public void should_move_source_to_the_left_even_empty_lines_with_no_spaces() throws IOException {
		Writer writer = new StringWriter();
		ExtractCommentsDoclet doclet = new ExtractCommentsDoclet(writer, null);
		doclet.printSource(joinLines(
				"   boolean my_test() {",
				"",
				"     return true",
				"",
				"   }"));
		
		assertEquals(joinLines(
				"\n[source,java]",
				"----",
				"boolean my_test() {",
				"",
				"  return true",
				"",
				"}",
				"----\n\n"), 
				writer.toString());
	}

	@Test
	public void should_move_source_to_the_left_even_lines_with_only_some_spaces() throws IOException {
		Writer writer = new StringWriter();
		ExtractCommentsDoclet doclet = new ExtractCommentsDoclet(writer, null);
		doclet.printSource(joinLines(
				"   boolean my_test() {",
				" ",
				"     return true",
				"  ",
				"   }"));
		
		assertEquals(joinLines(
				"\n[source,java]",
				"----",
				"boolean my_test() {",
				"",
				"  return true",
				"",
				"}",
				"----\n\n"), 
				writer.toString());
	}
	
	private String joinLines(String... lines) {
		return String.join("\n", lines);
	}

}
