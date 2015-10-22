/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.seed.security.api.exceptions;

/**
 * Base class for exceptions concerning authorization failure
 * 
 * @author yves.dautremay@mpsa.com
 * 
 */
public class AuthorizationException extends RuntimeException {

    /** UID */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new AuthorizationException.
     */
    public AuthorizationException() {
        super();
    }

    /**
     * Constructs a new AuthorizationException.
     * 
     * @param message
     *            the reason for the exception
     */
    public AuthorizationException(String message) {
        super(message);
    }

    /**
     * Constructs a new AuthorizationException.
     * 
     * @param cause
     *            the underlying Throwable that caused this exception to be
     *            thrown.
     */
    public AuthorizationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new AuthorizationException.
     * 
     * @param message
     *            the reason for the exception
     * @param cause
     *            the underlying Throwable that caused this exception to be
     *            thrown.
     */
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}