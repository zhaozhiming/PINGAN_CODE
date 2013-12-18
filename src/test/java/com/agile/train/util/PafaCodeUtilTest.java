package com.agile.train.util;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;

public class PafaCodeUtilTest {
    String zipfileName = "test-repository/cglib-2.2-sources.jar";
    String searchName = "BeanCopier";
    String contentFileName = "javax/security/auth/Policy.java";

    @Test
    public void testLoadZipFile() throws Exception {
        assertNotNull(PafaCodeUtil.loadZipFile(zipfileName, contentFileName));
    }

    @Test
    public void testSearchFile() throws Exception {
        List<String> result = PafaCodeUtil.searchFile(zipfileName, searchName);
        assertNotNull(result);
    }

}
