package org.sfvl.prepamock;

public class MethodCall {

    public static final String NO_RETURN = "void";
    private String methodName;
    private String callerName;
    private String callerType;
    private String callerFullType;
    private String[] arguments;
    private String returnType;

    public MethodCall(String methodName, String callerName, String callerType, String callerFullType) {
        this(methodName, new String[]{}, callerName, callerType, callerFullType);
    }

    public MethodCall(String methodName, String[] arguments, String callerName, String callerType, String callerFullType) {
        this(methodName, NO_RETURN, arguments, callerName, callerType, callerFullType);
    }

    public MethodCall(String methodName, String returnType, String[] arguments, String callerName, String callerType, String callerFullType) {
        this.methodName = methodName;
        this.arguments = arguments;
        this.callerName = callerName;
        this.callerType = callerType;
        this.callerFullType = callerFullType;
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallerType() {
        return callerType;
    }

    public void setCallerType(String callerType) {
        this.callerType = callerType;
    }

    public String getCallerFullType() {
        return callerFullType;
    }

    public void setCallerFullType(String callerFullType) {
        this.callerFullType = callerFullType;
    }

    public String[] getArguments() { return arguments; }

    public void setArguments(String[] arguments) { this.arguments = arguments; }


    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }


    boolean hasReturnType() {
        return !NO_RETURN.equals(getReturnType());
    }
}
