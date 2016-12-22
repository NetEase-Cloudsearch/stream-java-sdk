package com.netease.stream.util;

public class CommonUtils {

    public CommonUtils() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * Asserts that the specified parameter value is not null and if it is, throws an
     * IllegalArgumentException with the specified error message.
     * 
     * @param parameterValue The parameter value being checked.
     * @param errorMessage The error message to include in the IllegalArgumentException if the
     *        specified parameter is null.
     */
    public void assertParameterNotNull(Object parameterValue, String errorMessage) {
        if (parameterValue == null)
            throw new IllegalArgumentException(errorMessage);
    }
}
