package com.wfc.web.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by snow on 14-8-5.
 */
class JSONUtils {

    private static ObjectMapper objectMapper;

    static {
        init();
    }

    private static void init() {
        if (objectMapper == null)
            objectMapper = new ObjectMapper();
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * json 转对象
     *
     * @param json
     * @param c
     * @return
     * @throws Exception
     */
    public static <T> T toObject(String json, Class c) throws Exception {
        return (T) objectMapper.readValue(json, c);
    }

    public static JSONObject getJSONObjectByInput(InputStream is) {
        try {
            StringBuilder stb = new StringBuilder();
            String s = null;
            String ret;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while ((s = br.readLine()) != null) {
                stb.append(s);
            }

            System.out.println("=========" + stb.toString());
            return new JSONObject(stb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
