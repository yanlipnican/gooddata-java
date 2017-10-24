/*
 * Copyright (C) 2007-2017, GoodData(R) Corporation. All rights reserved.
 * This source code is licensed under the BSD-style license found in the
 * LICENSE.txt file in the root directory of this source tree.
 */
package com.gooddata.executeafm;

import com.gooddata.GoodDataException;

/**
 * Represents error during computation or handling it's result.
 */
public class ComputeException extends GoodDataException {

    /**
     * Creates new instance of given message and cause
     * @param message message
     * @param cause cause
     */
    public ComputeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
