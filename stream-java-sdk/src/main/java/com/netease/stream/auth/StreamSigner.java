package com.netease.stream.auth;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netease.stream.exception.ClientException;
import com.netease.stream.http.Headers;
import com.netease.stream.http.Request;
import com.netease.stream.util.DateUtils;
import com.netease.stream.util.StringUtils;

/**
 * Implementation of the {@linkplain Signer} interface specific to StreamProxy's signing algorithm.
 */
public class StreamSigner extends AbstractSigner {

    /** Shared log for signing debug output */
    private static final Log log = LogFactory.getLog(StreamSigner.class);

    /**
     * Constructs a new S3Signer to sign requests based on the credentials, HTTP method and
     * canonical S3 resource path.
     * 
     */
    public StreamSigner() {}

    /**
     * Sign request with credentials.
     * 
     * @param request Needed to be signed.
     * @param credentials credentials.
     */
    public void sign(Request request, Credentials credentials) throws ClientException {
        if (credentials == null) {
            log.debug("Canonical string will not be signed, as no  Secret Key was provided");
            return;
        }

        Credentials sanitizedCredentials = sanitizeCredentials(credentials);

        request.addHeader(Headers.DATE, new DateUtils().formatRfc1123Date(new Date()));
        String canonicalString = StringUtils.makeCanonicalString(request);

        String signature =
                super.signAndBase64Encode(canonicalString, sanitizedCredentials.getSecretKey(),
                        SigningAlgorithm.HmacSHA256);
        request.addHeader("Authorization", "LOG " + sanitizedCredentials.getAccessKeyId() + ":"
                + signature);
    }

}
