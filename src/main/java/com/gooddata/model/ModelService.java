/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.model;

import com.gooddata.AbstractService;
import com.gooddata.FutureResult;
import com.gooddata.GoodDataRestException;
import com.gooddata.PollHandlerBase;
import com.gooddata.SimplePollHandler;
import com.gooddata.gdc.AsyncTask;
import com.gooddata.project.Project;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import static com.gooddata.Validate.noNullElements;
import static com.gooddata.Validate.notNull;
import static com.gooddata.model.ModelDiff.UpdateScript;
import static java.util.Arrays.asList;

/**
 * Service for manipulating with project model
 */
public class ModelService extends AbstractService {

    public ModelService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    private FutureResult<ModelDiff> getProjectModelDiff(Project project, DiffRequest diffRequest) {
        notNull(project, "project");
        notNull(diffRequest, "diffRequest");
        try {
            final AsyncTask asyncTask = restTemplate
                    .postForObject(DiffRequest.URI, diffRequest, AsyncTask.class, project.getId());
            return new FutureResult<>(this, new SimplePollHandler<ModelDiff>(asyncTask.getUri(), ModelDiff.class));
        } catch (GoodDataRestException | RestClientException e) {
            throw new ModelException("Unable to get project model diff", e);
        }
    }

    public FutureResult<ModelDiff> getProjectModelDiff(Project project, String targetModel) {
        notNull(project, "project");
        notNull(targetModel, "targetModel");
        return getProjectModelDiff(project, new DiffRequest(targetModel));
    }

    public FutureResult<ModelDiff> getProjectModelDiff(Project project, Reader targetModel) {
        notNull(project, "project");
        notNull(targetModel, "targetModel");
        try {
            return getProjectModelDiff(project, FileCopyUtils.copyToString(targetModel));
        } catch (IOException e) {
            throw new ModelException("Can't read target model", e);
        }
    }

    /**
     * Update project model with the MAQL script from given ModelDiff with the least side-effects
     * (see {@link ModelDiff#getUpdateMaql()}).
     *
     * @param project   project to be updated
     * @param modelDiff difference of model to be applied into the project
     * @return collection of results (task statuses) of execution of all MAQL script chunks
     */
    public FutureResult<Void> updateProjectModel(Project project, ModelDiff modelDiff) {
        notNull(modelDiff, "modelDiff");
        return updateProjectModel(project, modelDiff.getUpdateMaql());
    }

    /**
     * Update project model with the given update script (MAQL).
     *
     * @param project      project to be updated
     * @param updateScript update script to be executed in the project
     * @return collection of results (task statuses) of execution of all MAQL script chunks
     */
    public FutureResult<Void> updateProjectModel(Project project, UpdateScript updateScript) {
        notNull(updateScript, "updateScript");
        return updateProjectModel(project, updateScript.getMaqlChunks());
    }

    private FutureResult<Void> updateProjectModel(final Project project, final String... maqlDdl) {
        return updateProjectModel(project, asList(maqlDdl));
    }

    public FutureResult<Void> updateProjectModel(final Project project, final List<String> maqlDdl) {
        notNull(project, "project");
        noNullElements(maqlDdl, "maqlDdl");
        if (maqlDdl.isEmpty()) {
            throw new IllegalArgumentException("MAQL DDL string(s) should be given");
        }
        return new FutureResult<>(this, new PollHandlerBase<MaqlDdlLinks, Void>(MaqlDdlLinks.class, Void.class) {

            private final String projectId = project.getId();
            private final LinkedList<String> maqlChunks = new LinkedList<>(maqlDdl);
            private String pollUri;

            {
                executeNextMaqlChunk();
            }

            @Override
            public String getPollingUri() {
                return pollUri;
            }

            private void executeNextMaqlChunk() {
                if (maqlChunks.isEmpty()) {
                    return;
                }
                try {
                    final MaqlDdlLinks links = restTemplate.postForObject(MaqlDdl.URI, new MaqlDdl(maqlChunks.poll()),
                        MaqlDdlLinks.class, projectId);
                    this.pollUri = links.getStatusLink();
                } catch (GoodDataRestException | RestClientException e) {
                    throw new ModelException("Unable to update project model", e);
                }
            }

            @Override
            public boolean isFinished(final ClientHttpResponse response) throws IOException {
                final boolean finished = super.isFinished(response);
                if (finished) {
                    final MaqlDdlTaskStatus maqlDdlTaskStatus = extractData(response, MaqlDdlTaskStatus.class);
                    if (!maqlDdlTaskStatus.isSuccess()) {
                        throw new ModelException("Unable to update project model: " + maqlDdlTaskStatus.getMessages());
                    }
                    executeNextMaqlChunk();
                }
                return finished;
            }

            @Override
            public void handlePollResult(MaqlDdlLinks pollResult) {
            }
        });
    }

}
