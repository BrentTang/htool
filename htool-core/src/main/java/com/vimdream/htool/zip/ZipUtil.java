package com.vimdream.htool.zip;

import com.vimdream.htool.string.StringUtil;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Title: ZipUtil
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/8/7 14:20
 */
public class ZipUtil {

    public static void compress(String srcFilePath, String destFilePath, String baseDir) {

        File src = new File(srcFilePath);
        if (!src.exists()) {
            throw new RuntimeException(srcFilePath + "不存在");
        }

        try {
            compress(srcFilePath, new FileOutputStream(destFilePath), baseDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compress(String srcFilePath, OutputStream out, String baseDir) {

        File src = new File(srcFilePath);
        if (!src.exists()) {
            throw new RuntimeException(srcFilePath + "不存在");
        }

        try {
            ZipOutputStream zos = new ZipOutputStream(out);
            if (StringUtil.isNotBlank(baseDir)) {
                if (src.isDirectory()) {
                    File[] files = src.listFiles();
                    for (File file : files) {
                        compress(file, zos, baseDir + File.separator);
                    }
                } else {
                    compress(src, zos, baseDir + File.separator);
                }
            } else {
                compress(src, zos, "");
            }
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compress(List<File> files, String destFilePath, String baseDir) {

        try {
            compress(files, new FileOutputStream(destFilePath), baseDir);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void compress(List<File> files, OutputStream out, String baseDir) {

        try {
            ZipOutputStream zos = new ZipOutputStream(out);

            baseDir = StringUtil.isNotBlank(baseDir) ? baseDir : "";
            for (File file : files) {
                compress(file, zos, baseDir + File.separator);
            }
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 压缩
     * @param src
     * @param zos
     * @param baseDir
     */
    private static void compress(File src, ZipOutputStream zos, String baseDir) {

        if (!src.exists()) {
            return;
        }
        if (src.isFile()) {
            compressFile(src, zos, baseDir);
        } else if (src.isDirectory()) {
            compressDir(src, zos, baseDir);
        }

    }

    /**
     * 压缩文件
     * @param file
     * @param zos
     * @param baseDir
     */
    private static void compressFile(File file, ZipOutputStream zos,String baseDir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zos.putNextEntry(entry);
            int count;
            byte[] buf = new byte[1024];
            while ((count = bis.read(buf)) != -1) {
                zos.write(buf, 0, count);
            }
            bis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩目录
     * @param dir
     * @param zos
     * @param baseDir
     */
    private static void compressDir(File dir, ZipOutputStream zos, String baseDir) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if(files.length == 0){
            try {
                zos.putNextEntry(new ZipEntry(baseDir + dir.getName() + File.separator));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File file : files) {
            compress(file, zos, baseDir + dir.getName() + File.separator);
        }
    }

}
