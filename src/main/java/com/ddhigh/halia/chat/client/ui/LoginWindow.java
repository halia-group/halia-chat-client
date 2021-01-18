package com.ddhigh.halia.chat.client.ui;

import com.ddhigh.halia.chat.client.ThreadPool;
import com.ddhigh.halia.chat.client.io.DefaultChatListener;
import com.ddhigh.halia.chat.client.io.packet.LoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class LoginWindow {
    private static final Logger logger = LoggerFactory.getLogger(LoginWindow.class);
    private static LoginWindow shared;

    public static LoginWindow shared() {
        if (shared == null) {
            synchronized (LoginWindow.class) {
                if (shared == null) {
                    shared = new LoginWindow();
                }
            }
        }
        return shared;
    }

    private final JFrame frame;
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;

    private LoginWindow() {
        frame = new JFrame("登录");
        frame.setResizable(false);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        // 登录
        loginButton.addActionListener(e -> {
            var username = usernameField.getText().trim();
            var password = new String(passwordField.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请将字段填写完整");
                return;
            }
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);

            ThreadPool.submit(() -> DefaultChatListener.shared().doLogin(username, password));
        });
        // 注册
        registerButton.addActionListener(e -> {
            hide();
            RegisterWindow.shared().show();
        });
        frame.pack();
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void onPong() {
        SwingUtilities.invokeLater(() -> {
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
        });
    }

    public void onLoginResp(LoginResp resp) {
        SwingUtilities.invokeLater(() -> {
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
            if (resp.getCode() != 0) {
                JOptionPane.showMessageDialog(null, resp.getMessage());
                return;
            }
            frame.dispose();
            shared = null;

            PublicChatWindow.shared().show();
        });
    }

}
