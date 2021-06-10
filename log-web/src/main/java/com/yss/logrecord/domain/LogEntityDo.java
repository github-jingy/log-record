package com.yss.logrecord.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "LOG_RECORD")
public class LogEntityDo {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "CLIENT_IP")
    private String clientIp;
    @Column(name = "CLASS_NAME")
    private String className;
    @Column(name = "CLASS_METHOD")
    private String classMethod;
    @Column(name = "METHOD")
    private String method;
    @Column(name = "URL")
    private String url;
    @Column(name = "REQ_TYPE")
    private String reqType;
    @Column(name = "SWAGGER_API")
    private String swaggerApi;
    @Column(name = "SWAGGER_API_OPERATION")
    private String swaggerApiOperation;
    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    @Lob
    @Column(name = "REQUEST_DATA",columnDefinition = "CLOB")
    private String requestData;
    @Column(name = "RETURN_TIME")
    private LocalDateTime returnTime;
    @Lob
    @Column(name = "RESPONSE_DATA",columnDefinition = "CLOB")
    private String responseData;
    @Column(name = "HTTP_STATUS_CODE")
    private Integer httpStatusCode;
    @Column(name = "TIME_CONSUMING")
    private long timeConsuming;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "USER_CODE")
    private String userCode;
    @Column(name = "FSYS_ID")
    private String fSysId;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "USER_HOST")
    private String userHost;
    @Column(name = "SERVICE_NAME")
    private String serviceName;
    @Column(name = "BROWSER")
    private String browser;

    @Column(name = "REFRESH_TOKEN")
    private String refreshtoken;

    @CreationTimestamp
    @Column(name = "CREATE_TIME",updatable = false)
    private LocalDateTime createtime;

    @Column(name = "INFO")
    private String info;
}
