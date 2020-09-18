package com.vimdream.htool.encrypt;

import java.util.Random;

/**
 * @Title: RC4Util
 * @Author vimdream
 * @ProjectName htool-core
 * @Date 2020/6/28 10:00
 */
public class RC4Util {

    public static String getCode(int id, String secret, int maxIdLen) {
        String s = RC4(String.format("%0" + (maxIdLen >> 1) + "d", id), secret);
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            code.append(Integer.toHexString((int) s.charAt(i)));
        }
        for (int i = maxIdLen - code.length(); i > 0; i--) {
            code.insert(0, "f");
        }
        return code.subSequence(0, Integer.min(maxIdLen * 2, code.length())).toString().toUpperCase();
    }

    /**
     * 生成长度为{maxIdLen}的随机码
     * @param secret
     * @param maxIdLen
     * @return
     */
    public static String getCode(String secret, int maxIdLen) {
        int idLen = maxIdLen >> 1;
        StringBuilder max = new StringBuilder();
        for (int i = 0; i < idLen; i++) {
            max.append('9');
        }
        return getCode(new Random().nextInt(Integer.valueOf(max.toString())), secret, maxIdLen);
    }

    public static String RC4(String aInput, String aKey) {
        int[] iS = new int[256];
        byte[] iK = new byte[256];

        for (int i=0;i<256;i++)
            iS[i]=i;

        int j = 1;

        for (short i= 0;i<256;i++) {
            iK[i]=(byte)aKey.charAt((i % aKey.length()));
        }
        j=0;

        for (int i=0;i<255;i++) {
            j=(j+iS[i]+iK[i]) % 256;
            int temp = iS[i];
            iS[i]=iS[j];
            iS[j]=temp;
        }
        int i=0;
        j=0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for(short x = 0;x<iInputChar.length;x++) {
            i = (i+1) % 256;
            j = (j+iS[i]) % 256;
            int temp = iS[i];
            iS[i]=iS[j];
            iS[j]=temp;
            int t = (iS[i]+(iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char)iY;
            iOutputChar[x] =(char)( iInputChar[x] ^ iCY) ;
        }
        return new String(iOutputChar);
    }

}
