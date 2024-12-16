package com.forty.sdk.client;

import java.lang.reflect.Constructor;


public class FortyClient {

    private ClientConfiguration configuration;

    public FortyClient(String secretId, String secretKey) {
        this.configuration = new ClientConfiguration(secretId, secretKey);
    }

    public FortyClient(String secretId, String secretKey, String public_open_api_url) {
        this.configuration = new ClientConfiguration(secretId, secretKey,public_open_api_url);
    }


    public <T> T adaptor(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(ClientConfiguration.class);
            constructor.setAccessible(true);
            return constructor.newInstance(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
