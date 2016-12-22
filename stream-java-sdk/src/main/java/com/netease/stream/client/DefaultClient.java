package com.netease.stream.client;

import java.net.URI;
import java.net.URISyntaxException;

import com.netease.stream.config.ClientConfiguration;

/**
 * Abstract base class for Web Service Java clients. Responsible for basic client capabilities that
 * are the same across all SDK Java clients (ex: setting the client endpoint).
 */
public abstract class DefaultClient {

    /** The service endpoint to which this client will send requests. */
    protected URI endpoint;

    /** The client configuration */
    protected ClientConfiguration clientConfiguration;

    /** Low level client for sending requests to services. */
    protected NeteaseHttpClient client;


    /**
     * Constructs a new WebServiceClient object using the specified configuration.
     * 
     * @param clientConfiguration The client configuration for this client.
     */
    public DefaultClient(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        client = new NeteaseHttpClient(clientConfiguration);
    }

    /**
     * Overrides the default endpoint for this client. Callers can use this method to control which
     * region they want to work with.
     * <p>
     * <b>This method is not threadsafe. Endpoints should be configured when the client is created
     * and before any service requests are made. Changing it afterwards creates inevitable race
     * conditions for any service requests in transit.</b>
     * 
     * @param endpoint The endpoint or a full URL, including the protocol.
     * @throws IllegalArgumentException If any problems are detected with the specified endpoint.
     */
    public void setEndpoint(String endpoint) throws IllegalArgumentException {
        /*
         * If the endpoint doesn't explicitly specify a protocol to use, then we'll defer to the
         * default protocol specified in the client configuration.
         */
        if (endpoint.contains("://") == false) {
            endpoint = clientConfiguration.getProtocol().toString() + "://" + endpoint;
        }

        try {
            this.endpoint = new URI(endpoint);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Set configuration
     * 
     * @param clientConfiguration clientConfiguration.
     */
    public void setConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        client = new NeteaseHttpClient(clientConfiguration);
    }

    /**
     * Shuts down this client object, releasing any resources that might be held open. This is an
     * optional method, and callers are not expected to call it, but can if they want to explicitly
     * release any open resources. Once a client has been shutdown, it should not be used to make
     * any more requests.
     */
    public void shutdown() {
        client.shutdown();
    }

}
