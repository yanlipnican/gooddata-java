/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm;

import com.gooddata.AbstractPollHandler;
import com.gooddata.AbstractService;
import com.gooddata.FutureResult;
import com.gooddata.GoodDataException;
import com.gooddata.GoodDataRestException;
import com.gooddata.GoodDataSettings;
import com.gooddata.PollResult;
import com.gooddata.executeafm.result.ExecutionResult;
import com.gooddata.gdc.UriResponse;
import com.gooddata.project.Project;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.gooddata.util.Validate.notNull;

/**
 * Allows to start computation and handle it's result.
 */
public class ExecuteAfmService extends AbstractService {

    private static final String URI = "/gdc/app/projects/{projectId}/executeAfm";

    /**
     * Sets RESTful HTTP Spring template. Should be called from constructor of concrete service extending
     * this abstract one.
     *
     * @param restTemplate RESTful HTTP Spring template
     * @param settings settings
     */
    public ExecuteAfmService(final RestTemplate restTemplate, final GoodDataSettings settings) {
        super(restTemplate, settings);
    }

    /**
     * Executes the given execution for given project.
     * @param project project
     * @param execution execution
     * @return future execution result
     */
    public FutureResult<ExecutionResult> executeAfm(final Project project, final Execution execution) {
        notNull(notNull(project, "project").getId(), "project.id");
        notNull(execution, "execution");

        final UriResponse uri;
        try {
            uri = restTemplate.postForObject(URI, execution, UriResponse.class, project.getId());
        } catch (GoodDataException | RestClientException e) {
            throw new ExecuteAfmException("Cannot post afm execution", e);
        }

        return new PollResult<>(this, new AbstractPollHandler<UriResponse, ExecutionResult>(uri.getUri(), UriResponse.class, ExecutionResult.class) {
            @Override
            public void handlePollResult(final UriResponse pollResult) {
                try {
                    setResult(restTemplate.getForObject(getPolling(), ExecutionResult.class));
                } catch (GoodDataException | RestClientException e) {
                    throw new ExecuteAfmException("Can't get execution result from uri: " + pollResult.getUri(), e);
                }
            }

            @Override
            public void handlePollException(final GoodDataRestException e) {
                throw new ExecuteAfmException("Can't executeAfm execution", e);
            }
        });
    }
}
