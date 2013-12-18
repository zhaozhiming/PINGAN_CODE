package com.agile.train.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PafaCodeUtil {

    public static String loadZipFile(String zipname, String name) throws IOException {

        StringBuffer sb = new StringBuffer();
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipname));
            ZipEntry entry;
            //System.out.println("");
            while ((entry = zin.getNextEntry()) != null) {
                if (entry.getName().equals(name)) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(zin));
                    String s = null;
                    while ((s = in.readLine()) != null)
                        //System.out.println(s + "\n");
                        sb.append(s).append("\n");
                }
                zin.closeEntry();
            }
            zin.close();
            return sb.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    public static List<String> searchFile(String JarFileName, String searchKeyword) throws IOException{
        List<String> list = getZipFileList(JarFileName);
        List<String> resultList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            String fileName = list.get(i);
            if(fileName.indexOf(searchKeyword)!= -1){
                resultList.add(list.get(i));
            }
        }
        return resultList;
    }

    /**
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static List<String> getZipFileList(String fileName) throws FileNotFoundException,
            IOException {
        List<String> list = new ArrayList<String>();
        File file = new File(fileName);
        ZipInputStream input = null;         // 定义压缩输入流
        input = new ZipInputStream(new  FileInputStream(file));
        ZipEntry entry;                                        // 实例化压缩输入流
        while ((entry = input.getNextEntry()) != null) {
            if(entry.getName().endsWith(".java")){
                //System.out.println("压缩实体名称：" +  entry.getName()) ;
                list.add(entry.getName());
            }
        }
        input.close();                    // 关闭压缩输入流
        return list;
    }

}
