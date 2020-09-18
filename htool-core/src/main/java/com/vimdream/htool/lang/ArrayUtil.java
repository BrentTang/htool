package com.vimdream.htool.lang;

/**
 * @Title: ArrayUtil
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/6/28 13:20
 */
public class ArrayUtil {

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean hasNull(T[] array) {
        if (array != null && array.length > 0) {
            for (int i = array.length; i > 0; i--) {
                if (array[i] == null)
                    return true;
            }
            return false;
        }
        return false;
    }
}
