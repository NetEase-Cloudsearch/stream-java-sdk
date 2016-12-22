package com.netease.stream.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.netease.stream.exception.ClientException;

/**
 * Abstract base class for signing protocol implementations. Provides utilities commonly needed by
 * signing protocols such as computing canonicalized host names, query string parameters, etc. Not
 * intended to be sub-classed by developers.
 */
public abstract class AbstractSigner implements Signer {

    /** The default encoding to use when URL encoding */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Computes an RFC 2104-compliant HMAC signature and returns the result as a Base64 encoded
     * string.
     * 
     * @param data Data needed to sign.
     * @param key The key to sign data.
     * @param algorithm The algorithm to sign data.
     * @return String Signed string.
     * @throws ClientException ClientException.
     */
    protected String signAndBase64Encode(String data, String key, SigningAlgorithm algorithm)
            throws ClientException {
        try {
            return signAndBase64Encode(data.getBytes(DEFAULT_ENCODING), key, algorithm);
        } catch (UnsupportedEncodingException e) {
            throw new ClientException("Unable to calculate a request signature: " + e.getMessage(),
                    e);
        }
    }

    /**
     * Computes an RFC 2104-compliant HMAC signature for an array of bytes and returns the result as
     * a Base64 encoded string.
     * 
     * @param data Data needed to sign.
     * @param key The key to sign data.
     * @param algorithm The algorithm to sign data.
     * @return String Signed string.
     * @throws ClientException ClientException.
     */
    protected String signAndBase64Encode(byte[] data, String key, SigningAlgorithm algorithm)
            throws ClientException {
        try {
            byte[] signature = sign(data, key.getBytes(DEFAULT_ENCODING), algorithm);
            return new String(Base64.encodeBase64(signature));
        } catch (Exception e) {
            throw new ClientException("Unable to calculate a request signature: " + e.getMessage(),
                    e);
        }
    }

    /**
     * Computes an RFC 2104-compliant HMAC signature for an array of bytes and returns the result as
     * a Base64 encoded string.
     * 
     * @param stringData Data needed to sign.
     * @param key The key to sign data.
     * @param algorithm The algorithm to sign data.
     * @return byte[] of Signed string.
     * @throws ClientException ClientException.
     */
    protected byte[] sign(String stringData, byte[] key, SigningAlgorithm algorithm)
            throws ClientException {
        try {
            byte[] data = stringData.getBytes(DEFAULT_ENCODING);
            return sign(data, key, algorithm);
        } catch (Exception e) {
            throw new ClientException("Unable to calculate a request signature: " + e.getMessage(),
                    e);
        }
    }

    /**
     * Computes an RFC 2104-compliant HMAC signature for an array of bytes and returns the result as
     * a Base64 encoded string.
     * 
     * @param data Data needed to sign.
     * @param key The key to sign data.
     * @param algorithm The algorithm to sign data.
     * @return byte[] of Signed string.
     * @throws ClientException ClientException.
     */
    protected byte[] sign(byte[] data, byte[] key, SigningAlgorithm algorithm)
            throws ClientException {
        try {
            Mac mac = Mac.getInstance(algorithm.toString());
            mac.init(new SecretKeySpec(key, algorithm.toString()));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new ClientException("Unable to calculate a request signature: " + e.getMessage(),
                    e);
        }
    }

    /**
     * Hashes the string contents (assumed to be UTF-8) using the SHA-256 algorithm.
     * 
     * @param text The string to hash.
     * @return The hashed bytes from the specified string.
     * @throws ClientException If the hash cannot be computed.
     */
    protected byte[] hash(String text) throws ClientException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes(DEFAULT_ENCODING));
            return md.digest();
        } catch (Exception e) {
            throw new ClientException("Unable to compute hash while signing request: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Loads the individual access key ID and secret key from the specified credentials, ensuring
     * that access to the credentials is synchronized on the credentials object itself, and trimming
     * any extra whitespace from the credentials.
     * 
     * @param credentials.
     * @return Credentials A new credentials object with the sanitized credentials.
     */
    protected Credentials sanitizeCredentials(Credentials credentials) {
        String accessKeyId = null;
        String secretKey = null;
        synchronized (credentials) {
            accessKeyId = credentials.getAccessKeyId();
            secretKey = credentials.getSecretKey();

        }
        if (secretKey != null)
            secretKey = secretKey.trim();
        if (accessKeyId != null)
            accessKeyId = accessKeyId.trim();

        return new BasicCredentials(accessKeyId, secretKey);
    }

}
