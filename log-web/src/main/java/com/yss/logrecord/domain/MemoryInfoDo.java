package com.yss.logrecord.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoryInfoDo {

    /**
     * 总内存
     */
    private String total;

    /**
     * 可用内存
     */
    private String available;
}
