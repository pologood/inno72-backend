package com.inno72.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * gzip 压缩,解压缩工具类
 */
public class GZIPUtil {

    public static final String DEFAULT_GZIP_CHARSET = "UTF-8";

    /**
     * 压缩字符串为byte数组 默认使用 utf-8 编码
     */
    public static String compress(String str) {
        return compress(str, DEFAULT_GZIP_CHARSET);
    }

    /**
     * 压缩字符串为byte数组
     *
     * @param str     要进行压缩的字符串
     * @param charset 字符集
     */
    public static String compress(String str, String charset) {
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        GZIPOutputStream gzipOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(str.getBytes(charset));
            gzipOutputStream.close();//重要 否则byteArrayOutputStream返回的数据不完整 将剩余压缩数据写入输出流并关闭底层流。继续自DeflaterOutputStream
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encode(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解压gzip格式的byte数组 默认使用 utf-8 编码
     */
    public static String uncompress(String str) {
        return uncompress(str, DEFAULT_GZIP_CHARSET);
    }

    /**
     * 解压gzip格式的byte数组
     *
     * @param str     str
     * @param charset 字符集
     */
    public static String uncompress(String str, String charset) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        GZIPInputStream gzipInputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayInputStream = new ByteArrayInputStream(Base64.decode(str));
            gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            byte[] buffer = new byte[1024 * 10];
            int n = 0;
            while ((n = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, n);
                byteArrayOutputStream.flush();
            }
            return byteArrayOutputStream.toString(charset);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gzipInputStream != null) {
                    gzipInputStream.close();
                }
                if (gzipInputStream != null) {
                    byteArrayInputStream.close();
                }
                if (gzipInputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

   /* public static void main(String[] args) {
        String info = "46740df61be0508b856a7310a82d2747753eeb0e99eaf1a008a0d95ecde99f70831ba168ba4772a576a1fff931c6210b562b5eea7ff42147c2d97d99197020acdbf45ebd750271f4bf009475284c1966";
        System.out.println("-----info:" + info);
        String compress = GZIPUtil.compress(info);
        System.out.println("-----compress:" + compress);
        String uncompress = GZIPUtil.uncompress(compress);
        System.out.println("----uncompress:" + uncompress);
    }*/
}
