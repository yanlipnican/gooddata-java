/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.generic;

import com.gooddata.AbstractService;
import com.gooddata.GoodDataException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Generic HTTP service
 */
public class GenericService extends AbstractService {

    public GenericService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public <T> T get(String uri, Class<T> cls, Object... uriVariables) {
        try {
            return restTemplate.getForObject(uri, cls,  uriVariables);
        } catch (GoodDataException | RestClientException e) {
            throw new GoodDataException("Unable to GET uri " + uri, e);
        }
    }

    public <T> T post(String uri, Object request, Class<T> cls, Object... uriVariables) {
        try {
            return restTemplate.postForObject(uri, request, cls, uriVariables);
        } catch (GoodDataException | RestClientException e) {
            throw new GoodDataException("Unable to POST uri " + uri, e);
        }
    }

    public void put(String uri, Object request, Object... uriVariables) {
        try {
            restTemplate.put(uri, request, uriVariables);
        } catch (GoodDataException | RestClientException e) {
            throw new GoodDataException("Unable to PUT uri " + uri, e);
        }
    }

    public void delete(String uri, Object... uriVariables) {
        try {
            restTemplate.delete(uri, uriVariables);
        } catch (GoodDataException | RestClientException e) {
            throw new GoodDataException("Unable to DELETE uri " + uri, e);
        }
    }

}
