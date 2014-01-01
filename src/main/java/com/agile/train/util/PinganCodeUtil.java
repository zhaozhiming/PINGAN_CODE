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
import java.util.Comparator;
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
    private static final String PREFIX_COMPRESS_FILE = "jar:file:";
    private static final String PREFIX_FILE_IN_COMPRESS = "!/";

    public static List<SourceFile> searchFileInRepositoryByKeyword(String repositoryPath,
                                                                   String[] searchKeywords) throws IOException {
        File repository = new File(repositoryPath);
        File[] files = repository.listFiles();

        if (files.length == 0) return Collections.emptyList();

        List<SourceFile> result = Lists.newArrayList();
        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(searchFileInRepositoryByKeyword(file.getAbsolutePath(), searchKeywords));
                continue;
            }

            result.addAll(searchFileInCompressFileByKeyword(file, searchKeywords));
        }

        sortSourceFilesByFireMatch(result);
        return result;
    }

    private static void sortSourceFilesByFireMatch(List<SourceFile> result) {
        Collections.sort(result, new Comparator<SourceFile>() {
            @Override
            public int compare(SourceFile o1, SourceFile o2) {
                return o2.getFireMatch() - o1.getFireMatch();
            }
        });
    }

    public static String readSourceCodeByFileNameInCompressFile(String compressFileName, String fileName) throws Exception {
        return retrieveCompilationUnitFromJavaFile(compressFileName, fileName).toString();
    }

    public static List<MethodDisplayer> retrieveMethodsByFileNameInCompressFile(String compressFileName, String fileName) throws Exception {
        CompilationUnit compilationUnit = retrieveCompilationUnitFromJavaFile(compressFileName, fileName);

        MethodVisitor methodVisitor = new MethodVisitor();
        methodVisitor.visit(compilationUnit, null);

        return methodVisitor.getMethods();
    }

    private static List<SourceFile> searchFileInCompressFileByKeyword(File compressFile,
                                                                      String[] searchKeywords) throws IOException {
        ZipFile zipFile = new ZipFile(compressFile);
        try {
            List<SourceFile> result = searchFileByKeyword(compressFile, searchKeywords, zipFile.entries());
            zipFile.close();
            return result;
        } finally {
            zipFile.close();
        }
    }

    private static List<SourceFile> searchFileByKeyword(File compressFile, String[] searchKeywords,
                                                        Enumeration<? extends ZipEntry> entries)
            throws IOException {
        List<SourceFile> result = Lists.newArrayList();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            String path = zipEntry.getName();

            if (path.endsWith(SUFFIX_JAVA) && constainsIgnoreCase(path, searchKeywords[0])) {
                int fireMatch = pathMatchKeywordCount(searchKeywords, path);

                String version = retrieveVersionInCompressFileName(compressFile.getName());
                result.add(new SourceFile(compressFile.getName(),
                        Strings.isNullOrEmpty(version) ? UNKNOWN_VERSION : version,
                        path, compressFile.getAbsolutePath(), fireMatch));
            }
        }
        return result;
    }

    private static int pathMatchKeywordCount(String[] searchKeywords, String path) {
        int fireMatch = 0;
        for (String searchKeyword : searchKeywords) {
            if (constainsIgnoreCase(path, searchKeyword)) {
                fireMatch++;
            }
        }
        return fireMatch;
    }

    private static boolean constainsIgnoreCase(String findString, String containsString) {
        return Pattern.compile(Pattern.quote(containsString),
                Pattern.CASE_INSENSITIVE).matcher(findString).find();
    }

    private static String retrieveVersionInCompressFileName(String compressFileName) {
        String regEx = REGEX_FIND_VERSION;
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(compressFileName);
        matcher.find();

        return matcher.group(VERSION_INDEX);
    }

    private static CompilationUnit retrieveCompilationUnitFromJavaFile(
            String compressFileName, String fileName) throws Exception {
        InputStream inputStream = null;

        try {
            URL url = new URL(PREFIX_COMPRESS_FILE + compressFileName + PREFIX_FILE_IN_COMPRESS + fileName);
            inputStream = url.openStream();
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
