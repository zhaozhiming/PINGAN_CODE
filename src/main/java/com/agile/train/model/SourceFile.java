package com.agile.train.model;

public class SourceFile {
    private String jarName;
    private String version;
    private String path;
    private String jarFilePath;

    public SourceFile(String jarName, String version, String path, String jarFilePath) {
        this.jarName = jarName;
        this.version = version;
        this.path = path;
        this.jarFilePath = jarFilePath;
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

    public String getJarFilePath() {
        return jarFilePath;
    }
}
