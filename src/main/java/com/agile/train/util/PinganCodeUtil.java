package com.agile.train.util;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Collections;
import java.util.List;
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

    public static List<String> searchFileInRepositoryByKeyword(String repositoryPath, String searchKeyword) throws IOException {
        File repository = new File(repositoryPath);
        File[] files = repository.listFiles();

        if (files == null || files.length == 0) return Collections.emptyList();

        List<String> result = Lists.newArrayList();
        for (File file : files) {
            if (file.isDirectory()) continue;

            result.addAll(searchFileInJarByKeyword(file, searchKeyword));
        }

        return result;
    }

    private static List<String> searchFileInJarByKeyword(File jarFile, String searchKeyword) throws IOException {
        ZipInputStream jarFileInputStream = null;
        try {
            List<String> result = Lists.newArrayList();
            jarFileInputStream = new ZipInputStream(new FileInputStream(jarFile));
            ZipEntry entry;
            while ((entry = jarFileInputStream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".java") && entry.getName().contains(searchKeyword)) {
                    result.add(entry.getName());
                }
            }
            IOUtils.closeQuietly(jarFileInputStream);
            return result;
        } finally {
            IOUtils.closeQuietly(jarFileInputStream);
        }
    }

}
