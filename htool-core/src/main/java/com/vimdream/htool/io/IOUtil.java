package com.vimdream.htool.io;

import java.io.*;

/**
 * @Title: IOUtil
 * @Author vimdream
 * @ProjectName htool-io
 * @Date 2020/6/28 11:39
 */
public class IOUtil {

    public static void transfer(File from, File to) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(to));
            transfer(bis, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void transfer(InputStream from, OutputStream to) {
        try (InputStream is = from; OutputStream os = to) {
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) > 0) {
                os.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {}
        }
    }

    public static void store(String text, String filePath, boolean append) {
        File file = new File(filePath);
        if (file.isDirectory()) throw new IllegalArgumentException("确保 " + filePath + "为文件路径");
        store(text, file, append);
    }

    public static void store(String text, File file, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
