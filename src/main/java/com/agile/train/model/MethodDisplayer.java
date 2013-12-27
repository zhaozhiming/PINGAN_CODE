package com.agile.train.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;

import java.lang.reflect.Modifier;
import java.util.List;

public class MethodDisplayer {
    private static final String MODIFIER_PUBLIC = "public";
    private static final String MODIFIER_PROTECTED = "protected";
    private static final String MODIFIER_PRIVATE = "private";
    private static final String MODIFIER_DEFAULT = "default";

    private String modifierText;
    private String modifier;
    private String showText;
    private String findText;

    public MethodDisplayer(MethodDeclaration methodDeclaration) {
        this.modifierText = convertModifierToString(methodDeclaration);
        this.modifier = getModifierBy(modifierText);

        List<Parameter> parameters = methodDeclaration.getParameters();

        this.showText = getShowTextBy(methodDeclaration, parameters);
        this.findText = getFindText(methodDeclaration, parameters);
    }

    private String convertModifierToString(MethodDeclaration methodDeclaration) {
        return Modifier.toString(methodDeclaration.getModifiers());
    }

    private String getFindText(MethodDeclaration methodDeclaration, List<Parameter> parameters) {
        StringBuilder findString = new StringBuilder();
        findString.append(convertModifierToString(methodDeclaration)).append(" ");
        findString.append(methodDeclaration.getType().toString()).append(" ");
        findString.append(methodDeclaration.getName()).append("(");

        if (parameters != null && !parameters.isEmpty()) {
            Joiner.on(", ").appendTo(findString, parameters);
        }

        findString.append(")");
        return findString.toString();
    }

    private String getShowTextBy(MethodDeclaration methodDeclaration, List<Parameter> parameters) {
        StringBuilder showString = new StringBuilder();
        showString.append(methodDeclaration.getName()).append("(");

        if (parameters != null && !parameters.isEmpty()) {
            List<String> parameterTypes = Lists.newArrayList();
            for (Parameter parameter : parameters) {
                parameterTypes.add(parameter.getType().toString());
            }
            Joiner.on(",").appendTo(showString, parameterTypes);
        }

        showString.append(")").append(": ").append(methodDeclaration.getType().toString());
        return showString.toString();
    }

    private String getModifierBy(String modifierText) {
        if (modifierText.contains(MODIFIER_PUBLIC)) return MODIFIER_PUBLIC;
        if (modifierText.contains(MODIFIER_PROTECTED)) return MODIFIER_PROTECTED;
        if (modifierText.contains(MODIFIER_PRIVATE)) return MODIFIER_PRIVATE;
        return MODIFIER_DEFAULT;
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

    @Override
    public String toString() {
        return "MethodDisplayer{" +
                "modifierText='" + modifierText + '\'' +
                ", modifier='" + modifier + '\'' +
                ", showText='" + showText + '\'' +
                ", findText='" + findText + '\'' +
                '}';
    }
}
