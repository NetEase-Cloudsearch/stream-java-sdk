package com.netease.stream.test;

import com.netease.stream.client.StreamClient;
import com.netease.stream.util.json.JSONArray;
import com.netease.stream.util.json.JSONObject;

public class StreamSample {

    private static String accessKey = "1a51246df285464c957819f0d97e5d5c";
    private static String secretKey = "41121a6912a1493b8113420f674efb00";

    public static void main(String[] args) throws Exception {

        String subscriptionName = "test201612151903.default-wm3zq";
        String positionType = "EARLIEST";
        StreamClient client = null;

        try {
            // get subscription position
            client = new StreamClient(accessKey, secretKey);
            String ret = client.getSubscriptionPosition(positionType, subscriptionName);
            System.out.println(ret);

            // get subscription logs
            JSONObject retObject = new JSONObject(ret);
            String logsPosition = retObject.getJSONObject("result").getString("position");
            long limit = 1;
            String logs = client.getLogs(logsPosition, limit, subscriptionName);
            System.out.println(logs);

            // cal number of subscription logs
            JSONObject logsObject = new JSONObject(logs);
            JSONArray subscription_logs =
                    logsObject.getJSONObject("result").getJSONArray("subscription_logs");
            System.out.println(subscription_logs.length());
        } catch (Exception e) {
            System.out.println("Execute error " + e.getMessage());
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }

    }

}
