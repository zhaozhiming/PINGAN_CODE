package com.agile.train.util;

import com.agile.train.model.SourceFile;
import org.junit.Test;

import java.util.List;

import static com.agile.train.util.PinganCodeUtil.retrieveMethodInSourceCode;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PinganCodeUtilTest {

    @Test
    public void should_read_source_code_by_file_name_in_jar_correct() throws Exception {
        String sourceCode = PinganCodeUtil.readSourceCodeByFileNameInJar(
                "test-repository/cglib-2.2-sources.jar", "net/sf/cglib/beans/BeanCopier.java");
        assertNotNull(sourceCode);
        assertThat(sourceCode.contains("public class BeanCopier"), is(true));
    }

    @Test
    public void should_search_file_in_repository_by_keyword_return_1_file_when_when_have_1_jar() throws Exception {
        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "BeanCopier");
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getPath(), is("net/sf/cglib/beans/BeanCopier.java"));
        assertThat(result.get(0).getVersion(), is("2.2"));
        assertThat(result.get(0).getJarName(), is("cglib-2.2-sources.jar"));
    }

    @Test
    public void should_search_file_in_repository_by_keyword_return_more_files_when_when_have_many_jars() throws Exception {
        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "Log");
        assertThat(result.size(), is(16));
        int assertCount = 0;
        for (SourceFile sourceFile : result) {
            assertCount += assertSourceFileWhenExist(sourceFile, "org/slf4j/impl/Log4jLoggerAdapter.java",
                    "slf4j-log4j12-1.7.5-sources.jar", "1.7.5");

            assertCount += assertSourceFileWhenExist(sourceFile, "org/apache/commons/logging/LogSource.java",
                    "commons-logging-1.1.1-sources.jar", "1.1.1");
        }

        assertThat(assertCount, is(2));
    }

    @Test
    public void should_return_method_list_when_given_source_code() throws Exception {
        String sourceCode = PinganCodeUtil.readSourceCodeByFileNameInJar(
                "test-repository/cglib-2.2-sources.jar", "net/sf/cglib/beans/BeanCopier.java");
        List<String> result = PinganCodeUtil.findMethodsBySourceCode(sourceCode);
        assertThat(result.size(), is(11));
        assertThat(result.contains("void setSource(Class source)"), is(true));
    }

    @Test
    public void should_return_method_when_given_source_code_file() throws Exception {
        assertThat(retrieveMethodInSourceCode("public void method1(String s1)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("protected void method1(String s1)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("private void method1(String s1)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("private String method1(String s1)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("private String method1(String s1, String s2)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("String method1(String s1, String s2)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("String method1(String[] s1)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("String method1(String[] s1, String[] s2)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("String method1(List<String> s1)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("String method1(List<String> s1, Map<String, String> s2)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("private static String method1(List<String> s1, Map<String, String> s2)").get(0), is("method1"));
        assertThat(retrieveMethodInSourceCode("Object convert(Object, Class, Object)").get(0), is("convert"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void should_throw_exception_when_given_source_code_with_new() {
        assertThat(retrieveMethodInSourceCode("private new method1(String s1)").get(0), is("method1"));
    }

    private int assertSourceFileWhenExist(SourceFile sourceFile, String exceptPath,
                                          String exceptJarName, String exceptVersion) {
        if (sourceFile.getPath().equals(exceptPath)) {
            assertThat(sourceFile.getJarName(), is(exceptJarName));
            assertThat(sourceFile.getVersion(), is(exceptVersion));
            return 1;
        }
        return 0;
    }

}
