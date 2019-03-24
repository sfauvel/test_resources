package org.sfvl.prepamock;

import java.util.ArrayList;
import java.util.List;

public class CallCollector {
    final List<MethodCall> calls = new ArrayList<>();

    public void addCall(MethodCall call) {
        calls.add(call);
    }

    public MethodCall getCall(int number) {
        return calls.get(number);
    }
}
