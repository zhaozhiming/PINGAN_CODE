package com.agile.train.util;

import com.agile.train.model.SourceFile;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PinganCodeUtil {

    private static final String REGEX_FIND_VERSION = "(\\d(\\.\\d)+)";
    private static final String REGEX_FIND_METHODS = "(\\w+\\s+\\w+\\s*\\(((\\s*\\w+\\s*(\\[\\])*\\s*\\s+(\\[\\])*\\s*\\w+\\s*(\\[\\])*,?)+)?\\)\\s*(?=\\{))";
    private static final String IMPLEMENTATION_VERSION = "Implementation-Version";
    private static final String BUNDLE_VERSION = "Bundle-Version";
    private static final String SUFFIX_JAVA = ".java";
    private static final String UNKNOWN_VERSION = "";
    private static final int VERSION_INDEX = 1;

    public static String readSourceCodeByFileNameInJar(String jarFileName, String fileName) throws IOException {
        ZipInputStream jarFileInputStream = null;
        try {
            jarFileInputStream = new ZipInputStream(new FileInputStream(jarFileName));
            String result = readSourceFileInJar(fileName, jarFileInputStream);
            jarFileInputStream.closeEntry();
            IOUtils.closeQuietly(jarFileInputStream);
            return result;
        } finally {
            IOUtils.closeQuietly(jarFileInputStream);
        }
    }

    private static String readSourceFileInJar(String fileName,
                                              ZipInputStream jarFileInputStream) throws IOException {
        ZipEntry entry;
        while ((entry = jarFileInputStream.getNextEntry()) != null) {
            if (entry.getName().equals(fileName)) {
                return readSourceCode(jarFileInputStream);
            }
        }
        return "";
    }

    private static String readSourceCode(ZipInputStream jarFileInputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader sourceCode = new BufferedReader(new InputStreamReader(jarFileInputStream));

        String line;
        while ((line = sourceCode.readLine()) != null) {
            result.append(line).append("\n");
        }

        return result.toString();
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

    private static List<SourceFile> searchFileInJarByKeyword(File jarFile, String searchKeyword) throws IOException {
        ZipInputStream jarFileInputStream = null;
        try {
            jarFileInputStream = new ZipInputStream(new FileInputStream(jarFile));
            List<SourceFile> result = Lists.newArrayList();
            ZipEntry entry;
            while ((entry = jarFileInputStream.getNextEntry()) != null) {
                String path = entry.getName();
                if (path.endsWith(SUFFIX_JAVA) && path.contains(searchKeyword)) {
                    result.add(new SourceFile(jarFile.getName(), retrieveVersionInJar(jarFile), path));
                }
            }
            IOUtils.closeQuietly(jarFileInputStream);
            return result;
        } finally {
            IOUtils.closeQuietly(jarFileInputStream);
        }
    }

    private static String retrieveVersionInJar(File jarFile) throws IOException {
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
            Manifest manifest = jar.getManifest();

            String version = UNKNOWN_VERSION;
            Attributes attributes = manifest.getMainAttributes();
            if (attributes == null) return UNKNOWN_VERSION;

            for (Object attribute : attributes.keySet()) {
                Attributes.Name key = (Attributes.Name) attribute;
                String keyword = key.toString();
                if (keyword.equals(IMPLEMENTATION_VERSION) || keyword.equals(BUNDLE_VERSION)) {
                    version = (String) attributes.get(key);
                    break;
                }
            }
            jar.close();

            version = Strings.isNullOrEmpty(version) ? retrieveVersionInJarName(jarFile.getName()) : version;
            return Strings.isNullOrEmpty(version) ? UNKNOWN_VERSION : version;
        } finally {
            if (jar != null) jar.close();
        }
    }

    private static String retrieveVersionInJarName(String jarName) {
        String regEx = REGEX_FIND_VERSION;
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(jarName);
        matcher.find();

        return matcher.group(VERSION_INDEX);
    }

}
