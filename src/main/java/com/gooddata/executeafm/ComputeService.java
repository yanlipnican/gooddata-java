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
import com.gooddata.executeafm.result.ComputationResult;
import com.gooddata.gdc.UriResponse;
import com.gooddata.project.Project;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.gooddata.util.Validate.notNull;

/**
 * Allows to start computation and handle it's result.
 */
public class ComputeService extends AbstractService {

    private static final String URI = "/gdc/app/projects/{projectId}/compute";

    /**
     * Sets RESTful HTTP Spring template. Should be called from constructor of concrete service extending
     * this abstract one.
     *
     * @param restTemplate RESTful HTTP Spring template
     * @param settings settings
     */
    public ComputeService(final RestTemplate restTemplate, final GoodDataSettings settings) {
        super(restTemplate, settings);
    }

    /**
     * Compute the given computation for given project.
     * @param project project
     * @param computation computation
     * @return future computation result
     */
    public FutureResult<ComputationResult> compute(final Project project, final Computation computation) {
        notNull(notNull(project, "project").getId(), "project.id");
        notNull(computation, "computation");

        final UriResponse uri;
        try {
            uri = restTemplate.postForObject(URI, computation, UriResponse.class, project.getId());
        } catch (GoodDataException | RestClientException e) {
            throw new ComputeException("Cannot post afm computation", e);
        }

        return new PollResult<>(this, new AbstractPollHandler<UriResponse, ComputationResult>(uri.getUri(), UriResponse.class, ComputationResult.class) {
            @Override
            public void handlePollResult(final UriResponse pollResult) {
                try {
                    setResult(restTemplate.getForObject(getPolling(), ComputationResult.class));
                } catch (GoodDataException | RestClientException e) {
                    throw new ComputeException("Can't get computation result from uri: " + pollResult.getUri(), e);
                }
            }

            @Override
            public void handlePollException(final GoodDataRestException e) {
                throw new ComputeException("Can't compute computation", e);
            }
        });
    }
}
