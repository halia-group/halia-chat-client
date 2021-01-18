package com.ddhigh.halia.chat.client;

import com.ddhigh.halia.chat.client.io.ChatClient;
import com.ddhigh.halia.chat.client.ui.LoginWindow;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        FlatLightLaf.install();
        setDefaultFont();
        ThreadPool.submit(() -> {
            try {
                ChatClient.shared().bootstrap();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        LoginWindow.shared().show();
    }

    private static void setDefaultFont() {
        Font font = new Font("Microsoft Yahei", Font.PLAIN, 12);
        var keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
