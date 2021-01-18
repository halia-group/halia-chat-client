package com.ddhigh.halia.chat.client;

import com.ddhigh.halia.chat.client.io.ChatClient;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var client = new ChatClient("localhost", 8080);
        client.bootstrap();
    }
}
