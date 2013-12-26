package com.agile.train.util;

import com.agile.train.model.MethodDisplayer;
import com.agile.train.model.SourceFile;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PinganCodeUtilTest {

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
    public void should_read_source_code_by_file_name_in_jar_correct() throws Exception {
        String sourceCode = PinganCodeUtil.readSourceCodeByFileNameInJar(
                "test-repository/cglib-2.2-sources.jar", "net/sf/cglib/beans/BeanCopier.java");
        assertNotNull(sourceCode);
        assertThat(sourceCode.contains("class BeanCopier"), is(true));
    }

    @Test
    public void should_return_method_when_given_source_code_file() throws Exception {
        List<MethodDisplayer> methods = PinganCodeUtil.retrieveMethodsByFileNameInJar(
                "test-repository/cglib-2.2-sources.jar", "net/sf/cglib/beans/BeanCopier.java");
        assertThat(methods.size(), is(12));

        int assertCount = 0;
        for (MethodDisplayer method : methods) {
            assertCount += assertMethod(method, "copy(Object from, Object to, Converter converter)",
                    "public abstract", "public", "copy(Object,Object,Converter)", "void");
            assertCount += assertMethod(method, "compatible(PropertyDescriptor getter, PropertyDescriptor setter)",
                    "private static", "private", "compatible(PropertyDescriptor,PropertyDescriptor)", "boolean");
        }
        assertThat(assertCount, is(2));
    }

    private int assertMethod(MethodDisplayer method, String exceptFindText,
                             String exceptModifierText, String exceptModifier,
                             String exceptShowText, String exceptReturnText) {
        if (method.getFindText().equals(exceptFindText)) {
            assertThat(method.getModifierText(), is(exceptModifierText));
            assertThat(method.getModifier(), is(exceptModifier));
            assertThat(method.getShowText(), is(exceptShowText));
            assertThat(method.getReturnText(), is(exceptReturnText));
            return 1;
        }
        return 0;
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
