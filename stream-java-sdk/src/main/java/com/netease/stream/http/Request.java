package com.netease.stream.http;

import java.net.URI;
import java.util.Map;

/**
 * Represents a request being sent to an Web Service, including the parameters being sent as part of
 * the request, the endpoint to which the request should be sent, etc.
 * <p>
 * This class is only intended for internal use inside the client libraries. Callers shouldn't ever
 * interact directly with objects of this class.
 * 
 */
public interface Request {

    /**
     * Adds the specified header to this request.
     * 
     * @param name The name of the header to add.
     * @param value The header's value.
     */
    public void addHeader(String name, String value);

    /**
     * Returns a map of all the headers included in this request.
     * 
     * @return A map of all the headers included in this request.
     */
    public Map<String, String> getHeaders();

    /**
     * Sets all headers, clearing any existing ones.
     * 
     * @param headers The headers to the resource being requested.
     */
    public void setHeaders(Map<String, String> headers);

    /**
     * Sets the path to the resource being requested.
     * 
     * @param path The path to the resource being requested.
     */
    public void setResourcePath(String path);

    /**
     * Returns the path to the resource being requested.
     * 
     * @return The path to the resource being requested.
     */
    public String getResourcePath();

    /**
     * Adds the specified request parameter to this request.
     * 
     * @param name The name of the request parameter.
     * @param value The value of the request parameter.
     */
    public void addParameter(String name, String value);

    /**
     * Returns a map of all parameters in this request.
     * 
     * @return A map of all parameters in this request.
     */
    public Map<String, String> getParameters();

    /**
     * Sets all parameters, clearing any existing values.
     * 
     * @param parameters Map.
     */
    public void setParameters(Map<String, String> parameters);

    /**
     * Returns the service endpoint
     * 
     * @return The service endpoint to which this request should be sent.
     */
    public URI getEndpoint();

    /**
     * Sets the service endpoint
     * 
     * @param endpoint The service endpoint to which this request should be sent.
     */
    public void setEndpoint(URI endpoint);

    /**
     * Returns the HTTP method (GET, POST, etc) to use when sending this request.
     * 
     * @return The HTTP method to use when sending this request.
     */
    public HttpMethod getHttpMethod();

    /**
     * Sets the HTTP method (GET, POST, etc) to use when sending this request.
     * 
     * @param httpMethod The HTTP method to use when sending this request.
     */
    public void setHttpMethod(HttpMethod httpMethod);

    /**
     * Returns the optional stream containing the payload data to include for this request. Not all
     * requests will contain payload data.
     * 
     * @return The optional stream containing the payload data to include for this request.
     */
    public String getContent();

    /**
     * Sets the optional stream containing the payload data to include for this request. Not all
     * requests will contain payload data.
     * 
     * @param content The optional stream containing the payload data to include for this request.
     */
    public void setContent(String content);

    /**
     * Returns the name of the service this request is for.
     * 
     * @return The name of the service this request is for.
     */
    public String getServiceName();

    public void setEncryptContent(String encryptContent);

    public String getEncryptContent();

}
