package com.agile.train.util;

import com.agile.train.model.MethodDisplayer;
import com.agile.train.model.SourceFile;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PinganCodeUtil {

    private static final String REGEX_FIND_VERSION = "(\\d(\\.\\d)+)";
    private static final String SUFFIX_JAVA = ".java";
    private static final String UNKNOWN_VERSION = "";
    private static final int VERSION_INDEX = 1;
    private static final String PREFIX_JAR_FILE = "jar:file:";
    private static final String PREFIX_FILE_IN_JAR = "!/";

    public static List<SourceFile> searchFileInRepositoryByKeyword(String repositoryPath,
                                                                   String searchKeyword) throws IOException {
        File repository = new File(repositoryPath);
        File[] files = repository.listFiles();

        if (files.length == 0) return Collections.emptyList();

        List<SourceFile> result = Lists.newArrayList();
        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(searchFileInRepositoryByKeyword(file.getAbsolutePath(), searchKeyword));
                continue;
            }

            result.addAll(searchFileInJarByKeyword(file, searchKeyword));
        }

        return result;
    }

    public static String readSourceCodeByFileNameInJar(String jarFileName, String fileName) throws Exception {
        return retrieveCompilationUnitInFromJavaFile(jarFileName, fileName).toString();
    }

    public static List<MethodDisplayer> retrieveMethodsByFileNameInJar(String jarFileName, String fileName) throws Exception {
        CompilationUnit compilationUnit = retrieveCompilationUnitInFromJavaFile(jarFileName, fileName);

        MethodVisitor methodVisitor = new MethodVisitor();
        methodVisitor.visit(compilationUnit, null);

        return methodVisitor.getMethods();
    }

    private static List<SourceFile> searchFileInJarByKeyword(File jarFile, String searchKeyword) throws IOException {
        ZipFile zipFile = new ZipFile(jarFile);
        try {
            List<SourceFile> result = searchFileByKeyword(jarFile, searchKeyword, zipFile.entries());
            zipFile.close();
            return result;
        } finally {
            zipFile.close();
        }
    }

    private static List<SourceFile> searchFileByKeyword(File jarFile, String searchKeyword, Enumeration<? extends ZipEntry> entries) throws IOException {
        List<SourceFile> result = Lists.newArrayList();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            String path = zipEntry.getName();

            if (path.endsWith(SUFFIX_JAVA) && path.contains(searchKeyword)) {
                String version = retrieveVersionInJarName(jarFile.getName());
                result.add(new SourceFile(jarFile.getName(),
                        Strings.isNullOrEmpty(version) ? UNKNOWN_VERSION : version,
                        path, jarFile.getAbsolutePath()));
            }
        }
        return result;
    }

    private static String retrieveVersionInJarName(String jarName) {
        String regEx = REGEX_FIND_VERSION;
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(jarName);
        matcher.find();

        return matcher.group(VERSION_INDEX);
    }

    private static CompilationUnit retrieveCompilationUnitInFromJavaFile(
            String jarFileName, String fileName) throws Exception {
        InputStream inputStream = null;

        try {
            URL url = new URL(PREFIX_JAR_FILE + jarFileName + PREFIX_FILE_IN_JAR + fileName);
            inputStream = url.openStream();
            // parse the file
            return JavaParser.parse(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter {

        private List<MethodDisplayer> methods = Lists.newArrayList();

        @Override
        public void visit(MethodDeclaration method, Object arg) {
            methods.add(new MethodDisplayer(method));
        }

        public List<MethodDisplayer> getMethods() {
            return methods;
        }
    }

}
