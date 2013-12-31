package com.agile.train.util;

import com.agile.train.model.MethodDisplayer;
import com.agile.train.model.SourceFile;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

public class PinganCodeUtilTest {

    @Test
    public void should_search_source_file_in_repository_by_keyword_return_1_file_when_have_1_jar() throws Exception {
        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "BeanCopier");
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSourceFilePath(), is("net/sf/cglib/beans/BeanCopier.java"));
        assertThat(result.get(0).getVersion(), is("2.2"));
        assertThat(result.get(0).getJarName(), is("cglib-2.2-sources.jar"));
        assertThat(result.get(0).getJarFilePath(), containsString("test-repository/cglib-2.2-sources.jar"));
    }

    @Test
    public void should_search_source_file_in_repository_by_keyword_return_more_files_when_have_many_jars() throws Exception {
        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "Log");
        assertThat(result.size(), is(29));
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
    public void should_return_source_file_when_jar_in_repository_folder() throws Exception {
        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "ArrayToCollectionConverter");
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSourceFilePath(), is("org/springframework/core/convert/support/ArrayToCollectionConverter.java"));
        assertThat(result.get(0).getVersion(), is("3.2.0"));
        assertThat(result.get(0).getJarName(), is("spring-core-3.2.0.RELEASE-sources.jar"));
        assertThat(result.get(0).getJarFilePath(), containsString("test-repository/spring/spring-core-3.2.0.RELEASE-sources.jar"));
    }

    @Test
    public void should_return_source_file_when_jar_in_repository_multiply_folders() throws Exception {
        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "JspAwareRequestContext");
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSourceFilePath(), is("org/springframework/web/servlet/support/JspAwareRequestContext.java"));
        assertThat(result.get(0).getVersion(), is("3.2.0"));
        assertThat(result.get(0).getJarName(), is("spring-webmvc-3.2.0.RELEASE-sources.jar"));
        assertThat(result.get(0).getJarFilePath(), containsString("test-repository/spring/3.2.0/spring-webmvc-3.2.0.RELEASE-sources.jar"));
    }

    @Test
    public void should_return_source_file_when_meet_a_zip_file() throws Exception {
        List<SourceFile> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "DaoTransactionState");
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSourceFilePath(), is("src/com/ibatis/dao/engine/impl/DaoTransactionState.java"));
        assertThat(result.get(0).getVersion(), is("2.1.7"));
        assertThat(result.get(0).getJarName(), is("ibatis-2.1.7-src.zip"));
        assertThat(result.get(0).getJarFilePath(), containsString("test-repository/ibatis-2.1.7-src.zip"));
    }

    @Test
    public void should_read_source_code_by_file_name_in_jar_correct() throws Exception {
        String sourceCode = PinganCodeUtil.readSourceCodeByFileNameInCompressFile(
                "test-repository/cglib-2.2-sources.jar", "net/sf/cglib/beans/BeanCopier.java");
        assertNotNull(sourceCode);
        assertThat(sourceCode.contains("class BeanCopier"), is(true));
    }

    @Test
    public void should_read_source_code_by_file_name_in_zip_correct() throws Exception {
        String sourceCode = PinganCodeUtil.readSourceCodeByFileNameInCompressFile(
                "test-repository/ibatis-2.1.7-src.zip", "src/com/ibatis/db/sqlmap/MappedStatement.java");
        assertNotNull(sourceCode);
        assertThat(sourceCode.contains("public class MappedStatement"), is(true));
    }

    @Test
    public void should_return_method_when_given_source_code_file() throws Exception {
        List<MethodDisplayer> methods = PinganCodeUtil.retrieveMethodsByFileNameInCompressFile(
                "test-repository/cglib-2.2-sources.jar", "net/sf/cglib/beans/BeanCopier.java");
        assertThat(methods.size(), is(12));

        int assertCount = 0;
        for (MethodDisplayer method : methods) {
            assertCount += assertMethod(method, "public abstract void copy(Object from, Object to, Converter converter)",
                    "public abstract", "public", "copy(Object,Object,Converter): void");
            assertCount += assertMethod(method, "private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter)",
                    "private static", "private", "compatible(PropertyDescriptor,PropertyDescriptor): boolean");
            assertCount += assertMethod(method, "protected ClassLoader getDefaultClassLoader()",
                    "protected", "protected", "getDefaultClassLoader(): ClassLoader");
        }
        assertThat(assertCount, is(3));
    }

    private int assertMethod(MethodDisplayer method, String exceptFindText,
                             String exceptModifierText, String exceptModifier,
                             String exceptShowText) {
        if (method.getFindText().equals(exceptFindText)) {
            assertThat(method.getModifierText(), is(exceptModifierText));
            assertThat(method.getModifier(), is(exceptModifier));
            assertThat(method.getShowText(), is(exceptShowText));
            return 1;
        }
        return 0;
    }

    private int assertSourceFileWhenExist(SourceFile sourceFile, String exceptPath,
                                          String exceptJarName, String exceptVersion) {
        if (sourceFile.getSourceFilePath().equals(exceptPath)) {
            assertThat(sourceFile.getJarName(), is(exceptJarName));
            assertThat(sourceFile.getVersion(), is(exceptVersion));
            return 1;
        }
        return 0;
    }

}
