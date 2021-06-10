package com.yss.logrecord.core.gateways.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 多个Bean属性映射器，一般应用于多个DTO对应一个PO
 * </p>
 *
 * @author liuliangxing
 * @since 2019-06-22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyMappers {
    /**
     * Bean属性映射器
     *
     * @return
     */
    PropertyMapper[] value();
}
