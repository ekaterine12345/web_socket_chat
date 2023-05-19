package com.example.web_socket.websocketUtil;

import com.example.web_socket.models.Message;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(
        value = "/chat/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)
public class ChatEndpoint {

    private Session session;

    private static Set<ChatEndpoint> chatEndpoints =
            new CopyOnWriteArraySet<>();

    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {

        this.session = session;
        users.put(session.getId(), username);
        chatEndpoints.add(this);

        Message message = new Message();
        message.setFrom(username);
        message.setContent("Connected...");
     //   System.out.println("onOpen for username = "+username);
        broadcast(message);

    }

    @OnMessage
    public void onMessage(Session session, Message message) {

        String username = users.get(session.getId());
        message.setFrom(username);
       // System.out.println("OnMessage for session = "+session+" message " + message);
        broadcast(message);

    }

    @OnClose
    public void onClose(Session session) {

        Message message = new Message();
        String username = users.get(session.getId());
        message.setFrom(username);
        message.setContent("Disconnected...");

        chatEndpoints.remove(this);
       // System.out.println("onOpen for session = "+session);
        broadcast(message);

    }

    private static void broadcast(Message message) {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Error handling
    }


}