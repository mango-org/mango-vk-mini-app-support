package com.mango.support.miniapp.classes;

import com.mango.support.miniapp.MangoVkMiniAppSupportExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Reflection.Name("HMACSignVerifier")
@Reflection.Namespace(MangoVkMiniAppSupportExtension.NS)
public class HMACSignVerifier extends BaseObject {
    public HMACSignVerifier(Environment env) {
        super(env);
    }

    protected HMACSignVerifier(ClassEntity entity) {
        super(entity);
    }

    public HMACSignVerifier(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Reflection.Signature
    public static boolean verify(String url, String clientSecret) throws Exception {
        Map<String, String> queryParams = getQueryParams(new URL(url).getQuery());

        String checkString = queryParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("vk_"))
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));

        String sign = getHashCode(checkString, clientSecret);
        return sign.equals(queryParams.getOrDefault("sign", ""));
    }

    @Reflection.Signature
    public static Map<String, String> getQueryParams(String query) {
        final Map<String, String> result = new LinkedHashMap<>();
        final String[] pairs = query.split("&");

        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? decode(pair.substring(0, idx)) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? decode(pair.substring(idx + 1)) : null;

            if (key.startsWith("?"))
                key = key.substring(1);

            result.put(key, value);
        }

        return result;
    }

    private static String getHashCode(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
        return new String(Base64.getUrlEncoder().withoutPadding().encode(hmacData));
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return value;
    }

    private static String encode(String value) {
        if (value == null)
            return "";

        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return value;
    }
}
