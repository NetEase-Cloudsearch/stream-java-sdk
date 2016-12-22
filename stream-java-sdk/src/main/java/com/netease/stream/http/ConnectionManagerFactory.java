package com.netease.stream.http;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import com.netease.stream.config.ClientConfiguration;

/** Responsible for creating and configuring instances of Apache HttpClient4's Connection Manager. */
class ConnectionManagerFactory {

    /**
     * Create threadsafe client connect manager.
     * 
     * @param config Used to connect to server.
     * @param httpClientParams.
     * @return connection manager.
     */
    public static ThreadSafeClientConnManager createThreadSafeClientConnManager(
            ClientConfiguration config, HttpParams httpClientParams) {
        ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager();
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnections());
        connectionManager.setMaxTotal(config.getMaxConnections());

        IdleConnectionReaper.registerConnectionManager(connectionManager);
        return connectionManager;
    }
}
