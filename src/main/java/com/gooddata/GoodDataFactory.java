/*
 * Copyright (C) 2007-2015, GoodData(R) Corporation. All rights reserved.
 */

package com.gooddata;

import static com.gooddata.util.Validate.notEmpty;
import static org.apache.http.util.VersionInfo.loadVersionInfo;

import com.gooddata.http.client.GoodDataHttpClient;
import com.gooddata.http.client.LoginSSTRetrievalStrategy;
import com.gooddata.http.client.SSTRetrievalStrategy;
import com.gooddata.http.client.SimpleSSTRetrievalStrategy;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.VersionInfo;
import org.springframework.http.MediaType;

/**
 * TODO
 */
public class GoodDataFactory {

    protected final HttpClient httpClient;

    // TODO shouldn't be copied from GoodData
    protected static final String PROTOCOL = "https";
    protected static final int PORT = 443;
    protected static final String HOSTNAME = "secure.gooddata.com";

    private static final String UNKNOWN_VERSION = "UNKNOWN";

    protected final String hostname;
    protected final int port;
    protected final String protocol;

    public GoodDataFactory() {
        this(HOSTNAME, PORT, PROTOCOL, new GoodDataSettings());
    }

    public GoodDataFactory(GoodDataSettings settings) {
        this(HOSTNAME, PORT, PROTOCOL, settings);
    }

    public GoodDataFactory(String hostname) {
        this(hostname, PORT, PROTOCOL, new GoodDataSettings());
    }

    public GoodDataFactory(String hostname, GoodDataSettings settings) {
        this(hostname, PORT, PROTOCOL, settings);
    }

    public GoodDataFactory(String hostname, int port) {
        this(hostname, port, PROTOCOL, new GoodDataSettings());
    }

    public GoodDataFactory(String hostname, int port, GoodDataSettings settings) {
        this(hostname, port, PROTOCOL, settings);
    }

    protected GoodDataFactory(String hostname, int port, String protocol, GoodDataSettings settings) {
        notEmpty(hostname, "hostname");
        notEmpty(protocol, "protocol");
        this.hostname = hostname;
        this.port = port;
        this.protocol = protocol;
        httpClient = createHttpClient(settings);
    }

    public GoodData fromSst(String sst) {
        return new GoodData(new GoodDataHttpClient(httpClient, new HttpHost(hostname, port, protocol),
                new SimpleSSTRetrievalStrategy(sst)), hostname, port, protocol);
    }

    public GoodData fromLogin(String login, String password) {
        final HttpHost host = new HttpHost(hostname, port, protocol);
        final LoginSSTRetrievalStrategy loginSSTRetrievalStrategy = new LoginSSTRetrievalStrategy(httpClient, host, login, password);
        return new GoodData(new GoodDataHttpClient(httpClient, loginSSTRetrievalStrategy), hostname, port, protocol);
    }

    protected HttpClient createHttpClient(GoodDataSettings settings){
        return createHttpClientBuilder(settings).build();
    }

    static HttpClientBuilder createHttpClientBuilder(final GoodDataSettings settings) {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(settings.getMaxConnections());
        connectionManager.setMaxTotal(settings.getMaxConnections());

        final RequestConfig.Builder requestConfig = RequestConfig.copy(RequestConfig.DEFAULT);
        requestConfig.setConnectTimeout(settings.getConnectionTimeout());
        requestConfig.setSocketTimeout(settings.getSocketTimeout());

        return HttpClientBuilder.create()
                .setUserAgent(getUserAgent())
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig.build());
    }

    private static String getUserAgent() {
        final Package pkg = Package.getPackage("com.gooddata");
        final String clientVersion = pkg != null && pkg.getImplementationVersion() != null
                ? pkg.getImplementationVersion() : UNKNOWN_VERSION;

        final VersionInfo vi = loadVersionInfo("org.apache.http.client", HttpClientBuilder.class.getClassLoader());
        final String apacheVersion = vi != null ? vi.getRelease() : UNKNOWN_VERSION;

        return String.format("%s/%s (%s; %s) %s/%s", "GoodData-Java-SDK", clientVersion,
                System.getProperty("os.name"), System.getProperty("java.specification.version"),
                "Apache-HttpClient", apacheVersion);
    }
}


