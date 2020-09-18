package com.vimdream.htool.reflect;

import com.vimdream.htool.lang.Assert;
import com.vimdream.htool.string.StringUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Title: ReflectSupport
 * @Author vimdream
 * @ProjectName htool-core
 * @Date 2020/6/24 16:47
 */
public class ReflectSupport {

    private Object target;

    public ReflectSupport(Object target) {
        this.target = target;
    }

    /**
     * 获取所有带有{annotationClass}的  字段名 -> {anno}
     * @param annotationClass
     * @param <T>
     * @return
     */
    public <T extends Annotation> Map<String, T> getAllFieldAnno(Class<T> annotationClass) {
        Assert.notNull(this.target, "目标对象不能为空", new Object[0]);
        Map<String, T> annotations = new HashMap<>();
        Class<?> targetClass = this.target.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            T anno = field.getAnnotation(annotationClass);
            if (anno != null)
                annotations.put(field.getName(), anno);
        }
        return annotations;
    }

    /**
     * 获取所有字段的字段名
     * @return
     */
    public List<String> getFieldsName() {
        Assert.notNull(this.target, "目标对象不能为空", new Object[0]);
        List<String> fieldsName = new ArrayList<>();
        Class<?> targetClass = this.target.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            fieldsName.add(field.getName());
        }
        return fieldsName;
    }

    /**
     * 通过{fieldName}获取字段值
     * @param fieldName
     * @return
     */
    public Object getFieldValue(String fieldName) {
        Assert.notNull(this.target, "目标对象不能为空", new Object[0]);
        Class<?> targetClass = this.target.getClass();
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            return field.get(this.target);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("未发现Field:" + fieldName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("无法访问Field:" + fieldName);
        }
    }

    /**
     * 获取私有变量值
     * @param fieldName
     * @return
     */
    public Object getFieldValueForce(String fieldName) {
        Assert.notNull(this.target, "目标对象不能为空", new Object[0]);
        Class<?> targetClass = this.target.getClass();
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(this.target);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("未发现Field:" + fieldName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("无法访问Field:" + fieldName);
        }
    }

    /**
     * 通过无参构造实例化{targetClass}
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T getInstanceByNoArgs(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取该名为{argName}在方法中的索引位置
     * @param className
     * @param methodName
     * @param argName
     * @return
     */
    @Deprecated
    public static int argIndexOfMethod(String className, String methodName, String argName) {
        if (StringUtil.isBlank(className) ||
                StringUtil.isBlank(methodName) ||
                StringUtil.isBlank(argName))
            return -1;
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass cClass = classPool.get(className);
            CtMethod method = cClass.getDeclaredMethod(methodName);
            return argIndexOfMethod(method, argName);
        } catch (NotFoundException e) {
            throw new RuntimeException(className + "不存在");
        }
    }

    /**
     * 获取该名为{argName}在方法中的索引位置
     * @param className
     * @param methodName
     * @param mParameterTypes  推荐传入方法的参数类型  可以判断重载的方法  因此更精确
     * @param argName
     * @return
     */
    public static int argIndexOfMethod(String className, String methodName, Class[] mParameterTypes, String argName) {
        if (StringUtil.isBlank(className) ||
                StringUtil.isBlank(methodName) ||
                StringUtil.isBlank(argName))
            return -1;
        try {
            ClassPool classPool = ClassPool.getDefault();
            CtClass cClass = classPool.get(className);
            CtMethod method = null;
            if (mParameterTypes != null && mParameterTypes.length > 0) {
                CtClass[] typeClasses = new CtClass[mParameterTypes.length];
                for (int i = 0; i < mParameterTypes.length; i++) {
                    typeClasses[i] = classPool.get(mParameterTypes[i].getName());
                }
                method = cClass.getDeclaredMethod(methodName, typeClasses);
            } else {
                method = cClass.getDeclaredMethod(methodName);
            }
            return argIndexOfMethod(method, argName);
        } catch (NotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取方法参数名称
     * @param cm
     * @throws NotFoundException
     */
    protected static List<String> getParamNames(CtMethod cm) {
        // 静态方法与非静态方法索引不同
        int staticIndex = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;

        try {
            ArrayList<String> paramNames = new ArrayList<>();
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attributes = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            for (int i = 0; i < cm.getParameterTypes().length; i++) {
                paramNames.add(attributes.variableName(staticIndex + i));
            }
            return paramNames;
        } catch (NotFoundException e) {
            throw new RuntimeException("未发现参数");
        }

    }

    protected static int argIndexOfMethod(CtMethod cm, String argName) {
        // 静态方法与非静态方法索引不同
        int staticIndex = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;

        try {
            ArrayList<String> paramNames = new ArrayList<>();
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attributes = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            for (int i = 0; i < cm.getParameterTypes().length; i++) {
                String curArgName = attributes.variableName(staticIndex + i);
                if (curArgName.equals(argName)) {
                    return i;
                }
            }
            return -1;
        } catch (NotFoundException e) {
            throw new RuntimeException("未发现参数");
        }
    }
}
