package com.gooddata;

import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * For internal usage by services employing polling.<p>
 * @param <P> polling type
 * @param <R> result type
 *
 * @see com.gooddata.FutureResult
 */
public interface PollHandler<P,R> {

    String getPollingUri();

    Class<R> getResultClass();

    Class<P> getPollClass();

    boolean isDone();

    R getResult();

    boolean isFinished(ClientHttpResponse response) throws IOException;

    void handlePollResult(P pollResult);
}
