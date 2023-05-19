package com.example.web_socket.websocketUtil;

import com.example.web_socket.models.Message;
import com.google.gson.Gson;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

public class MessageEncoder implements Encoder.Text<Message> {

    private static Gson gson = new Gson();

    @Override
    public String encode(Message object) throws EncodeException {
        return gson.toJson(object);
    }

}
