package doclet_living_doc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.Test;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import com.sun.tools.classfile.Exceptions_attribute;
import com.sun.tools.javac.code.Attribute.Array;

public class ExtractCommentsDoclet {

	public static class WriteException extends RuntimeException {

		private static final long serialVersionUID = -3238559592912915212L;

		WriteException(Exception e) {
			super(e);
		}
	}
	
	public static class ReadException extends RuntimeException {

		private static final long serialVersionUID = -8934518257471788419L;

		ReadException(Exception e) {
			super(e);
		}
	}
	
	
	private RootDoc root;
	private Writer currentFile;

	public ExtractCommentsDoclet(Writer writer, RootDoc root) {
		this.currentFile = writer;
		this.root = root;
	}

	public void generate() {
		printDocHeader();
		for (ClassDoc c : root.classes()) {
			printTestClass(c.name());
			printBlock(c.getRawCommentText());
			for (FieldDoc f : c.fields(false)) {
				printBlock(f.commentText());
			}
			for (MethodDoc m : c.methods(false)) {

				if (isTestMethod(m)) {
					printTestMethod(m.name());
					printBlock(m.getRawCommentText());
					if (m.commentText() != null && m.commentText().length() > 0) {
						for (ParamTag p : m.paramTags())
							printBlock(p.parameterComment());
						for (Tag t : m.tags("return")) {
							if (t.text() != null && t.text().length() > 0)
								print(t.text());
						}
					}
					printSource(m);
				}

			}
		}
	}

	private void printDocHeader() {
		printTitle("Test documentation", ":toc: left", ":source-highlighter: coderay");
	}

	public class WriteUntil implements Predicate<String> {
		long count = 0;
		boolean isMethodOpen = false;
		StringBuffer buffer = new StringBuffer();

		@Override
		public boolean test(String line) {
			isMethodOpen |= line.contains("{");
			count += line.codePoints().filter(ch -> ch == '{').count()
					- line.codePoints().filter(ch -> ch == '}').count();
			buffer.append(line + "\n");
			return isMethodOpen && count <= 0;
		}

		public void print() {
			ExtractCommentsDoclet.this.printSource(buffer.toString());
		}

	}

	protected void printSource(MethodDoc m) {
		File file = m.position().file();

		int line = m.position().line();

		WriteUntil bufferUntil = new WriteUntil();

		try (Stream<String> stream = Files.lines(file.toPath()).skip(line - 1)) {
			stream.anyMatch(bufferUntil);
		} catch (IOException e) {
			throw new ReadException(e);
		}
		bufferUntil.print();

	}

	protected void printSource(String source) {
		Optional<Integer> minSpace = Arrays.stream(source.split("\n"))
				.filter(line -> !line.trim().isEmpty())
				.map(line -> line.indexOf(line.trim()))
				.min(Comparator.naturalOrder());
		
		print("\n[source,java]", "----");
		Arrays.stream(source.split("\n"))
			.map(line -> line.substring(Math.min(minSpace.orElse(0), line.length())))			
			.forEach(line -> print(line));
		
		print("----\n");
	}

	private boolean isTestMethod(MethodDoc m) {
		return Arrays.stream(m.annotations()).map(desc -> desc.annotationType())
				.anyMatch(type -> Test.class.getName().equals(type.toString()));
	}

	private void printBlock(String comment) {
		if (!comment.isEmpty()) {
			print("\n----\n" + comment + "\n----\n");
		}
	}

	private void printTitle(String... values) {
		print("\n= " + String.join("\n", values));
	}

	private void printTestClass(String className) {
		print("\n== " + camelCaseToSnakeCase(className));
	}

	private void printTestMethod(String methodname) {
		print("\n=== " + camelCaseToSnakeCase(methodname));
	}

	private String camelCaseToSnakeCase(String text) {
		return text.replaceAll("(.)(\\p{Upper})", "$1_$2").replaceAll("_", " ").toLowerCase();
	}

	private void print(String... texts) {
		try {
			currentFile.append(String.join("\n", texts) + "\n");
		} catch (IOException e) {
			throw new WriteException(e);
		}
	}

	public static boolean start(RootDoc root) throws IOException {
		for (ClassDoc classDoc : root.classes()) {
			System.out.println(classDoc.name());
		}
		try (Writer file = new FileWriter("docs/doc.adoc")) {
			ExtractCommentsDoclet doclet = new ExtractCommentsDoclet(file, root);
			doclet.generate();
		}
		return true;
	}

}