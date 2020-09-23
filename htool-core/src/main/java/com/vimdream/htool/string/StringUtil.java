package com.vimdream.htool.string;

import com.vimdream.htool.lang.Assert;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> split(String str, char separator, char banPrefix, char banSuffix) {
        Assert.notBlank(str, "字符串不能为空");
        Assert.isFalse(separator == banPrefix, "%c(separator) == %c(banPrefix) 不能相同", separator, banPrefix);
        Assert.isFalse(separator == banSuffix, "%c(separator) == %c(banSuffix) 不能相同", separator, banSuffix);
        Assert.isFalse(banPrefix == banSuffix, "%c(banPrefix) == %c(banSuffix) 不能相同", banPrefix, banSuffix);

        int stack = 0;

        List<String> res = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 越过禁止区
            if (c == banPrefix) {
                temp.append(c);
                stack++;
                while (stack != 0) {
                    c = str.charAt(++i);
                    temp.append(c);
                    if (c == banPrefix) {
                        stack++;
                        continue;
                    }
                    if (c == banSuffix){ stack--; }
                }
                res.add(temp.toString());
                temp = new StringBuilder();
                c = str.charAt(++i);
            }

            if (c != separator) {
                temp.append(c);
            } else {
                if (temp.length() > 0) {
                    res.add(temp.toString());
                    temp = new StringBuilder();
                }
            }

        }
        if (temp.length() > 0) {
            res.add(temp.toString());
            temp = new StringBuilder();
        }
        return res;
    }
}
