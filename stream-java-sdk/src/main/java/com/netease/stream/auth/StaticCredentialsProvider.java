package com.netease.stream.auth;


/**
 * Simple implementation of CredentialsProvider that just wraps static Credentials.
 */
public class StaticCredentialsProvider implements CredentialsProvider {

    private final Credentials credentials;

    public StaticCredentialsProvider(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void refresh() {}

}
