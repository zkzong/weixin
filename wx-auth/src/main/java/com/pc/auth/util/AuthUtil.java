package com.pc.auth.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class AuthUtil {
    // 开放平台
    public static final String APPID = "wx0c780653438844da";
    public static final String APPSECRET = "284c5f2c319525fa07ea831b35db62b3";

    public static JSONObject doGetJson(String url) throws ClientProtocolException, IOException {
        JSONObject jsonObject = null;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSONObject.fromObject(result);
        }
        httpGet.releaseConnection();
        return jsonObject;
    }
}
