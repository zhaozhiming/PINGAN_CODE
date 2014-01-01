package com.agile.train.model;

public class SourceFile {
    private String jarName;
    private String version;
    private String sourceFilePath;
    private String jarFilePath;
    private int fireMatch = 1;

    public SourceFile(String jarName, String version, String sourceFilePath, String jarFilePath, int fireMatch) {
        this.jarName = jarName;
        this.version = version;
        this.sourceFilePath = sourceFilePath;
        this.jarFilePath = jarFilePath;
        this.fireMatch = fireMatch;
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

    public int getFireMatch() {
        return fireMatch;
    }
}
