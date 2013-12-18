package com.agile.train.util;

import com.agile.train.dto.SourceFile;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PinganCodeUtil {

    public static String readSourceCodeByFileNameInJar(String jarFileName, String fileName) throws IOException {
        ZipInputStream jarFileInputStream = null;
        try {
            jarFileInputStream = new ZipInputStream(new FileInputStream(jarFileName));
            StringBuilder result = readSourceFileInJar(fileName, jarFileInputStream);
            jarFileInputStream.closeEntry();
            IOUtils.closeQuietly(jarFileInputStream);
            return result.toString();
        } finally {
            IOUtils.closeQuietly(jarFileInputStream);
        }
    }

    private static StringBuilder readSourceFileInJar(String fileName, ZipInputStream jarFileInputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        ZipEntry entry;
        while ((entry = jarFileInputStream.getNextEntry()) != null) {
            if (!entry.getName().equals(fileName)) continue;

            BufferedReader sourceCode = new BufferedReader(new InputStreamReader(jarFileInputStream));

            String line;
            while ((line = sourceCode.readLine()) != null)
                result.append(line).append("\n");

            break;
        }
        return result;
    }

    public static List<SourceFile> searchFileInRepositoryByKeyword(String repositoryPath, String searchKeyword) throws IOException {
        File repository = new File(repositoryPath);
        File[] files = repository.listFiles();

        if (files == null || files.length == 0) return Collections.emptyList();

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
            if (attributes != null) {
                for (Object o : attributes.keySet()) {
                    Attributes.Name key = (Attributes.Name) o;
                    String keyword = key.toString();
                    if (keyword.equals("Implementation-Version") || keyword.equals("Bundle-Version")) {
                        version = (String) attributes.get(key);
                        break;
                    }
                }
            }
            jar.close();

            return version;
        } finally {
            if (jar != null) jar.close();
        }
    }
}
