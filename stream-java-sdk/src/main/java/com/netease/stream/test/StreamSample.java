package com.netease.stream.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String topicName = "test201612121010.statetest-combloghzx";
        int partitionId = 0;
        String offsetType = "EARLIEST";
        int count = 1000;
        long limit = 10;

        try {
            // get subscription position
            client = new StreamClient(accessKey, secretKey);
            String ret = client.getSubscriptionPosition(positionType, subscriptionName);
            System.out.println(ret);

            // get subscription logs
            JSONObject retObject = new JSONObject(ret);
            String logsPosition = retObject.getJSONObject("result").getString("position");
            String logs = client.getLogs(logsPosition, limit, subscriptionName);
            System.out.println(logs);

            // cal number of subscription logs
            JSONObject logsObject = new JSONObject(logs);
            JSONArray subscription_logs =
                    logsObject.getJSONObject("result").getJSONArray("subscription_logs");
            System.out.println(subscription_logs.length());

            // get offset
            String offsetRet = client.getOffset(topicName, partitionId, offsetType);
            System.out.println(offsetRet);

            // get records
            JSONObject offsetRetObject = new JSONObject(offsetRet);
            String offset = offsetRetObject.getJSONObject("result").getString("offset");
            String getRecordsRet = client.getRecords(offset, limit);
            System.out.println(getRecordsRet);

            // put records
            List<Map<String, String>> records = new ArrayList<Map<String, String>>();
            Map<String, String> record = new HashMap<String, String>();
            for (int i = 0; i < 1000; i++) {
                record.put("data", "hzx test now" + i + ".");
                records.add(record);
            }
            String putRecordsRet = client.putRecords(topicName, partitionId, records, count);
            System.out.println(putRecordsRet);

            // get records
            offsetRetObject = new JSONObject(offsetRet);
            offset = offsetRetObject.getJSONObject("result").getString("offset");
            getRecordsRet = client.getRecords(offset, limit);
            System.out.println(getRecordsRet);

        } catch (Exception e) {
            System.out.println("Execute error " + e.getMessage());
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }

    }
}
