package com.vimdream.htool.reflect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Title: JoinPointUtil
 * @Author vimdream
 * @ProjectName htool-core
 * @Date 2020/6/7 22:59
 */
public class JoinPointUtil {

    public static <T extends Annotation> T getMethodAnno(ProceedingJoinPoint proceedingJoinPoint, Class<T> annoClazz) throws NoSuchMethodException {

        // 获取当前执行方法的方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        // 获取当前执行方法的参数类型
        Class[] parameterTypes =
                ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterTypes();
        // 获取当前方法的调用对象
        Object target = proceedingJoinPoint.getTarget();
        // 获取调用对象的Class
        Class<?> clazz = target.getClass();
        // 获取方法
        Method method = clazz.getMethod(methodName, parameterTypes);
        // 获取注解
        return method.getAnnotation(annoClazz);

    }

    public static <T extends Annotation> T getClassAnno(ProceedingJoinPoint proceedingJoinPoint, Class<T> annoClazz) throws NoSuchMethodException {

        // 获取当前方法的调用对象
        Object target = proceedingJoinPoint.getTarget();
        // 获取调用对象的Class
        Class<?> clazz = target.getClass();
        // 获取注解
        return clazz.getAnnotation(annoClazz);

    }

    public static String getClassName(ProceedingJoinPoint proceedingJoinPoint) {
        // 获取当前方法的调用对象
        Object target = proceedingJoinPoint.getTarget();
        // 获取调用对象的Class
        Class<?> clazz = target.getClass();
        // 获取注解
        return clazz.getName();
    }
}
