/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata;

import com.gooddata.account.AccountService;
import com.gooddata.connector.ConnectorService;
import com.gooddata.dataload.processes.ProcessService;
import com.gooddata.util.ResponseErrorHandler;
import com.gooddata.warehouse.WarehouseService;
import com.gooddata.dataset.DatasetService;
import com.gooddata.gdc.DataStoreService;
import com.gooddata.gdc.GdcService;
import com.gooddata.http.client.GoodDataHttpClient;
import com.gooddata.http.client.LoginSSTRetrievalStrategy;
import com.gooddata.http.client.SSTRetrievalStrategy;
import com.gooddata.md.MetadataService;
import com.gooddata.model.ModelService;
import com.gooddata.project.ProjectService;
import com.gooddata.report.ReportService;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static com.gooddata.util.Validate.notEmpty;
import static com.gooddata.util.Validate.notNull;
import static java.util.Collections.singletonMap;
import static org.apache.http.util.VersionInfo.loadVersionInfo;

/**
 * Entry point for GoodData SDK usage.
 * <p>
 * Configure connection to GoodData using one of constructors. One can then get initialized service he needs from
 * the newly constructed instance. This instance can be also used later for logout from GoodData Platform.
 * <p>
 * Usage example:
 * <pre><code>
 *     GoodData gd = new GoodData("roman@gooddata.com", "Roman1");
 *     // do something useful like: gd.getSomeService().doSomething()
 *     gd.logout();
 * </code></pre>
 */
public class GoodData {

    public static final String GDC_REQUEST_ID_HEADER = "X-GDC-REQUEST";

    protected static final String PROTOCOL = "https";
    protected static final int PORT = 443;
    protected static final String HOSTNAME = "secure.gooddata.com";

    private static final int RESTAPI_VERSION = 1;

    private final RestTemplate restTemplate;
    private final AccountService accountService;
    private final ProjectService projectService;
    private final MetadataService metadataService;
    private final ModelService modelService;
    private final GdcService gdcService;
    private final DataStoreService dataStoreService;
    private final DatasetService datasetService;
    private final ReportService reportService;
    private final ConnectorService connectorService;
    private final ProcessService processService;
    private final WarehouseService warehouseService;

    /**
     * Create instance configured to communicate with GoodData Platform under user with given credentials.
     *
     * @param login    GoodData user's login
     * @param password GoodData user's password
     */
    public GoodData(String login, String password) {
        this(HOSTNAME, login, password, new GoodDataSettings());
    }

    /**
     * Create instance configured to communicate with GoodData Platform under user with given credentials.
     *
     * @param login    GoodData user's login
     * @param password GoodData user's password
     * @param settings additional settings
     */
    public GoodData(String login, String password, GoodDataSettings settings) {
        this(HOSTNAME, login, password, settings);
    }

    /**
     * Create instance configured to communicate with GoodData Platform running on given host using given user's
     * credentials.
     *
     * @param hostname GoodData Platform's host name (e.g. secure.gooddata.com)
     * @param login    GoodData user's login
     * @param password GoodData user's password
     */
    public GoodData(String hostname, String login, String password) {
        this(hostname, login, password, PORT, PROTOCOL, new GoodDataSettings());
    }



    /**
     * Create instance configured to communicate with GoodData Platform running on given host using given user's
     * credentials.
     *
     * @param hostname GoodData Platform's host name (e.g. secure.gooddata.com)
     * @param login    GoodData user's login
     * @param password GoodData user's password
     * @param settings additional settings
     */
    public GoodData(String hostname, String login, String password, GoodDataSettings settings) {
        this(hostname, login, password, PORT, PROTOCOL, settings);
    }

    /**
     * Create instance configured to communicate with GoodData Platform running on given host and port using given user's
     * credentials.
     *
     * @param hostname GoodData Platform's host name (e.g. secure.gooddata.com)
     * @param login    GoodData user's login
     * @param password GoodData user's password
     * @param port     GoodData Platform's API port (e.g. 443)
     */
    public GoodData(String hostname, String login, String password, int port) {
        this(hostname, login, password, port, PROTOCOL, new GoodDataSettings());
    }

    /**
     * Create instance configured to communicate with GoodData Platform running on given host and port using given user's
     * credentials.
     *
     * @param hostname GoodData Platform's host name (e.g. secure.gooddata.com)
     * @param login    GoodData user's login
     * @param password GoodData user's password
     * @param port     GoodData Platform's API port (e.g. 443)
     * @param settings additional settings
     */
    public GoodData(String hostname, String login, String password, int port, GoodDataSettings settings) {
        this(hostname, login, password, port, PROTOCOL, settings);
    }


    /**
     * Create instance configured to communicate with GoodData Platform running on given host, port and protocol using
     * given user's credentials.
     *
     * @param hostname GoodData Platform's host name (e.g. secure.gooddata.com)
     * @param login    GoodData user's login
     * @param password GoodData user's password
     * @param port     GoodData Platform's API port (e.g. 443)
     * @param protocol GoodData Platform's API protocol (e.g. https)
     * @param settings additional settings
     */
    protected GoodData(String hostname, String login, String password, int port, String protocol, GoodDataSettings settings) {
        notEmpty(hostname, "hostname");
        notEmpty(login, "login");
        notEmpty(password, "password");
        notEmpty(protocol, "protocol");

        final HttpClient httpClient = createHttpClient(login, password, hostname, port, protocol,
                GoodDataFactory.createHttpClientBuilder(settings));

        final ClientHttpRequestFactory factory = new UriPrefixingClientHttpRequestFactory(
                new HttpComponentsClientHttpRequestFactory(httpClient), hostname, port, protocol);
        restTemplate = createRestTemplate(factory);

        accountService = new AccountService(getRestTemplate());
        projectService = new ProjectService(getRestTemplate(), accountService);
        metadataService = new MetadataService(getRestTemplate());
        modelService = new ModelService(getRestTemplate());
        gdcService = new GdcService(getRestTemplate());
        dataStoreService = new DataStoreService(httpClient, gdcService, new HttpHost(hostname, port, protocol).toURI());
        datasetService = new DatasetService(getRestTemplate(), dataStoreService);
        processService = new ProcessService(getRestTemplate(), accountService, dataStoreService);
        reportService = new ReportService(getRestTemplate());
        warehouseService = new WarehouseService(getRestTemplate());
        connectorService = new ConnectorService(getRestTemplate(), projectService);
    }

    protected GoodData(HttpClient httpClient, String hostname, int port, String protocol) {
        notNull(httpClient, "httpClient");
        notEmpty(hostname, "hostname");
        notEmpty(protocol, "protocol");

        final ClientHttpRequestFactory factory = new UriPrefixingClientHttpRequestFactory(
                new HttpComponentsClientHttpRequestFactory(httpClient), hostname, port, protocol);
        restTemplate = createRestTemplate(factory);

        accountService = new AccountService(getRestTemplate());
        projectService = new ProjectService(getRestTemplate(), accountService);
        metadataService = new MetadataService(getRestTemplate());
        modelService = new ModelService(getRestTemplate());
        gdcService = new GdcService(getRestTemplate());
        dataStoreService = new DataStoreService(httpClient, gdcService, new HttpHost(hostname, port, protocol).toURI());
        datasetService = new DatasetService(getRestTemplate(), dataStoreService);
        processService = new ProcessService(getRestTemplate(), accountService, dataStoreService);
        reportService = new ReportService(getRestTemplate());
        warehouseService = new WarehouseService(getRestTemplate());
        connectorService = new ConnectorService(getRestTemplate(), projectService);
    }

    private RestTemplate createRestTemplate(ClientHttpRequestFactory factory) {
        final RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(
                new HeaderSettingRequestInterceptor(singletonMap("Accept", getAcceptHeaderValue()))));

        restTemplate.setErrorHandler(new ResponseErrorHandler(restTemplate.getMessageConverters()));

        return restTemplate;
    }

    /*
     * Set accept header (application/json by default) and append rest api versioning information which is mandatory
     * for some resources.
     */
    private String getAcceptHeaderValue(){
        return MediaType.APPLICATION_JSON_VALUE + ";version=" + RESTAPI_VERSION;
    }

    protected HttpClient createHttpClient(final String login, final String password, final String hostname,
                                          final int port, final String protocol, final HttpClientBuilder builder) {
        final HttpHost host = new HttpHost(hostname, port, protocol);
        final HttpClient httpClient = builder.build();
        final SSTRetrievalStrategy strategy = new LoginSSTRetrievalStrategy(httpClient, host, login, password);
        return new GoodDataHttpClient(httpClient, strategy);
    }

    /**
     * Get the configured {@link RestTemplate} used by the library.
     * This is the extension point for inheriting classes providing additional services.
     * @return REST template
     */
    protected final RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     * Logout from GoodData Platform
     */
    public void logout() {
        getAccountService().logout();
    }

    /**
     * Get initialized service for project management (to list projects, create a project, ...)
     *
     * @return initialized service for project management
     */
    public ProjectService getProjectService() {
        return projectService;
    }

    /**
     * Get initialized service for account management (to get current account information, logout, ...)
     *
     * @return initialized service for account management
     */
    public AccountService getAccountService() {
        return accountService;
    }

    /**
     * Get initialized service for metadata management (to query, create and update project metadata like attributes,
     * facts, metrics, reports, ...)
     *
     * @return initialized service for metadata management
     */
    public MetadataService getMetadataService() {
        return metadataService;
    }

    /**
     * Get initialized service for model management (to get model diff, update model, ...)
     *
     * @return initialized service for model management
     */
    public ModelService getModelService() {
        return modelService;
    }

    /**
     * Get initialized service for API root management (to get API root links, ...)
     *
     * @return initialized service for API root management
     */
    public GdcService getGdcService() {
        return gdcService;
    }

    /**
     * Get initialized service for data store (user staging/WebDAV) management (to upload, download, delete, ...)
     *
     * @return initialized service for data store management
     */
    public DataStoreService getDataStoreService() {
        return dataStoreService;
    }

    /**
     * Get initialized service for dataset management (to list manifest, get datasets, load dataset, ...)
     *
     * @return initialized service for dataset management
     */
    public DatasetService getDatasetService() {
        return datasetService;
    }

    /**
     * Get initialized service for report management (to execute and export report, ...)
     *
     * @return initialized service for report management
     */
    public ReportService getReportService() {
        return reportService;
    }

    /**
     * Get initialized service for dataload processes management and process executions.
     *
     * @return initialized service for dataload processes management and process executions
     */
    public ProcessService getProcessService() {
        return processService;
    }

    /**
     * Get initialized service for ADS management (create, access and delete ads instances).
     *
     * @return initialized service for ADS management
     */
    public WarehouseService getWarehouseService() {
        return warehouseService;
    }

    /**
     * Get initialized service for connector integration management (create, update, start process, ...).
     *
     * @return initialized service for connector integration management
     */
    public ConnectorService getConnectorService() {
        return connectorService;
    }
}
