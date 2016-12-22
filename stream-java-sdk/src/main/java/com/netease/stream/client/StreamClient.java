package com.netease.stream.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netease.stream.auth.BasicCredentials;
import com.netease.stream.auth.CredentialsProvider;
import com.netease.stream.auth.StaticCredentialsProvider;
import com.netease.stream.auth.StreamSigner;
import com.netease.stream.config.ClientConfiguration;
import com.netease.stream.exception.ClientException;
import com.netease.stream.exception.ServiceException;
import com.netease.stream.http.DefaultRequest;
import com.netease.stream.http.HttpMethod;
import com.netease.stream.http.HttpResponse;
import com.netease.stream.http.Request;
import com.netease.stream.util.CommonUtils;
import com.netease.stream.util.Md5Utils;
import com.netease.stream.util.PropertiesUtils;
import com.netease.stream.util.json.JSONException;
import com.netease.stream.util.json.JSONObject;

/**
 * 
 * The client for accessing the Netease streamproxy service. The client provides storage service on
 * the Internet. This client can be used to store and retrieve any amount of data, at any time, from
 * anywhere on client.
 */
public class StreamClient extends DefaultClient implements Stream {

    /** Shared logger for client events */
    private static Log log = LogFactory.getLog(StreamClient.class);

    /** Provider for credentials. */
    private CredentialsProvider CredentialsProvider;

    /**
     * Constructs a new streamproxy client using the specified credentials and client configuration
     * to access streamproxy.
     * 
     * @param accessKey The access id to use when making requests to streamproxy with this client.
     * @param secretKey The secret key to use when making requests to streamproxy with this client.
     * 
     */
    public StreamClient(String accessKey, String secretKey) {
        super(new ClientConfiguration());
        this.CredentialsProvider =
                new StaticCredentialsProvider(new BasicCredentials(accessKey, secretKey));
    }

    /**
     * set streams proxy host
     * 
     * @param subscriptionName subscription name.
     */
    private void initStreamsProxyHost(String subscriptionName) {
        // online enviroment
        setEndpoint(subscriptionName + "." + PropertiesUtils.getStreamsProxyHost());
        // test enviroment
        // setEndpoint(PropertiesUtils.getStreamsProxyHost());
    }

    /**
     * execute request; send request and get respone.
     * 
     * @param request Request.
     * @return String respone.
     */
    private String executeRequest(Request request) {
        try {
            log.info("Send request: " + request.toString());
            HttpResponse respone = client.execute(request);
            log.info("recevie responses: " + respone.toString());
            return respone.toString();
        } catch (Exception e) {
            throw new ClientException("Execute error " + e.getMessage(), e);
        }
    }

    /**
     * Get subscription position
     * 
     * @param positionType wanted log position type.
     * @param subscriptionName subscription logs name.
     * @return response result from server.
     * @throws ClientException ClientException.
     * @throws ServiceException ClientException.
     */
    public String getSubscriptionPosition(String positionType, String subscriptionName)
            throws ClientException, ServiceException {

        initStreamsProxyHost(subscriptionName);

        Request request =
                createSubscriptionPositionRequest(positionType, subscriptionName,
                        PropertiesUtils.getSubscriptionPositionResourcePath());

        return executeRequest(request);
    }

    /**
     * Get logs.
     * 
     * @param logsPosition Position to get logs.
     * @param limit how many logs to get.
     * @param subscriptionName which topic logs to get.
     * @return response String.
     * @throws ClientException ClientException.
     * @throws ServiceException ServiceException.
     */
    public String getLogs(String logsPosition, long limit, String subscriptionName)
            throws ClientException, ServiceException {

        initStreamsProxyHost(subscriptionName);

        Request request =
                createGetLogsRequest(logsPosition, limit, subscriptionName,
                        PropertiesUtils.getLogsResourcePath());

        return executeRequest(request);
    }

    /**
     * Creates and initializes a subscription position request object for the specified streamproxy
     * resource. This method is responsible for determining the right way to address resources.
     * Callers can take the request, add any additional headers or parameters, then sign and execute
     * the request.
     * 
     * @param positionType wanted log position type.
     * @param subscriptionName subscription logs name.
     * @param resourcePath resource path,which to visit.
     * @return A new request object, populated with endpoint, resource path, and service name, ready
     *         for callers to populate any additional headers or parameters, and execute.
     */
    protected Request createSubscriptionPositionRequest(String positionType,
            String subscriptionName, String resourcePath) {
        CommonUtils commonUtils = new CommonUtils();
        commonUtils.assertParameterNotNull(positionType,
                "The position type parameter must be specified.");
        commonUtils.assertParameterNotNull(subscriptionName,
                "The subscription name parameter must be specified.");
        commonUtils.assertParameterNotNull(resourcePath,
                "The resource path parameter must be specified.");

        Request request = new DefaultRequest();
        request.setHttpMethod(HttpMethod.POST);
        JSONObject content = new JSONObject();
        try {
            content.put("position_type", positionType);
        } catch (JSONException e) {
            throw new ClientException("create request error " + e.getMessage(), e);
        }
        try {
            request.setEndpoint(new URI(endpoint.getScheme() + "://" + endpoint.getAuthority()));
        } catch (URISyntaxException e) {
            throw new ClientException("Can't turn" + endpoint + "into a URI: " + e.getMessage(), e);
        }
        request.setContent(content.toString());
        request.setResourcePath(resourcePath);
        String encryptContent = createEncryptText(content.toString());
        request.setEncryptContent(encryptContent);

        request.addHeader("Content-Type", "application/json");
        // request.addHeader("Host", subscriptionName + "." + "log.c.163.com");
        request.addHeader("User-Agent", PropertiesUtils.getUserAgent());
        StreamSigner streamProxySigner = createSigner();
        streamProxySigner.sign(request, CredentialsProvider.getCredentials());

        return request;
    }


    /**
     * Creates and initializes a get logs request object for the specified streamproxy resource.
     * This method is responsible for determining the right way to address resources. Callers can
     * take the request, add any additional headers or parameters, then sign and execute the
     * request.
     * 
     * @param logsPosition Position to get logs.
     * @param limit how many logs to get.
     * @param subscriptionName which topic logs to get.
     * @param resourcePath resource path to excuse.
     * 
     * @return request
     */
    protected Request createGetLogsRequest(String logsPosition, long limit,
            String subscriptionName, String resourcePath) {
        CommonUtils commonUtils = new CommonUtils();
        commonUtils.assertParameterNotNull(logsPosition,
                "The logs position parameter must be specified.");
        commonUtils.assertParameterNotNull(subscriptionName,
                "The subscription name parameter must be specified.");
        commonUtils.assertParameterNotNull(resourcePath,
                "The resource path parameter must be specified.");

        Request request = new DefaultRequest();
        request.setHttpMethod(HttpMethod.POST);
        JSONObject content = new JSONObject();
        try {
            content.put("position", logsPosition);
            content.put("limit", limit > 0 ? limit : 0);
        } catch (JSONException e) {
            throw new ClientException("create request error " + e.getMessage(), e);
        }
        try {
            request.setEndpoint(new URI(endpoint.getScheme() + "://" + endpoint.getAuthority()));
        } catch (URISyntaxException e) {
            throw new ClientException("Can't turn" + endpoint + "into a URI: " + e.getMessage(), e);
        }
        request.setContent(content.toString());
        request.setResourcePath(resourcePath);
        String encryptContent = createEncryptText(content.toString());
        request.setEncryptContent(encryptContent);

        request.addHeader("Content-Type", "application/json");
        // request.addHeader("Host", subscriptionName + "." + "log.c.163.com");
        request.addHeader("User-Agent", PropertiesUtils.getUserAgent());
        StreamSigner streamProxySigner = createSigner();
        streamProxySigner.sign(request, CredentialsProvider.getCredentials());

        return request;
    }

    protected StreamSigner createSigner() {
        return new StreamSigner();
    }

    /**
     * encrypt body data.
     * 
     * @param text which needed to encrypt.
     * @return String encrypt body data.
     */
    private String createEncryptText(String text) {
        String encryptText = null;
        try {
            encryptText = Md5Utils.getHex(Md5Utils.computeMD5Hash(text.toString().getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new ClientException("create encrypt text error " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ClientException("create encrypt text error " + e.getMessage(), e);
        }
        return encryptText;
    }

    /**
     * Shuts down this HTTP client object, releasing any resources that might be held open. This is
     * an optional method, and callers are not expected to call it, but can if they want to
     * explicitly release any open resources. Once a client has been shutdown, it cannot be used to
     * make more requests.
     */
    public void shutdown() {
        if (client != null) {
            client.shutdown();
        }
    }

}
