/*
 * Copyright (C) 2007-2015, GoodData(R) Corporation. All rights reserved.
 */

package com.gooddata;

import com.gooddata.commons.http.FakeCloseableHttpClient;
import com.gooddata.http.client.GoodDataHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * TODO
 */
public class GoodDataHttpClientBuilderClientWrapper extends HttpClientBuilder {

    private final HttpClient httpClient;

    public GoodDataHttpClientBuilderClientWrapper(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    @Override
    public CloseableHttpClient build() {
        return new FakeCloseableHttpClient(httpClient);
    }
}
