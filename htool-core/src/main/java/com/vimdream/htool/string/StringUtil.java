package com.vimdream.htool.string;

/**
 * @Title: StringUtil
 * @Author vimdream
 * @ProjectName htool-core
 * @Date 2020/6/24 14:53
 */
public class StringUtil {

    /**
     * 字符串转换为指定类型
     * @param str
     * @param clazz
     * @return
     */
    public static <T> T convert(String str, Class<T> clazz) {
        if (isBlank(str) || clazz == null) return null;
        if (String.class.equals(clazz)) return (T) str;
        try {
            if (Integer.class.equals(clazz) || int.class.equals(clazz)) return (T) Integer.valueOf(str);
            else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) return (T) Byte.valueOf(str);
            else if (Long.class.equals(clazz) || long.class.equals(clazz)) return (T) Long.valueOf(str);
            else if (Double.class.equals(clazz) || double.class.equals(clazz)) return (T) Double.valueOf(str);
            else if (Float.class.equals(clazz) || float.class.equals(clazz)) return (T) Float.valueOf(str);
            else if (Character.class.equals(clazz) || char.class.equals(clazz)) return (T) new Character(str.charAt(0));
            else if (Short.class.equals(clazz) || short.class.equals(clazz)) return (T) Short.valueOf(str);
            else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) return (T) Boolean.valueOf(str);
        } catch (Exception e) {
            throw new RuntimeException("转换为" + clazz.getName() + "失败");
        }
        return null;
    }

    public static boolean isBlank(CharSequence s) {
        if (s != null && s.length() > 0) {
            for (int i = s.length() - 1; i >= 0; i--) {
                if (s.charAt(i) != ' ')
                    return false;
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence s) {
        return !isBlank(s);
    }

    public static boolean isNotEmpty(CharSequence s) {
        return s != null && s.length() > 0;
    }

    public static boolean isEmpty(CharSequence s) {
        return !isNotEmpty(s);
    }

    public static String format(String template, Object... args) {
        return String.format(template, args);
    }
}
