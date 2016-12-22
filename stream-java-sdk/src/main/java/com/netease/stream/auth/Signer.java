package com.netease.stream.auth;

import com.netease.stream.exception.ClientException;
import com.netease.stream.http.Request;

public interface Signer {
    public void sign(Request request, Credentials credentials) throws ClientException;
}
