package com.yss.logrecord.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordDo {

    /**
     * 时间
     */
    private String time;

    /**
     * 登录个数
     */
    private String count;
}
