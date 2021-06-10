package com.yss.logrecord.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrowserDo {

    /**
     * 浏览器类型
     */
    private String code;

    /**
     * 浏览器数量
     */
    private long num;
}
