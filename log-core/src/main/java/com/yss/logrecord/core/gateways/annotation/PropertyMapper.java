package com.yss.logrecord.core.gateways.annotation;

import java.io.Serializable;
import java.lang.annotation.*;

/**
 * <p>
 * Bean属性映射器，两个不同的Bean且不同的属性名称之间的转换时需要明确属性来源
 * </p>
 *
 * @author liuliangxing
 * @since 2019-06-22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyMapper {
    /**
     * 数据来源（全路径字符串）
     *
     * @return java.lang.String
     */
    String sourceReference() default "";

    /**
     * 数据来源（类名.class）,一般应用于VO、DTO转PO（直接引用类避免拼写错误问题）
     *
     * @return Class
     */
    Class<? extends Serializable> sourceClass() default Serializable.class;

    /**
     * 属性
     *
     * @return java.lang.String
     */
    String property();
}