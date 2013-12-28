package com.agile.train.model;

public class SourceFile {
    private String jarName;
    private String version;
    private String sourceFilePath;
    private String jarFilePath;

    public SourceFile(String jarName, String version, String sourceFilePath, String jarFilePath) {
        this.jarName = jarName;
        this.version = version;
        this.sourceFilePath = sourceFilePath;
        this.jarFilePath = jarFilePath;
    }

    public String getJarName() {
        return jarName;
    }

    public String getVersion() {
        return version;
    }

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public String getJarFilePath() {
        return jarFilePath;
    }
}
