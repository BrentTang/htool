package com.vimdream.htool.collection;

import java.util.*;

/**
 * @Title: CollectionUtil
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/6/28 13:17
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /*public static <T> List<T> asList(T... args) {
        List<T> list = new ArrayList<>();
        for (T arg : args) {
            list.add(arg);
        }
        return list;
    }*/

    public static <T> Set<T> asSet(T... args) {
        HashSet<T> set = new HashSet<>();
        for (T arg : args) {
            set.add(arg);
        }
        return set;
    }
}
