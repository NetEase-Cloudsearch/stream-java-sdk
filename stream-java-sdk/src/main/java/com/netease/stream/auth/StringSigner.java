package com.netease.stream.auth;

import com.netease.stream.exception.ClientException;

public interface StringSigner {
    public String sign(String stringToSign, Credentials credentials) throws ClientException;
}
