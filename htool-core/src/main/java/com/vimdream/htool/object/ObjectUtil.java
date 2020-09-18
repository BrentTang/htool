package com.vimdream.htool.object;

import com.vimdream.htool.string.StringUtil;

/**
 * @Title: ObjectUtil
 * @Author vimdream
 * @ProjectName htool-core
 * @Date 2020/6/24 14:51
 */
public class ObjectUtil {

    public static <T> T convert(Object object, Class<T> clazz) {

        if (object == null) {
            return null;
        }

        if (clazz.isInstance(object)) {
            return (T) object;
        }

        return StringUtil.convert(object.toString(), clazz);

    }


}
