package com.agile.train.util;

import com.agile.train.dto.SourceFile;
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

    private static String readSourceFileInJar(String fileName, ZipInputStream jarFileInputStream) throws IOException {
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

    public static List<SourceFile> searchFileInRepositoryByKeyword(String repositoryPath, String searchKeyword) throws IOException {
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

    private static List<SourceFile> searchFileInJarByKeyword(File jarFile, String searchKeyword) throws IOException {
        ZipInputStream jarFileInputStream = null;
        try {
            jarFileInputStream = new ZipInputStream(new FileInputStream(jarFile));
            List<SourceFile> result = Lists.newArrayList();
            ZipEntry entry;
            while ((entry = jarFileInputStream.getNextEntry()) != null) {
                String path = entry.getName();
                if (path.endsWith(".java") && path.contains(searchKeyword)) {
                    result.add(new SourceFile(jarFile.getName(), retrieveVersionInJar(jarFile), path));
                }
            }
            IOUtils.closeQuietly(jarFileInputStream);
            return result;
        } finally {
            IOUtils.closeQuietly(jarFileInputStream);
        }
    }

    public static String retrieveVersionInJar(File jarFile) throws IOException {
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
            Manifest manifest = jar.getManifest();

            String version = "";
            Attributes attributes = manifest.getMainAttributes();
            if (attributes == null) return "";

            for (Object attribute : attributes.keySet()) {
                Attributes.Name key = (Attributes.Name) attribute;
                String keyword = key.toString();
                if (keyword.equals("Implementation-Version") || keyword.equals("Bundle-Version")) {
                    version = (String) attributes.get(key);
                    break;
                }
            }
            jar.close();

            return Strings.isNullOrEmpty(version) ? retrieveVersionInJarName(jarFile.getName()) : version;
        } finally {
            if (jar != null) jar.close();
        }
    }

    private static String retrieveVersionInJarName(String jarName) {
        String regEx = "(\\d(\\.\\d)+)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(jarName);
        matcher.find();

        return matcher.group(1);
    }

    public static List<String> findMethodsBySourceCode(String srcCode) {
        Pattern p = Pattern
                .compile("(\\w+\\s+\\w+\\s*\\(((\\s*\\w+\\s*(\\[\\])*\\s*\\s+(\\[\\])*\\s*\\w+\\s*(\\[\\])*,?)+)?\\)\\s*(?=\\{))");
        Matcher m = p.matcher(srcCode);

        List<String> methods = Lists.newArrayList();
        while (m.find()) {
            String methodName = m.group(0).trim();
            methods.add(methodName);
        }
        return methods;
    }

}
