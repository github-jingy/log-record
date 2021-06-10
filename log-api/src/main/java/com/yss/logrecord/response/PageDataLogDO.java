package com.yss.logrecord.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 业务日志分页查询结果
 * @author wangshi.
 * @date 2021/1/1
 */
@Data
@AllArgsConstructor
public class PageDataLogDO<T> {
    long total;
    List<T> rows;
}