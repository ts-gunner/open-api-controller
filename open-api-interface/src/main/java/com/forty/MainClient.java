package com.forty;

import com.forty.client.FortyClient;
import com.forty.model.User;

import java.text.MessageFormat;

public class MainClient {
    public static void main(String[] args) {
        String secretID = "asdasd";
        String secretKey = "asdassadasdasd";
        FortyClient client = new FortyClient(secretID, secretKey);

        String resp1 = client.getName("哈哈哈哈你真棒");
        System.out.println(MessageFormat.format("resp1: {0}", resp1));

        User user = new User();
        user.setUserName("postUser你真牛");
        String resp2 = client.postName(user);
        System.out.println(MessageFormat.format("resp2: {0}", resp2));
    }
}
