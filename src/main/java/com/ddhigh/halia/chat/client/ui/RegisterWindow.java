package com.ddhigh.halia.chat.client.ui;

import com.ddhigh.halia.chat.client.ThreadPool;
import com.ddhigh.halia.chat.client.io.DefaultChatListener;
import com.ddhigh.halia.chat.client.io.packet.RegisterResp;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterWindow {
    private static RegisterWindow shared;

    public static RegisterWindow shared() {
        if (shared == null) {
            synchronized (RegisterWindow.class) {
                if (shared == null) {
                    shared = new RegisterWindow();
                }
            }
        }
        return shared;
    }

    private final JFrame frame;
    private JPanel panel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField nicknameField;
    private JButton registerButton;

    private RegisterWindow() {
        frame = new JFrame("注册");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LoginWindow.shared().show();
            }
        });
        frame.setLocationRelativeTo(null);
        registerButton.addActionListener(e -> {
            var username = usernameField.getText().trim();
            var password = passwordField.getText().trim();
            var nickname = nicknameField.getText().trim();
            if (username.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请将字段填写完毕");
                return;
            }
            registerButton.setEnabled(false);
            ThreadPool.submit(() -> DefaultChatListener.shared().doRegister(username, password, nickname));
        });
        frame.pack();
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void onRegisterResp(RegisterResp packet) {
        SwingUtilities.invokeLater(() -> {
            registerButton.setEnabled(true);
            JOptionPane.showMessageDialog(null, packet.getMessage());
            if (packet.getCode() == 0) {
                frame.dispose();
                shared = null;
                LoginWindow.shared().show();
            }
        });
    }
}
