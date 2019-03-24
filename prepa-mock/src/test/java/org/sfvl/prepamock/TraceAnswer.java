package org.sfvl.prepamock;

import com.github.javaparser.Position;
import com.github.javaparser.ast.nodeTypes.NodeWithRange;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Optional;

public class TraceAnswer implements Answer<Object> {

    private String indent = "";
    private final boolean verbose;

    public TraceAnswer(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        final String lastIndent = indent;
        indent += "\t";
        Object arg = invocation.getArguments()[0];

        String name = "";
        if (arg instanceof NodeWithSimpleName) {
            name = ((NodeWithSimpleName) arg).getName().asString();
        } else {
            name = arg.toString().replace("\n", " ");
        }
        int position = 0;
        if (arg instanceof NodeWithRange) {
            Optional<Position> begin = ((NodeWithRange) arg).getBegin();
            position = begin.map(p -> p.line).orElse(0);
        }

        if (verbose) {
            System.out.println(lastIndent + arg.getClass().getSimpleName() + "(" + position + ", " + name + ")");
        }

        Object result = invocation.callRealMethod();
        indent = lastIndent;

        return result;
    }

}
