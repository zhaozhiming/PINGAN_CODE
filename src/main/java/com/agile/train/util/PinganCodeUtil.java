package com.agile.train.util;

import com.agile.train.model.SourceFile;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinganCodeUtil {

    private static final String REGEX_FIND_VERSION = "(\\d(\\.\\d)+)";
    private static final String REGEX_FIND_METHODS = "(\\w+\\s+\\w+\\s*\\(((\\s*\\w+\\s*(\\[\\])*\\s*\\s+(\\[\\])*\\s*\\w+\\s*(\\[\\])*,?)+)?\\)\\s*(?=\\{))";
    private static final String IMPLEMENTATION_VERSION = "Implementation-Version";
    private static final String BUNDLE_VERSION = "Bundle-Version";
    private static final String SUFFIX_JAVA = ".java";
    private static final String UNKNOWN_VERSION = "";
    private static final int VERSION_INDEX = 1;
    private static final String PREFIX_JAR_FILE = "jar:file:";
    private static final String PREFIX_FILE_IN_JAR = "!/";

    public static String readSourceCodeByFileNameInJar(String jarFileName, String fileName) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(PREFIX_JAR_FILE + jarFileName + PREFIX_FILE_IN_JAR + fileName);
            inputStream = url.openStream();
            return readSourceCode(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static List<SourceFile> searchFileInRepositoryByKeyword(String repositoryPath,
                                                                   String searchKeyword) throws IOException {
        File repository = new File(repositoryPath);
        File[] files = repository.listFiles();

        if (files.length == 0) return Collections.emptyList();

        List<SourceFile> result = Lists.newArrayList();
        for (File file : files) {
            if (file.isDirectory()) continue;

            result.addAll(searchFileInJarByKeyword(file, searchKeyword));
        }

        return result;
    }

    public static List<String> findMethodsBySourceCode(String srcCode) {
        Pattern pattern = Pattern.compile(REGEX_FIND_METHODS);
        Matcher matcher = pattern.matcher(srcCode);

        List<String> methods = Lists.newArrayList();
        while (matcher.find()) {
            String methodName = matcher.group(0).trim();
            methods.add(methodName);
        }
        return methods;
    }

    private static String readSourceCode(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader sourceCode = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = sourceCode.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }

    private static List<SourceFile> searchFileInJarByKeyword(File jarFile, String searchKeyword) throws IOException {
        JarFile jar = new JarFile(jarFile);

        try {
            List<SourceFile> result = searchFileByKeyword(jarFile, searchKeyword, jar.entries());
            jar.close();
            return result;
        } finally {
            jar.close();
        }
    }

    private static List<SourceFile> searchFileByKeyword(File jarFile, String searchKeyword, Enumeration<JarEntry> entries) throws IOException {
        List<SourceFile> result = Lists.newArrayList();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String path = jarEntry.getName();

            if (path.endsWith(SUFFIX_JAVA) && path.contains(searchKeyword)) {
                result.add(new SourceFile(jarFile.getName(), retrieveVersionInJar(jarFile), path));
            }
        }
        return result;
    }

    private static String retrieveVersionInJar(File jarFile) throws IOException {
        JarFile jar = new JarFile(jarFile);
        try {
            Manifest manifest = jar.getManifest();

            Attributes attributes = manifest.getMainAttributes();
            if (attributes == null) return UNKNOWN_VERSION;

            String version = retrieveVersionInManifest(attributes);
            jar.close();

            version = Strings.isNullOrEmpty(version) ? retrieveVersionInJarName(jarFile.getName()) : version;
            return Strings.isNullOrEmpty(version) ? UNKNOWN_VERSION : version;
        } finally {
            jar.close();
        }
    }

    private static String retrieveVersionInManifest(Attributes attributes) {
        for (Object attribute : attributes.keySet()) {
            Attributes.Name key = (Attributes.Name) attribute;
            String keyword = key.toString();
            if (keyword.equals(IMPLEMENTATION_VERSION) || keyword.equals(BUNDLE_VERSION)) {
                return (String) attributes.get(key);
            }
        }
        return UNKNOWN_VERSION;
    }

    private static String retrieveVersionInJarName(String jarName) {
        String regEx = REGEX_FIND_VERSION;
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(jarName);
        matcher.find();

        return matcher.group(VERSION_INDEX);
    }

    public static List<String> retrieveMethodInSourceCode(String sourceCode) {
        String regEx = "((public|private|protected)\\s+)?(static\\s+)?([^new]+)\\s+(\\w+)\\((([a-zA-Z0-9|<|>|\\[|\\]|,]+)\\s+(\\w+)(,\\s)?)*\\)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(sourceCode);

        List<String> methods = Lists.newArrayList();
        while (matcher.find()) {
            String methodName = matcher.group(5).trim();
            methods.add(methodName);
        }

        return methods;
    }
}
