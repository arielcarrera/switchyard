/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.security.credential;

import java.util.Arrays;

/**
 * BearerTokenCredential
 *
 * @author Ariel Carrera
 */
public class BearerTokenCredential implements Credential {

	private static final long serialVersionUID = 1201269682010107218L;

	private static final String FORMAT = BearerTokenCredential.class.getSimpleName() + "@%s[token=%s]";

    private final char[] _token;

    /**
     * Constructs a BearerTokenCredential with the specified token.
     * @param token the specified bearer token
     */
    public BearerTokenCredential(String token) {
        _token = token != null ? token.toCharArray() : null;
    }

    /**
     * Constructs a BearerTokenCredential with the specified token.
     * @param token the specified bearer token
     */
    public BearerTokenCredential(char[] token) {
        _token = token;
    }

    /**
     * Gets the bearer token.
     * @return the token
     */
    public char[] getToken() {
        return _token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(FORMAT, System.identityHashCode(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(_token);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BearerTokenCredential other = (BearerTokenCredential)obj;
        if (!Arrays.equals(_token, other._token)) {
            return false;
        }
        return true;
    }

}
