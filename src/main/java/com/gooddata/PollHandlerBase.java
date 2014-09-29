package com.gooddata;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static com.gooddata.Validate.notNull;

/**
 * @author martin
 */
public abstract class PollHandlerBase<P, R> implements PollHandler<P, R> {
    protected final Class<P> pollClass;
    protected final Class<R> resultClass;
    private boolean done = false;
    private R result;

    protected PollHandlerBase(Class<P> pollClass, Class<R> resultClass) {
        this.pollClass = notNull(pollClass, "pollClass");
        this.resultClass = notNull(resultClass, "resultClass");
    }

    @Override
    public final Class<R> getResultClass() {
        return resultClass;
    }

    @Override
    public final Class<P> getPollClass() {
        return pollClass;
    }

    protected PollHandler<P, R> setResult(R result) {
        this.result = result;
        this.done = true;
        onFinish();
        return this;
    }

    @Override
    public final boolean isDone() {
        return done;
    }

    /**
     * Return result of polling
     *
     * @return result
     */
    @Override
    public final R getResult() {
        return result;
    }

    /**
     * Check if polling should finish
     *
     * @param response client side http response
     * @return true if polling should finish
     * @throws IOException
     */
    @Override
    public boolean isFinished(final ClientHttpResponse response) throws IOException {
        return HttpStatus.OK.equals(response.getStatusCode());
    }

    /**
     * Method called after polling is successfully finished (default no-op)
     */
    protected void onFinish() {
    }
}
