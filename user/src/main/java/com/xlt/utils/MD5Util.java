package com.xlt.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Util {

    /**
     * 加密密码
     *
     * @param pass 原文
     * @return 密文
     */
    public static String encryptPassword(String pass) {
        try {
            byte[] bytes = encryptionStr(pass);
            return toHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("encrypt error:", e);
        }
        return null;
    }

    /**
     * 字符串的加密
     *
     * @param str
     * @return byte[]
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encryptionStr(String str) throws NoSuchAlgorithmException {
        if (str != null) {
            //加密后得到的字节数组
            byte[] bytes = null;
            // 获取MD5算法实例 得到一个md5的消息摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //添加要进行计算摘要的信息
            md.update(str.getBytes());
            //得出摘要结果
            bytes = md.digest();
            return bytes;
        }
        return null;
    }

    //字符数组转成字符串
    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    //密码验证
    /*
     * 将加密后的字节数组转换成十六进制的字符窜，形成最终的密码。
     * 当输入字符串经过MD5加密后，得到的字符串与密码一样，则认为密码验证通过。
     * */
    public static boolean validPassword(String inputPassword, String dbPassword) throws NoSuchAlgorithmException {
        String passwordByMD5 = toHex(encryptionStr(inputPassword));
        if (dbPassword.equals(passwordByMD5)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String a = "test1";
        String b = "test2";
        String c = "Xlt@1314";
        String[] str = {a, b, c};
        for (String s : str) {
            byte[] bytes = encryptionStr(s);
            System.out.println("数据：" + s + " 加密后为：" + bytes.toString());
        }
        String dbPassword = toHex(encryptionStr("Xlt@1314"));
        System.out.println(dbPassword);
        String inputPassword = "Xlt@1314";
        String inputPassword2 = "abcdeffff";
        System.out.println(validPassword(inputPassword, dbPassword));
        System.out.print(validPassword(inputPassword, inputPassword2));
    }
}