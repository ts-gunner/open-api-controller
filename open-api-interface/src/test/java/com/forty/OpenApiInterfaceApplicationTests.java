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

    }

}
