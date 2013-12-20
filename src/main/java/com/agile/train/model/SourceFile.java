package com.agile.train.model;

public class SourceFile {
    private String jarName;
    private String version;
    private String path;

    public SourceFile(String jarName, String version, String path) {
        this.jarName = jarName;
        this.version = version;
        this.path = path;
    }

    public String getJarName() {
        return jarName;
    }

    public String getVersion() {
        return version;
    }

    public String getPath() {
        return path;
    }
}
