package com.netease.stream.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.netease.stream.exception.ClientException;
import com.netease.stream.http.Request;

/**
 * Utilities useful for String.
 */
public class StringUtils {

    /**
     * Calculate the canonical string for a REST/HTTP request.
     * 
     * @param request Store information, heads, content, etc.
     * @param <T> The type of original, user facing request represented by this request.
     * @return the canonical string.
     */
    public static <T> String makeCanonicalString(Request request) {
        StringBuilder buf = new StringBuilder();
        // HTTP-Verb
        buf.append(request.getHttpMethod() + "\n");
        // Content-MD5
        buf.append(request.getEncryptContent() + "\n");
        // Content-Type
        buf.append(request.getHeaders().get("Content-Type") + "\n");
        // Date
        buf.append(request.getHeaders().get("Date") + "\n");
        // CanonicalizedHeaders
        buf.append("\n");
        // CanonicalizedResource
        buf.append(request.getResourcePath());
        return buf.toString();
    }

    /**
     * Covert stream to string.
     * 
     * @param is Input stream needed to be converted.
     * @return string Converted string returned.
     */
    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            throw new ClientException(e.getMessage(), e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new ClientException(e.getMessage(), e);
            }
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return "";
        }

    }
}
