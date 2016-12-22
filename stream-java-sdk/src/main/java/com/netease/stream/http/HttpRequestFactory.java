package com.netease.stream.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;

import com.netease.stream.exception.ClientException;

/** Responsible for creating Apache HttpClient 4 request objects. */
public class HttpRequestFactory {

    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Creates an HttpClient method object based on the specified request and populates any
     * parameters, headers, etc. from the original request.
     * 
     * @param request The request to convert to an HttpClient method object.
     * @return The converted HttpClient method object with any parameters, headers, etc. from the
     *         original request set.
     */
    public HttpRequestBase createHttpRequest(Request request) {
        URI endpoint = request.getEndpoint();
        String uri = endpoint.toString();
        if (request.getResourcePath() != null && request.getResourcePath().length() > 0) {
            if (request.getResourcePath().startsWith("/") == false) {
                uri += "/";
            }
            uri += request.getResourcePath();
        } else if (!uri.endsWith("/")) {
            uri += "/";
        }

        HttpRequestBase httpRequest;
        if (request.getHttpMethod() == HttpMethod.POST) {
            HttpPost postMethod = new HttpPost(uri);

            /*
             * If there isn't any payload content to include in this request, then try to include
             * the POST parameters in the query body, otherwise, just use the query string. For all
             * Query services, the best behavior is putting the params in the request body for POST
             * requests, but we can't do that for S3.
             */
            if (request.getContent() != null) {
                postMethod.setEntity(newStringEntity(request.getContent()));
            }
            httpRequest = postMethod;
        } else if (request.getHttpMethod() == HttpMethod.PUT) {
            HttpPut putMethod = new HttpPut(uri);
            httpRequest = putMethod;

            /*
             * Enable 100-continue support for PUT operations, since this is where we're potentially
             * uploading large amounts of data and want to find out as early as possible if an
             * operation will fail. We don't want to do this for all operations since it will cause
             * extra latency in the network interaction.
             */

            if (request.getContent() != null) {
                HttpEntity entity = null;
                if (request.getHeaders().get("Content-Length") == null) {
                    entity = newBufferedHttpEntity(entity);
                }
                putMethod.setEntity(entity);
            }
        } else if (request.getHttpMethod() == HttpMethod.GET) {
            httpRequest = new HttpGet(uri);
        } else if (request.getHttpMethod() == HttpMethod.DELETE) {
            httpRequest = new HttpDelete(uri);
        } else if (request.getHttpMethod() == HttpMethod.HEAD) {
            httpRequest = new HttpHead(uri);
        } else {
            throw new ClientException("Unknown HTTP method name: " + request.getHttpMethod());
        }

        configureHeaders(httpRequest, request);
        return httpRequest;
    }

    /**
     * Configures the headers in the specified Apache HTTP request.
     * 
     * @param httpRequest The specified Apache HTTP request.
     * @param request The request to convert to an HttpClient method object.
     */
    private void configureHeaders(HttpRequestBase httpRequest, Request request) {

        // Copy over any other headers already in our request
        for (Entry<String, String> entry : request.getHeaders().entrySet()) {
            /*
             * HttpClient4 fills in the Content-Length header and complains if it's already present,
             * so we skip it here. We also skip the Host header to avoid sending it twice, which
             * will interfere with some signing schemes.
             */
            if (entry.getKey().equalsIgnoreCase("Content-Length")) {
                continue;
            }

            httpRequest.addHeader(entry.getKey(), entry.getValue());
        }

        /* Set content type and encoding */
        if (httpRequest.getHeaders("Content-Type") == null
                || httpRequest.getHeaders("Content-Type").length == 0) {
            httpRequest.addHeader("Content-Type", "application/json; " + "charset="
                    + DEFAULT_ENCODING.toLowerCase());
        }
    }

    /**
     * Utility function for creating a new StringEntity and wrapping any errors as an
     * ClientException.
     * 
     * @param s The string contents of the returned HTTP entity.
     * @return A new StringEntity with the specified contents.
     */
    private HttpEntity newStringEntity(String s) {
        try {
            return new StringEntity(s);
        } catch (UnsupportedEncodingException e) {
            throw new ClientException("Unable to create HTTP entity: " + e.getMessage(), e);
        }
    }

    /**
     * Utility function for creating a new BufferedEntity and wrapping any errors as an
     * ClientException.
     * 
     * @param entity The HTTP entity to wrap with a buffered HTTP entity.
     * @return A new BufferedHttpEntity wrapping the specified entity.
     */
    private HttpEntity newBufferedHttpEntity(HttpEntity entity) {
        try {
            return new BufferedHttpEntity(entity);
        } catch (IOException e) {
            throw new ClientException("Unable to create HTTP entity: " + e.getMessage(), e);
        }
    }
}
