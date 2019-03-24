package org.sfvl.prepamock;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.System.exit;

public class RunGeneration {

    public static void main(String[] args) throws FileNotFoundException {

        if (args.length < 1) {
            System.out.println("Please, give a file name to use for generation.");
            exit(1);
        }

        String filename = args[0];
        try (FileInputStream in = new FileInputStream(filename)) {
            CompilationUnit cu = JavaParser.parse(in);

//        VoidVisitor<CallCollector> visitor = Mockito.mock(CollectCallVisitor.class,
//                Mockito.withSettings().spiedInstance(new CollectCallVisitor("insertPersonGeneric"))
//                        .defaultAnswer(new TraceAnswer(true)));

            CollectCallVisitor collectCallVisitor = new CollectCallVisitor();
            CallCollector collector = new CallCollector();
            cu.accept(collectCallVisitor, collector);

            MockGenerator generator = new MockGenerator();
            String result = generator.generate(collector);

            System.out.println(result);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
