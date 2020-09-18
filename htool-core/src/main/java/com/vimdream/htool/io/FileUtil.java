package com.vimdream.htool.io;

import com.vimdream.htool.string.StringUtil;

import java.io.File;

/**
 * @Title: FileUtil
 * @Author vimdream
 * @ProjectName htool-io
 * @Date 2020/7/1 13:24
 */
public class FileUtil {

    public static void rename(String path, String newName) {
        rename(new File(path), newName);
    }

    public static void rename(File file, String newName) {
        file.renameTo(new File(file.getParentFile(), newName));
    }

    public static void copy(String sourcePath, String targetPath, boolean isForce) {
        if (StringUtil.isNotBlank(sourcePath) && StringUtil.isNotBlank(targetPath)) {
            copy(new File(sourcePath), new File(targetPath), isForce);
        }
    }

    public static void copy(File source, File target, boolean isForce) {
        if (source != null && target != null && source.exists() && target.isDirectory()) {
            if (source.isFile()) {
                copyFile(source, target, isForce);
            } else {
                copyDir(source, target, isForce);
            }
        }
    }

    /**
     * 复制文件
     * @param source
     * @param target
     * @param isForce
     */
    private static void copyFile(File source, File target, boolean isForce) {
        File newFile = new File(target, source.getName());
        if (!isForce && newFile.exists()) throw new IllegalArgumentException(newFile.getAbsolutePath() + "文件已经存在");

        IOUtil.transfer(source, newFile);
    }

    /**
     * 复制文件夹
     * @param source
     * @param target
     * @param isForce
     */
    private static void copyDir(File source, File target, boolean isForce) {

        File parentDir = new File(target, source.getName());
        parentDir.mkdirs();
        File[] files = source.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                copyFile(file, parentDir, isForce);
            } else {
                copyDir(file, parentDir, isForce);
            }
        }
    }

    public static void del(String path) {
        if (StringUtil.isNotBlank(path)) {
            del(new File(path));
        }
    }

    public static void del(File file) {
        if (file != null && file.exists()) {
            if (file.isFile())
                file.delete();
            else
                delDir(file);
        }
    }

    /**
     * 删除{dir}下的文件
     * @param dir
     */
    private static void delDir(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else {
                delDir(file);
            }
        }
        // 删除当前目录
        dir.delete();
    }

    /**
     * 生成文件分散存储路径
     * @return
     */
    public String getHashPath(String name) {
        String hashPath = null;
        int code = name.hashCode();
        String hex = Integer.toHexString(code);
        int len = hex.length();
        if (len < 2) {
            return "00" + File.separator + "00";
        }
        hashPath = hex.substring(0, 2) + File.separator;
        if (len >= 3) {
            hashPath += hex.substring(len - 2);
        } else {
            hashPath += "00";
        }
        return hashPath;
    }

}
