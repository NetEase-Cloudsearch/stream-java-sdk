package com.netease.stream.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@linkplain com.netease.cloud.http.Request} interface.
 * <p>
 * This class is only intended for internal use inside the client libraries. Callers shouldn't ever
 * interact directly with objects of this class.
 */
public class DefaultRequest implements Request {

    /** The resource path being requested */
    private String resourcePath;

    /** Map of the parameters being sent as part of this request */
    private Map<String, String> parameters = new HashMap<String, String>();

    /** Map of the headers included in this request */
    private Map<String, String> headers = new HashMap<String, String>();

    /** The service endpoint to which this request should be sent */
    private URI endpoint;

    /** The name of the service to which this request is being sent */
    private String serviceName;

    /** The HTTP method to use when sending this request. */
    private HttpMethod httpMethod = HttpMethod.POST;

    /** encrypt content body data content. */
    private String encryptContent;

    public String getEncryptContent() {
        return encryptContent;
    }

    public void setEncryptContent(String encryptContent) {
        this.encryptContent = encryptContent;
    }

    /** body data content. */
    private String content;

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @see com.netease.cloud.http.Request#addHeader(java.lang.String, java.lang.String)
     */
    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    /**
     * @see com.netease.cloud.http.Request#getHeaders()
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @see com.netease.cloud.http.Request#setResourcePath(java.lang.String)
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * @see com.netease.cloud.http.Request#getResourcePath()
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @see com.netease.cloud.http.Request#addParameter(java.lang.String, java.lang.String)
     */
    public void addParameter(String name, String value) {
        parameters.put(name, value);
    }

    /**
     * @see com.netease.cloud.http.Request#getParameters()
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * @see com.netease.cloud.http.Request#getHttpMethod()
     */
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * @see com.netease.cloud.http.Request#setHttpMethod(com.netease.cloud.http.HttpMethod)
     */
    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * @see com.netease.cloud.http.Request#setEndpoint(java.net.URI)
     */
    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @see com.netease.cloud.http.Request#getEndpoint()
     */
    public URI getEndpoint() {
        return endpoint;
    }

    /**
     * @see com.netease.cloud.http.Request#getServiceName()
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @see com.netease.cloud.http.Request#getContent()
     */
    public String getContent() {
        return content;
    }

    /**
     * @see com.netease.cloud.http.Request#setContent(String)
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @see com.netease.cloud.http.Request#setHeaders(java.util.Map)
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
    }

    /**
     * @see com.netease.cloud.http.Request#setParameters(java.util.Map)
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getHttpMethod().toString() + " ");
        builder.append(getEndpoint().toString() + " ");

        builder.append((getResourcePath() != null ? getResourcePath() : "") + " ");

        if (!getParameters().isEmpty()) {
            builder.append("Parameters: (");
            for (String key : getParameters().keySet()) {
                String value = getParameters().get(key);
                builder.append(key + ": " + value + ", ");
            }
            builder.append(") ");
        }

        if (!getHeaders().isEmpty()) {
            builder.append("Headers: (");
            for (String key : getHeaders().keySet()) {
                String value = getHeaders().get(key);
                builder.append(key + ": " + value + ", ");
            }
            builder.append(") ");
        }

        return builder.toString();
    }

    public Request withParameter(String name, String value) {
        // TODO Auto-generated method stub
        return null;
    }

}
