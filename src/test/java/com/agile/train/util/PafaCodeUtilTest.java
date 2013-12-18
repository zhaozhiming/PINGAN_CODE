package com.agile.train.util;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PafaCodeUtilTest {
    String jarFileName = "test-repository/cglib-2.2-sources.jar";
    String searchKeyword = "BeanCopier";
    String contentFileName = "net/sf/cglib/beans/BeanCopier.java";

    @Test
    public void testLoadZipFile() throws Exception {
        String sourceCode = PafaCodeUtil.loadZipFile(jarFileName, contentFileName);
        assertNotNull(sourceCode);
    }

    @Test
    public void testSearchFile() throws Exception {
        List<String> result = PafaCodeUtil.searchFile(jarFileName, searchKeyword);
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is("net/sf/cglib/beans/BeanCopier.java"));
    }

}
