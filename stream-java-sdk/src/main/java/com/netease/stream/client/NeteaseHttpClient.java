package com.netease.stream.client;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.netease.stream.config.ClientConfiguration;
import com.netease.stream.exception.ClientException;
import com.netease.stream.exception.ServiceException;
import com.netease.stream.http.HttpClientFactory;
import com.netease.stream.http.HttpRequestFactory;
import com.netease.stream.http.HttpResponse;
import com.netease.stream.http.IdleConnectionReaper;
import com.netease.stream.http.Request;
import com.netease.stream.util.StringUtils;

public class NeteaseHttpClient {

    /**
     * Logger providing detailed information on requests/responses.
     */
    private static final Log log = LogFactory.getLog(NeteaseHttpClient.class);
    /** Internal client for sending HTTP requests */
    private HttpClient httpClient;
    /** Client configuration options, such as proxy settings, max retries, etc. */
    private final ClientConfiguration config;
    private static HttpRequestFactory httpRequestFactory = new HttpRequestFactory();
    private static HttpClientFactory httpClientFactory = new HttpClientFactory();

    /**
     * Constructs a new client using the specified client configuration options (ex: max retry
     * attempts, proxy settings, etc).
     * 
     * @param clientConfiguration Configuration options specifying how this client will communicate.
     * 
     */
    public NeteaseHttpClient(ClientConfiguration clientConfiguration) {
        this.config = clientConfiguration;
        this.httpClient = httpClientFactory.createHttpClient(config);
    }

    /**
     * Executes the request and returns the result.
     * 
     * @param request The Services request to send to the remote server.
     * @return HttpResponse.
     * 
     */
    public HttpResponse execute(Request request) throws ClientException, ServiceException {

        try {
            return executeHelper(request);
        } catch (ClientException e) {
            throw e;
        }
    }

    /**
     * Internal method to execute the HTTP method given.
     * 
     * @see NeteaseHttpClient#execute(Request)
     */
    private HttpResponse executeHelper(Request request) throws ClientException, ServiceException {

        HttpRequestBase httpRequest = httpRequestFactory.createHttpRequest(request);
        org.apache.http.HttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);
            if (isRequestSuccessful(response)) {
                log.info("execute HTTP request successfully");
            } else {
                log.warn("Unable to execute HTTP request Code:"
                        + response.getStatusLine().getStatusCode() + ", message:"
                        + response.getStatusLine().getReasonPhrase());
            }
            return createResponse(httpRequest, request, response);
        } catch (IOException ioe) {
            log.warn("Unable to execute HTTP request: " + ioe.getMessage(), ioe);
            throw new ClientException("Unable to execute HTTP request: " + ioe.getMessage(), ioe);
        } finally {
            /*
             * Some response handlers need to manually manage the HTTP connection and will take care
             * of releasing the connection on their own, but if this response handler doesn't need
             * the connection left open, we go ahead and release the it to free up resources.
             */
            try {
                response.getEntity().getContent().close();
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Shuts down this HTTP client object, releasing any resources that might be held open. This is
     * an optional method, and callers are not expected to call it, but can if they want to
     * explicitly release any open resources. Once a client has been shutdown, it cannot be used to
     * make more requests.
     */
    public void shutdown() {
        IdleConnectionReaper.removeConnectionManager(httpClient.getConnectionManager());
        httpClient.getConnectionManager().shutdown();
    }

    private boolean isRequestSuccessful(org.apache.http.HttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        return status / 100 == HttpStatus.SC_OK / 100;
    }

    /**
     * Creates and initializes an HttpResponse object suitable to be passed to an HTTP response
     * handler object.
     * 
     * @param method The HTTP method that was invoked to get the response.
     * @param request The HTTP request associated with the response.
     * @return The new, initialized HttpResponse object ready to be passed to an HTTP response
     *         handler object.
     * @throws IOException If there were any problems getting any response information from the
     *         HttpClient method object.
     */
    private HttpResponse createResponse(HttpRequestBase method, Request request,
            org.apache.http.HttpResponse apacheHttpResponse) throws IOException {
        HttpResponse httpResponse = new HttpResponse(request, method);

        if (apacheHttpResponse.getEntity() != null) {
            httpResponse.setContent(new StringUtils().convertStreamToString(apacheHttpResponse
                    .getEntity().getContent()));
        }

        httpResponse.setStatusCode(apacheHttpResponse.getStatusLine().getStatusCode());
        httpResponse.setStatusText(apacheHttpResponse.getStatusLine().getReasonPhrase());
        for (Header header : apacheHttpResponse.getAllHeaders()) {
            httpResponse.addHeader(header.getName(), header.getValue());
        }

        return httpResponse;
    }

}
