/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm;

import com.gooddata.AbstractGoodDataIT;
import com.gooddata.executeafm.result.ComputationResult;
import com.gooddata.gdc.UriResponse;
import com.gooddata.project.Project;
import com.gooddata.executeafm.afm.ObjectAfm;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.gooddata.util.ResourceUtils.OBJECT_MAPPER;
import static com.gooddata.util.ResourceUtils.readFromResource;
import static com.gooddata.util.ResourceUtils.readObjectFromResource;
import static net.jadler.Jadler.onRequest;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExecuteAfmServiceIT extends AbstractGoodDataIT {

    private static final String COMPUTE_URI = "/gdc/app/projects/PROJECT_ID/executeAfm";
    private static final String POLLING_URI = COMPUTE_URI + "/computationResultId";
    private static final Execution TRANSFORMATION = new Execution(new ObjectAfm());
    private byte[] pollingBody;

    private Project project;
    private ExecuteAfmService service;

    @BeforeMethod
    public void setUp() throws Exception {
        project = readObjectFromResource("/project/project.json", Project.class);
        service = gd.getExecuteAfmService();
        pollingBody = OBJECT_MAPPER.writeValueAsBytes(new UriResponse(POLLING_URI));
    }

    @Test
    public void shouldExecute() throws Exception {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo(COMPUTE_URI)
            .respond()
                .withStatus(201)
                .withBody(pollingBody);

        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo(POLLING_URI)
            .respond()
                .withStatus(200)
                .withBody(pollingBody)
            .thenRespond()
                .withStatus(200)
                .withBody(readFromResource("/executeafm/result/computationResult.json"));

        final ComputationResult response = service.executeAfm(project, TRANSFORMATION).get();
        assertThat(response, notNullValue());
    }

    @Test(expectedExceptions = ExecuteAfmException.class)
    public void shouldThrowOnBadRequest() throws Exception {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo(COMPUTE_URI)
            .respond()
                .withStatus(400);

        service.executeAfm(project, TRANSFORMATION).get();
    }

    @Test(expectedExceptions = ExecuteAfmException.class)
    public void shouldThrowOnFailedPolling() throws Exception {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo(COMPUTE_URI)
            .respond()
                .withStatus(201)
                .withBody(pollingBody);

        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo(POLLING_URI)
            .respond()
                .withStatus(500);

        service.executeAfm(project, TRANSFORMATION).get();
    }

    @Test(expectedExceptions = ExecuteAfmException.class)
    public void shouldThrowOnFailedResult() throws Exception {
        onRequest()
                .havingMethodEqualTo("POST")
                .havingPathEqualTo(COMPUTE_URI)
                .respond()
                .withStatus(201)
                .withBody(pollingBody);

        onRequest()
                .havingMethodEqualTo("GET")
                .havingPathEqualTo(POLLING_URI)
            .respond()
                .withStatus(202)
                .withBody(pollingBody)
            .thenRespond()
                .withStatus(200)
                .withBody(pollingBody)
            .thenRespond()
                .withStatus(500);

        service.executeAfm(project, TRANSFORMATION).get();
    }
}