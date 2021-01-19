package com.ddhigh.halia.chat.client.ui;

import com.ddhigh.halia.chat.client.ThreadPool;
import com.ddhigh.halia.chat.client.io.DefaultChatListener;
import com.ddhigh.halia.chat.client.io.packet.PrivateChatMessage;
import com.ddhigh.halia.chat.client.io.packet.PrivateChatResp;
import com.ddhigh.halia.chat.client.io.protocol.MsgType;

import javax.swing.*;

public class PrivateChatPanel {
    private final int receiverId;
    private JTextArea chatTextarea;
    private JPanel panel;
    private JTextField messageField;
    private JButton sendButton;

    public PrivateChatPanel(int receiverId) {
        this.receiverId = receiverId;
        sendButton.addActionListener(e -> {
            var message = messageField.getText().trim();
            if (message.isEmpty()) {
                return;
            }
            sendButton.setEnabled(false);
            ThreadPool.submit(() -> DefaultChatListener.shared().doPrivateChat(receiverId, MsgType.TEXT, message));
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public void onPrivateChatMessage(PrivateChatMessage packet) {
        if (packet.getMsgType() == MsgType.TEXT) {
            String s = String.format(
                    "%s:<%s>:%s\n",
                    packet.getTime().toString(),
                    packet.getNickname(),
                    packet.getMessage()
            );
            chatTextarea.append(s);
        }
    }

    public void onPrivateChatResp(PrivateChatResp packet) {
        sendButton.setEnabled(true);
        if (packet.getCode() != 0) {
            JOptionPane.showMessageDialog(null, packet.getMessage());
            return;
        }
        messageField.setText("");
    }
}
