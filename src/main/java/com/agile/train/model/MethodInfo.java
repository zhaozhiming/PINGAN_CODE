package com.agile.train.model;

import java.util.List;

public class MethodInfo {
    private String scope;
    private boolean isStatic;
    private String returnValue;
    private String method;
    private List<MethodParameter> parameters;

    public MethodInfo(String scope, boolean isStatic, String returnValue, String method, List<MethodParameter> parameters) {
        this.scope = scope;
        this.isStatic = isStatic;
        this.returnValue = returnValue;
        this.method = method;
        this.parameters = parameters;
    }
}
