package com.forty;

import com.forty.sdk.client.FortyClient;

public class ClientMain {
    public static void main(String[] args) {
//        String secretId = "e8de092f5f9e91f6d451870cfcf6133b3a16ec4c44751e432efd5463e380b946"; // admin
        String secretId = "2bcbb957b4503e688390e5068f44be32f9c64eb86bd041bf031c5707d1cd80c4"; // test4
        String secretKey = "7:7SRcI^|Mi#Ru&69#6twMtu1kJB=SJ4|u]Lw)BJ";
        FortyClient client = new FortyClient(secretId, secretKey);
        String result = client.getName("我是大聪明");
        System.out.println(result);
    }
}
