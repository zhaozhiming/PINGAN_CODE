package com.agile.train.util;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PinganCodeUtilTest {

    @Test
    public void testLoadZipFile() throws Exception {
        String sourceCode = PinganCodeUtil.readSourceCodeByFileNameInJar(
                "test-repository/cglib-2.2-sources.jar", "net/sf/cglib/beans/BeanCopier.java");
        assertNotNull(sourceCode);
        assertThat(sourceCode.contains("public class BeanCopier"), is(true));
    }

    @Test
    public void testSearchFile() throws Exception {
        List<String> result = PinganCodeUtil.searchFileInRepositoryByKeyword("test-repository", "BeanCopier");
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is("net/sf/cglib/beans/BeanCopier.java"));
    }

}
