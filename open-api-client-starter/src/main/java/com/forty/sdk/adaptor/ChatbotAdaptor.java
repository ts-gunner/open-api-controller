package com.forty.sdk.adaptor;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.forty.sdk.api.Chatbot;
import com.forty.sdk.client.ClientConfiguration;

import java.util.HashMap;
import java.util.Map;


public class ChatbotAdaptor extends BaseAdaptor implements Chatbot {

    ChatbotAdaptor(ClientConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void queryDocument() {

    }

    @Override
    public String ask(String question) {
        Map<String, Object> map = new HashMap<>();
        map.put("question", question);
        String url = HttpUtil.urlWithForm(this.getConfiguration().getPrefixUrl() + "/public_api/chatbot/ask", map, CharsetUtil.CHARSET_UTF_8, false);
        HttpRequest.get(url);
        return "";
    }
}
