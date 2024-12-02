package com.forty;

import com.forty.sdk.client.FortyClient;
import com.forty.sdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenApiInterfaceApplicationTests {

    @Autowired
    FortyClient fortyClient;

    @Test
    void contextLoads() {
        String result1 = fortyClient.getName("牛逼plus");
        System.out.println(result1);


        User user = new User();
        user.setUserName("张xx是66");
        String s = fortyClient.postName(user);
        System.out.println(s);
    }

}
