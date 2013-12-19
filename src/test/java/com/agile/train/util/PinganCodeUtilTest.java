package com.agile.train.util;

import com.agile.train.dto.SourceFile;
import org.junit.Test;

import java.util.List;

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
        for (SourceFile sourceFile : result) {
            assertSourceFileWhenExist(sourceFile, "org/slf4j/impl/Log4jLoggerAdapter.java",
                    "slf4j-log4j12-1.7.5-sources.jar", "1.7.5");

            assertSourceFileWhenExist(sourceFile, "org/slf4j/impl/Log4jLoggerAdapter.java",
                    "slf4j-log4j12-1.7.5-sources.jar", "1.7.5");

            assertSourceFileWhenExist(sourceFile, "org/apache/commons/logging/LogSource.java",
                    "commons-logging-1.1.1-sources.jar", "1.1.1");
        }
    }

    @Test
    public void should_return_method_list_when_given_source_code() throws Exception {

        String sourceCode = PinganCodeUtil.readSourceCodeByFileNameInJar(
                "test-repository/cglib-2.2-sources.jar", "net/sf/cglib/beans/BeanCopier.java");
        List<String> result = PinganCodeUtil.findMethodsBySourceCode(sourceCode);
        assertThat(result.size(), is(11));
        assertThat(result.contains("void setSource(Class source)"), is(true));
    }

    private void assertSourceFileWhenExist(SourceFile sourceFile, String exceptPath,
                                           String exceptJarName, String exceptVersion) {
        if (sourceFile.getPath().equals(exceptPath)) {
            assertThat(sourceFile.getJarName(), is(exceptJarName));
            assertThat(sourceFile.getVersion(), is(exceptVersion));
        }
    }

}
