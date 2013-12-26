package com.agile.train.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;

import java.lang.reflect.Modifier;
import java.util.List;

public class MethodDisplayer {
    private String modifierText;
    private String modifier;
    private String showText;
    private String findText;
    private String returnText;

    public MethodDisplayer(MethodDeclaration methodDeclaration) {
        this.modifierText = Modifier.toString(methodDeclaration.getModifiers());
        this.modifier = getModifierBy(modifierText);

        List<Parameter> parameters = methodDeclaration.getParameters();

        this.showText = getShowTextBy(methodDeclaration, parameters);
        this.findText = getFindText(methodDeclaration, parameters);
        this.returnText = methodDeclaration.getType().toString();
    }

    private String getFindText(MethodDeclaration methodDeclaration, List<Parameter> parameters) {
        if (parameters == null || parameters.isEmpty()) return "";

        StringBuilder findString = new StringBuilder();
        findString.append(methodDeclaration.getName()).append("(");
        Joiner.on(", ").appendTo(findString, parameters);
        findString.append(")");
        return findString.toString();
    }

    private String getShowTextBy(MethodDeclaration methodDeclaration, List<Parameter> parameters) {
        if (parameters == null || parameters.isEmpty()) return "";

        StringBuilder showString = new StringBuilder();
        showString.append(methodDeclaration.getName()).append("(");
        List<String> parameterTypes = Lists.newArrayList();
        for (Parameter parameter : parameters) {
            parameterTypes.add(parameter.getType().toString());
        }
        Joiner.on(",").appendTo(showString, parameterTypes);
        showString.append(")");
        return showString.toString();
    }

    private String getModifierBy(String modifierText) {
        if (modifierText.contains("public")) return "public";
        if (modifierText.contains("protected")) return "protected";
        if (modifierText.contains("private")) return "private";
        return "default";
    }

    public String getModifier() {
        return modifier;
    }

    public String getModifierText() {
        return modifierText;
    }

    public String getShowText() {
        return showText;
    }

    public String getFindText() {
        return findText;
    }

    public String getReturnText() {
        return returnText;
    }
}
