package com.agile.train.util;

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
        List<String> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "BeanCopier");
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is("net/sf/cglib/beans/BeanCopier.java"));
    }

    @Test
    public void should_search_file_in_repository_by_keyword_return_more_files_when_when_have_many_jars() throws Exception {
        List<String> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "Log");
        assertThat(result.size(), is(16));
        assertThat(result.contains("org/slf4j/impl/Log4jLoggerAdapter.java"), is(true));
        assertThat(result.contains("org/apache/commons/logging/LogSource.java"), is(true));
    }

}
