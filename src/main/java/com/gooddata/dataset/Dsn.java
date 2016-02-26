/*
 * Copyright (C) 2007-2016, GoodData(R) Corporation. All rights reserved.
 */

package com.gooddata.dataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * TODO
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dsn {
    private final String driver;
    private final String db_name;
    private final String user;
    private final String host;
    private final String port;
    private final String passwd;
    private final String query;

    public Dsn(String driver, String db_name, String user, String host, String port, String passwd, String query) {
        this.driver = driver;
        this.db_name = db_name;
        this.user = user;
        this.host = host;
        this.port = port;
        this.passwd = passwd;
        this.query = query;
    }

    public String getDriver() {
        return driver;
    }

    public String getDb_name() {
        return db_name;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getQuery() {
        return query;
    }
}
