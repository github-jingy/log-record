package com.yss.logrecord.tool.persistence;

import com.yss.logrecord.request.LogEntityRequest;

public interface PersistenceLog {

    void storeLog(LogEntityRequest entiry);
}
