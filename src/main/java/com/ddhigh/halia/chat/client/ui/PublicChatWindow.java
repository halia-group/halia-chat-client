package com.ddhigh.halia.chat.client.ui;

import com.ddhigh.halia.chat.client.ThreadPool;
import com.ddhigh.halia.chat.client.io.DefaultChatListener;
import com.ddhigh.halia.chat.client.io.packet.PublicChatMessage;
import com.ddhigh.halia.chat.client.io.packet.PublicChatResp;
import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class PublicChatWindow {
    private static Logger logger = LoggerFactory.getLogger(PublicChatWindow.class);
    private static PublicChatWindow shared;

    public static PublicChatWindow shared() {
        if (shared == null) {
            synchronized (PublicChatWindow.class) {
                if (shared == null) {
                    shared = new PublicChatWindow();
                }
            }
        }
        return shared;
    }

    private final JFrame frame;
    private JPanel panel;
    private JTextArea chatTextArea;
    private JTextField messageField;
    private JButton sendButton;

    private PublicChatWindow() {
        frame = new JFrame("聊天");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        sendButton.addActionListener(e -> {
            var text = messageField.getText();
            if (text.isEmpty()) {
                return;
            }
            sendButton.setEnabled(false);
            ThreadPool.submit(() -> DefaultChatListener.shared().doSendPublicChat(text));
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void onPublicChatResp(PublicChatResp packet) {
        SwingUtilities.invokeLater(() -> {
            sendButton.setEnabled(true);
            if (packet.getCode() != 0) {
                JOptionPane.showMessageDialog(null, packet.getMessage());
                return;
            }
            messageField.setText("");
        });
    }

    public void onPublicChatMessage(PublicChatMessage packet) {
        SwingUtilities.invokeLater(() -> {
            if (packet.getMsgType() == MsgType.TEXT) {
                String s = String.format(
                        "%s:<%s>:%s\n",
                        packet.getTime().toString(),
                        packet.getPublisher(),
                        packet.getMessage()
                );
                chatTextArea.append(s);
            }
        });
    }
}
