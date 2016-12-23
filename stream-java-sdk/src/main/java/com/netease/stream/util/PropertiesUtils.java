package com.netease.stream.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for accessing SDK versioning information.
 */
public class PropertiesUtils {

    /** The SDK version info file with SDK versioning info */
    private static final String PROPERTIES_FILE = "properties.properties";
    /** SDK version info */
    private static String version = null;
    /** SDK platform info */
    private static String platform = null;
    /** User Agent info */
    private static String userAgent = null;
    /** log subscription host */
    private static String logSubscriptionHost = null;
    /** stream host */
    private static String streamHost = null;
    /** subscription position resource path */
    private static String subscriptionPositionResourcePath = null;
    /** get logs resource path */
    private static String getLogsResourcePath = null;
    /** get offset resource path */
    private static String getOffsetResourcePath = null;
    /** get record resource path */
    private static String getRecordsResourcePath = null;
    /** put record resource path */
    private static String putRecordsResourcePath = null;

    /**
     * Returns the current version for the SDK in which this class is running. Version information
     * is obtained from from the properties.properties file which the Java SDK build process
     * generates.
     * 
     * @return The current version for the SDK, if known, otherwise returns a string indicating that
     *         the version information is not available.
     */
    public static String getVersion() {
        if (version == null) {
            initializeProperties();
        }

        return version;
    }

    /**
     * Returns the current platform for the SDK in which this class is running. platform information
     * is obtained from from the properties.properties file which the Java SDK build process
     * generates.
     * 
     * @return The current platform for the SDK, if known, otherwise returns a string indicating
     *         that the platform information is not available.
     */
    public static String getPlatform() {
        if (platform == null) {
            initializeProperties();
        }

        return platform;
    }

    /**
     * Returns the current log subscription host for the SDK in which this class is running.
     * logSubscriptionHost information is obtained from from the properties.properties file which
     * the Java SDK build process generates.
     * 
     * @return log subscription host.
     */
    public static String getLogSubscriptionHost() {
        if (logSubscriptionHost == null) {
            initializeProperties();
        }

        return logSubscriptionHost;
    }

    /**
     * Returns the current stream host for the SDK in which this class is running. streamHost
     * information is obtained from from the properties.properties file which the Java SDK build
     * process generates.
     * 
     * @return stream host.
     */
    public static String getStreamHost() {
        if (streamHost == null) {
            initializeProperties();
        }

        return streamHost;
    }

    /**
     * Returns the current subscription position resource path for the SDK in which this class is
     * running. subscriptionPosition resource path information is obtained from from the
     * properties.properties file which the Java SDK build process generates.
     * 
     * @return subscription position resource path.
     */
    public static String getSubscriptionPositionResourcePath() {
        if (subscriptionPositionResourcePath == null) {
            initializeProperties();
        }

        return subscriptionPositionResourcePath;
    }

    /**
     * Returns the current logs resource path for the SDK in which this class is running. Logs
     * resource path information is obtained from from the properties.properties file which the Java
     * SDK build process generates.
     * 
     * @return logs resource path.
     */
    public static String getLogsResourcePath() {
        if (getLogsResourcePath == null) {
            initializeProperties();
        }

        return getLogsResourcePath;
    }


    /**
     * Returns the offset resource path for the SDK in which this class is running. offset resource
     * path information is obtained from from the properties.properties file which the Java SDK
     * build process generates.
     * 
     * @return logs resource path.
     */
    public static String getOffsetResourcePath() {
        if (getOffsetResourcePath == null) {
            initializeProperties();
        }

        return getOffsetResourcePath;
    }

    /**
     * Returns the records resource path for the SDK in which this class is running. records
     * resource path information is obtained from from the properties.properties file which the Java
     * SDK build process generates.
     * 
     * @return logs resource path.
     */
    public static String getRecordsResourcePath() {
        if (getRecordsResourcePath == null) {
            initializeProperties();
        }

        return getRecordsResourcePath;
    }

    /**
     * Returns the put records resource path for the SDK in which this class is running. put records
     * resource path information is obtained from from the properties.properties file which the Java
     * SDK build process generates.
     * 
     * @return logs resource path.
     */
    public static String getPutRecordsResourcePath() {
        if (putRecordsResourcePath == null) {
            initializeProperties();
        }

        return putRecordsResourcePath;
    }


    /**
     * @return Returns the User Agent string to be used when communicating with the services. The
     *         User Agent encapsulates SDK, Java, OS and region information.
     */
    public static String getUserAgent() {
        if (userAgent == null) {
            initializeUserAgent();
        }

        return userAgent;
    }

    /**
     * Loads the properties.properties file from the Java SDK and stores the information so that the
     * file doesn't have to be read the next time the data is needed.
     */
    private static void initializeProperties() {
        InputStream inputStream =
                PropertiesUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        Properties versionInfoProperties = new Properties();
        try {
            if (inputStream == null)
                throw new Exception(PROPERTIES_FILE + " not found on classpath");

            versionInfoProperties.load(inputStream);
            version = versionInfoProperties.getProperty("version");
            platform = versionInfoProperties.getProperty("platform");
            logSubscriptionHost = versionInfoProperties.getProperty("logSubscriptionHost");
            streamHost = versionInfoProperties.getProperty("streamHost");
            subscriptionPositionResourcePath =
                    versionInfoProperties.getProperty("subscriptionPositionResourcePath");
            getLogsResourcePath = versionInfoProperties.getProperty("getLogsResourcePath");
            getOffsetResourcePath = versionInfoProperties.getProperty("getOffsetResourcePath");
            getRecordsResourcePath = versionInfoProperties.getProperty("getRecordsResourcePath");
            putRecordsResourcePath = versionInfoProperties.getProperty("putRecordsResourcePath");
        } catch (Exception e) {
            version = "unknown-version";
            platform = "java";
        }
    }

    /**
     * Loads the properties.properties file from the Java SDK and stores the information so that the
     * file doesn't have to be read the next time the data is needed.
     */
    private static void initializeUserAgent() {
        StringBuilder buffer = new StringBuilder(1024);
        buffer.append("streamproxy-sdk-" + PropertiesUtils.getPlatform().toLowerCase() + "/");
        buffer.append(PropertiesUtils.getVersion());
        buffer.append(" ");
        buffer.append(System.getProperty("os.name").replace(' ', '_') + "/"
                + System.getProperty("os.version").replace(' ', '_'));
        buffer.append(" ");
        buffer.append(System.getProperty("java.vm.name").replace(' ', '_') + "/"
                + System.getProperty("java.vm.version").replace(' ', '_'));

        String region = "";
        try {
            region =
                    " " + System.getProperty("user.language").replace(' ', '_') + "_"
                            + System.getProperty("user.region").replace(' ', '_');
        } catch (Exception exception) {
        }

        buffer.append(region);
        userAgent = buffer.toString();
    }
}
