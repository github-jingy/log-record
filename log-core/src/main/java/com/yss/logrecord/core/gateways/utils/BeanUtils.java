package com.yss.logrecord.core.gateways.utils;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.yss.logrecord.core.gateways.annotation.PropertyMapper;
import com.yss.logrecord.core.gateways.annotation.PropertyMappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * <p>
 * BeanUtil
 * </p>
 *
 * @author liuliangxing
 * @since 2019-05-13
 */
@Slf4j
public class BeanUtils extends org.springframework.beans.BeanUtils {

    private static final int STREAM_BOUNDARY = 10000;
    private static final Map<String, ConstructorAccess> constructorAccessCache = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, String>> propertyCache = new ConcurrentHashMap<>();
    private static String CHARACTER_VERTICAL_LINE = "||";

    /**
     * 复制对象中的值
     *
     * @param source 源
     * @param target 目标
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, null, (String[]) null);
    }

    /**
     * 复制对象中的值
     *
     * @param source   源
     * @param target   目标
     * @param editable 限制target对象
     */
    public static void copyProperties(Object source, Object target, Class<?> editable) {
        copyProperties(source, target, editable, (String[]) null);
    }

    /**
     * 复制对象中的值
     *
     * @param source           源
     * @param target           目标
     * @param ignoreProperties 忽略属性
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        copyProperties(source, target, null, ignoreProperties);
    }

    /**
     * 复制对象中的值
     *
     * @param source           源
     * @param target           目标
     * @param editable         限制target对象
     * @param ignoreProperties 忽略属性
     */
    public static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        copyPropertiesParent(source, target, editable, ignoreProperties);
        Class<?> targetClass = target.getClass();
        Map<String, String> propertyCache = getPropertyCache(source.getClass(), targetClass);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) :
                Collections.emptyList());
        if (!CollectionUtils.isEmpty(propertyCache)) {
            for (Map.Entry<String, String> entry : propertyCache.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String[] targetSplit = key.split("\\|\\|");
                String targetValue = targetSplit[1];
                if (ignoreList.contains(targetValue)) {
                    continue;
                }
                PropertyDescriptor sourcePropertyDescriptor = getPropertyDescriptor(source.getClass(), value);
                PropertyDescriptor targetPropertyDescriptor = getPropertyDescriptor(targetClass, targetValue);
                if (targetPropertyDescriptor == null) {
                    log.warn(String.format("[ %s ] not found property [ %s ]", targetClass.getName(), targetValue));
                    continue;
                }
                if (sourcePropertyDescriptor == null) {
                    log.warn(String.format("[ %s ] not found property [ %s ]", source.getClass().getName(), value));
                    continue;
                }
                try {
                    Method sourceReadMethod = sourcePropertyDescriptor.getReadMethod();
                    Method targetWriteMethod = targetPropertyDescriptor.getWriteMethod();
                    Object param = sourceReadMethod.invoke(source);
                    targetWriteMethod.invoke(target, param);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }


    /**
     * 复制对象中的值
     *
     * @param source      源
     * @param targetClass 目标类
     * @param <T>         目标类泛型
     * @return 目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        return copyProperties(source, targetClass, null, (String[]) null);
    }

    /**
     * 复制对象中的值
     *
     * @param source      源
     * @param targetClass 目标类
     * @param editable    限制target对象
     * @param <T>         目标类泛型
     * @return 目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, Class<?> editable) {
        return copyProperties(source, targetClass, editable, (String[]) null);
    }

    /**
     * 复制对象中的值
     *
     * @param source           源
     * @param targetClass      目标类
     * @param <T>              目标类泛型
     * @param ignoreProperties 忽略属性
     * @return 目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, String... ignoreProperties) {
        return copyProperties(source, targetClass, null, ignoreProperties);
    }

    /**
     * 复制对象中的值
     *
     * @param source           源
     * @param targetClass      目标类
     * @param <T>              目标类泛型
     * @param editable         限制target对象
     * @param ignoreProperties 忽略属性
     * @return 目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass, Class<?> editable,
                                       String... ignoreProperties) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(targetClass, "Target must not be null");
        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        T t = constructorAccess.newInstance();
        copyProperties(source, t, editable, ignoreProperties);
        return t;
    }


    /**
     * 复制集合中的对象
     *
     * @param sources 源集合
     * @param clazz   目标集合类型
     * @param <T>     返回集合
     * @return List
     */
    public static <T> List<T> copyPropertiesOfList(Collection sources, Class<T> clazz) {
        return copyPropertiesOfList(sources, clazz, null, false);
    }

    /**
     * 复制集合中的对象
     *
     * @param sources  源集合
     * @param clazz    目标集合类型
     * @param <T>      返回集合
     * @param consumer
     * @return List
     */
    public static <T> List<T> copyPropertiesOfList(Collection sources, Class<T> clazz, Consumer<T> consumer) {
        return copyPropertiesOfList(sources, clazz, consumer, false);
    }

    /**
     * 复制集合中的对象
     *
     * @param sources  源集合
     * @param clazz    目标集合类型
     * @param <T>      返回集合
     * @param parallel 并行处理
     * @return List
     */
    public static <T> List<T> copyPropertiesOfList(Collection sources, Class<T> clazz, boolean parallel) {
        return copyPropertiesOfList(sources, clazz, null, parallel);
    }

    /**
     * 复制集合中的对象,并可以操作对象
     *
     * @param sources     源集合
     * @param targetClass 目标集合类型
     * @param consumer
     * @param <T>         返回集合
     * @param parallel    是否并行
     * @return List
     */
    public static <T> List<T> copyPropertiesOfList(Collection sources, Class<T> targetClass, Consumer<T> consumer,
                                                   boolean parallel) {
        int size = sources.size();
        List<T> targets = Lists.newArrayListWithCapacity(size);
        try {
            // 当数据量大于 STREAM_BOUNDARY 时使用 并行流
            if (parallel && size > STREAM_BOUNDARY) {
                sources.parallelStream().forEach((source) -> {
                    T t = copyProperties(source, targetClass);
                    if (consumer != null) {
                        consumer.accept(t);
                    }
                    targets.add(t);
                });
            } else {
                for (Object source : sources) {
                    T t = copyProperties(source, targetClass);
                    if (consumer != null) {
                        consumer.accept(t);
                    }
                    targets.add(t);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return targets;
    }


    /**
     * 获取缓存
     *
     * @param sourceClass 源
     * @param targetClass 目标
     * @return java.util.Map
     */
    private static Map<String, String> getPropertyCache(Class<?> sourceClass, Class<?> targetClass) {
        String classKey = sourceClass.getName() + CHARACTER_VERTICAL_LINE + targetClass.getName();
        if (propertyCache.containsKey(classKey)) {
            return propertyCache.get(classKey);
        }
        setPropertyCache(sourceClass);
        setPropertyCache(targetClass);
        return propertyCache.get(classKey);
    }

    private static List<Field> getClassFields(Class<?> clazz) {
        List<Field> fields = Lists.newArrayListWithExpectedSize(10);
        // 获取父类的字段
        getSuperClassFields(clazz, fields);
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    private static void getSuperClassFields(Class<?> clazz, List<Field> fields) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != Object.class) {
            fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
            getSuperClassFields(superclass, fields);
        }
    }

    /**
     * 设置缓存
     *
     * @param clazz 带设置的Class
     */
    private static void setPropertyCache(Class<?> clazz) {
        String mapperClassName = clazz.getName();
        // 获取映射类的字段
        List<Field> fields = getClassFields(clazz);
        for (Field field : fields) {
            Map<String, PropertyMapper> propertyMapperMap = Maps.newHashMap();
            if (field.isAnnotationPresent(PropertyMapper.class)) {
                PropertyMapper propertyMapper = field.getAnnotation(PropertyMapper.class);
                String key;
                if (!StringUtils.isEmpty(propertyMapper.sourceReference())) {
                    key = propertyMapper.sourceReference();
                } else {
                    key = propertyMapper.sourceClass().getName();
                }
                propertyMapperMap.put(key, propertyMapper);
            }
            if (field.isAnnotationPresent(PropertyMappers.class)) {
                PropertyMappers propertyMappers = field.getAnnotation(PropertyMappers.class);
                PropertyMapper[] value = propertyMappers.value();
                for (PropertyMapper propertyMapper : value) {
                    String key;
                    if (!StringUtils.isEmpty(propertyMapper.sourceReference())) {
                        key = propertyMapper.sourceReference();
                    } else {
                        key = propertyMapper.sourceClass().getName();
                    }
                    if (!propertyMapperMap.containsKey(key)) {
                        propertyMapperMap.put(key, propertyMapper);
                    }
                }
            }
            for (Map.Entry<String, PropertyMapper> mapperEntry : propertyMapperMap.entrySet()) {
                PropertyMapper propertyMapper = mapperEntry.getValue();
                Class<?> targetClass;
                String targetClassName;
                if (!StringUtils.isEmpty(propertyMapper.sourceReference())) {
                    targetClassName = propertyMapper.sourceReference();
                    try {
                        targetClass = Class.forName(targetClassName);
                    } catch (ClassNotFoundException e) {
                        continue;
                    }
                } else {
                    targetClass = propertyMapper.sourceClass();
                    targetClassName = targetClass.getName();
                }
                String targetProperty = propertyMapper.property();
                Map<String, String> mapperMap = getMap(targetClass, clazz, fields.size());
                mapperMap.put(mapperClassName + CHARACTER_VERTICAL_LINE + field.getName(),
                        targetProperty);
                Map<String, String> targetMap = getMap(clazz, targetClass, fields.size());
                targetMap.put(targetClassName + CHARACTER_VERTICAL_LINE + targetProperty,
                        field.getName());
            }
        }
    }

    private static Map<String, String> getMap(Class<?> class1, Class<?> class2, int length) {
        String key = class1.getName() + CHARACTER_VERTICAL_LINE + class2.getName();
        if (propertyCache.containsKey(key)) {
            return propertyCache.get(key);
        }
        Map<String, String> propertyMap = Maps.newHashMapWithExpectedSize(length);
        propertyCache.put(key, propertyMap);
        return propertyMap;
    }

    /**
     * 获取对象
     *
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T getObject(Class<T> targetClass) {
        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        return constructorAccess.newInstance();
    }

    /**
     * 获取类的构造对象
     *
     * @param targetClass 目标类
     * @param <T>
     * @return
     */
    private static <T> ConstructorAccess<T> getConstructorAccess(Class<T> targetClass) {
        ConstructorAccess<T> constructorAccess = constructorAccessCache.get(targetClass.toString());
        if (constructorAccess != null) {
            return constructorAccess;
        }
        try {
            constructorAccess = ConstructorAccess.get(targetClass);
            constructorAccess.newInstance();
            constructorAccessCache.put(targetClass.toString(), constructorAccess);
        } catch (Exception e) {
            log.error(String.format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
        }
        return constructorAccess;
    }

    private static void copyPropertiesParent(Object source, Object target, Class<?> editable,
                                             String... ignoreProperties)
            throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                Class writeParamClass = writeMethod.getParameterTypes()[0];
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        boolean assignable = ClassUtils.isAssignable(writeMethod.getParameterTypes()[0],
                                readMethod.getReturnType());
                        boolean isCollection =
                                Collection.class.isAssignableFrom(writeParamClass);
                        if (assignable && !isCollection) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(source);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            } catch (Throwable ex) {
                                throw new FatalBeanException(
                                        "Could not copy property '" + targetPd.getName() + "' from source to target",
                                        ex);
                            }
                        } else {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object sourceValue = readMethod.invoke(source);
                                Object targetValue = null;
                                if (isCollection) {
                                    List targetValues = new ArrayList();
                                    Type genericParameterType = writeMethod.getGenericParameterTypes()[0];
                                    ParameterizedType pt =
                                            (ParameterizedType) genericParameterType;
                                    Class tempClass = (Class) pt.getActualTypeArguments()[0];
                                    List<Class> classList = new ArrayList<>(8);
                                    classList.add(String.class);
                                    classList.add(CharSequence.class);
                                    classList.add(Integer.class);
                                    classList.add(Short.class);
                                    classList.add(Byte.class);
                                    classList.add(Long.class);
                                    classList.add(Double.class);
                                    classList.add(Float.class);
                                    if (classList.contains(tempClass)) {
                                        if (sourceValue != null) {
                                            targetValues = new ArrayList((Collection) sourceValue);
                                        }
                                    } else if (sourceValue != null) {
                                        for (Object value : (Collection) sourceValue) {
                                            Object tempObj = tempClass.newInstance();
                                            copyPropertiesParent(value, tempObj, editable, ignoreProperties);
                                            targetValues.add(tempObj);
                                        }
                                    }
                                    targetValue = targetValues;
                                } else if (sourceValue != null) {
                                    ConstructorAccess constructorAccess = getConstructorAccess(writeParamClass);
                                    targetValue = constructorAccess.newInstance();
                                    copyPropertiesParent(sourceValue, targetValue, editable, ignoreProperties);
                                }

                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, targetValue);
                            } catch (Throwable ex) {
                                throw new FatalBeanException(
                                        "Could not copy property '" + targetPd.getName() + "' from source to target",
                                        ex);
                            }
                        }

                    }
                }
            }
        }
    }
}