package com.vimdream.htool.lang;

import com.vimdream.htool.collection.CollectionUtil;
import com.vimdream.htool.string.StringUtil;

import java.util.Collection;
import java.util.Map;

/**
 * @Title: Assert
 * @Author vimdream
 * @ProjectName htool
 * @Date 2020/6/28 13:12
 */
public class Assert {

    public static void isTrue(boolean expression, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (!expression) {
            throw new IllegalArgumentException(String.format(errorMsgTemplate, params));
        }
    }

    public static void isTrue(boolean expression) throws IllegalArgumentException {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isFalse(boolean expression, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (expression) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        }
    }

    public static void isFalse(boolean expression) throws IllegalArgumentException {
        isFalse(expression, "[Assertion failed] - this expression must be false");
    }

    public static void isNull(Object object, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (object != null) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        }
    }

    public static void isNull(Object object) throws IllegalArgumentException {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static <T> T notNull(T object, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return object;
        }
    }

    public static <T> T notNull(T object) throws IllegalArgumentException {
        return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static <T extends CharSequence> T notEmpty(T text, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (StringUtil.isEmpty(text)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return text;
        }
    }

    public static <T extends CharSequence> T notEmpty(T text) throws IllegalArgumentException {
        return notEmpty(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static <T extends CharSequence> T notBlank(T text, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (StringUtil.isBlank(text)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return text;
        }
    }

    public static <T extends CharSequence> T notBlank(T text) throws IllegalArgumentException {
        return notBlank(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    public static String notContain(String textToSearch, String substring, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (StringUtil.isNotEmpty(textToSearch) && StringUtil.isNotEmpty(substring) && textToSearch.contains(substring)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return substring;
        }
    }

    public static String notContain(String textToSearch, String substring) throws IllegalArgumentException {
        return notContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [{}]", substring);
    }

    public static Object[] notEmpty(Object[] array, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return array;
        }
    }

    public static Object[] notEmpty(Object[] array) throws IllegalArgumentException {
        return notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    public static <T> T[] noNullElements(T[] array, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (ArrayUtil.hasNull(array)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return array;
        }
    }

    public static <T> T[] noNullElements(T[] array) throws IllegalArgumentException {
        return noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    public static <T> Collection<T> notEmpty(Collection<T> collection, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (CollectionUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return collection;
        }
    }

    public static <T> Collection<T> notEmpty(Collection<T> collection) throws IllegalArgumentException {
        return notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    public static <K, V> Map<K, V> notEmpty(Map<K, V> map, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (CollectionUtil.isEmpty(map)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return map;
        }
    }

    public static <K, V> Map<K, V> notEmpty(Map<K, V> map) throws IllegalArgumentException {
        return notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    public static <T> T isInstanceOf(Class<?> type, T obj) {
        return isInstanceOf(type, obj, "Object [{}] is not instanceof [{}]", obj, type);
    }

    public static <T> T isInstanceOf(Class<?> type, T obj, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        } else {
            return obj;
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) throws IllegalArgumentException {
        isAssignable(superType, subType, "{} is not assignable to {})", subType, superType);
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(StringUtil.format(errorMsgTemplate, params));
        }
    }

    public static void state(boolean expression, String errorMsgTemplate, Object... params) throws IllegalStateException {
        if (!expression) {
            throw new IllegalStateException(StringUtil.format(errorMsgTemplate, params));
        }
    }

    public static void state(boolean expression) throws IllegalStateException {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }

    public static int checkIndex(int index, int size) throws IllegalArgumentException, IndexOutOfBoundsException {
        return checkIndex(index, size, "[Assertion failed]");
    }

    public static int checkIndex(int index, int size, String errorMsgTemplate, Object... params) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (index >= 0 && index < size) {
            return index;
        } else {
            throw new IndexOutOfBoundsException(badIndexMsg(index, size, errorMsgTemplate, params));
        }
    }

    public static int checkBetween(int value, int min, int max) {
        if (value >= min && value <= max) {
            return value;
        } else {
            throw new IllegalArgumentException(StringUtil.format("Length must be between {} and {}.", new Object[]{min, max}));
        }
    }

    public static long checkBetween(long value, long min, long max) {
        if (value >= min && value <= max) {
            return value;
        } else {
            throw new IllegalArgumentException(StringUtil.format("Length must be between {} and {}.", new Object[]{min, max}));
        }
    }

    public static double checkBetween(double value, double min, double max) {
        if (value >= min && value <= max) {
            return value;
        } else {
            throw new IllegalArgumentException(StringUtil.format("Length must be between {} and {}.", new Object[]{min, max}));
        }
    }

    public static Number checkBetween(Number value, Number min, Number max) {
        notNull(value);
        notNull(min);
        notNull(max);
        double valueDouble = value.doubleValue();
        double minDouble = min.doubleValue();
        double maxDouble = max.doubleValue();
        if (valueDouble >= minDouble && valueDouble <= maxDouble) {
            return value;
        } else {
            throw new IllegalArgumentException(StringUtil.format("Length must be between {} and {}.", new Object[]{min, max}));
        }
    }

    private static String badIndexMsg(int index, int size, String desc, Object... params) {
        if (index < 0) {
            return StringUtil.format("{} ({}) must not be negative", new Object[]{StringUtil.format(desc, params), index});
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else {
            return StringUtil.format("{} ({}) must be less than size ({})", new Object[]{StringUtil.format(desc, params), index, size});
        }
    }

}
