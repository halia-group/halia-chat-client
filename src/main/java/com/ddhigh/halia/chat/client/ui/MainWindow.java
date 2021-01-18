package com.ddhigh.halia.chat.client.ui;

import com.ddhigh.halia.chat.client.ThreadPool;
import com.ddhigh.halia.chat.client.io.DefaultChatListener;
import com.ddhigh.halia.chat.client.io.packet.*;
import com.ddhigh.halia.chat.client.io.protocol.FriendStatus;
import com.ddhigh.halia.chat.client.io.protocol.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * todo 将申请列表拆分到菜单，点击打开才拉取申请列表，有新申请时，该button数量+1，不再直接弹出Dialog
 */
public class MainWindow {
    private static Logger logger = LoggerFactory.getLogger(MainWindow.class);
    private static MainWindow shared;

    public static MainWindow shared() {
        if (shared == null) {
            synchronized (MainWindow.class) {
                if (shared == null) {
                    shared = new MainWindow();
                }
            }
        }
        return shared;
    }

    private final JFrame frame;
    private JPanel panel;
    private JList<String> friendList;
    private DefaultComboBoxModel<String> friendListModel;
    private JTextArea chatTextArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton addFriendButton;

    private MainWindow() {
        frame = new JFrame("聊天");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        setupUI();
        setupEvents();

    }

    private void setupUI() {
        friendListModel = new DefaultComboBoxModel<>();
        friendList.setModel(friendListModel);
    }

    private void setupEvents() {
        addFriendButton.addActionListener(e -> {
            SearchUserDialog dialog = new SearchUserDialog();
            dialog.setLocationRelativeTo(frame);
            dialog.setTitle("添加好友");
            dialog.pack();
            dialog.setVisible(true);
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                ThreadPool.submit(() -> DefaultChatListener.shared().doFriendListReq());
            }
        });
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

    // 刷新好友列表
    public void onFriendListResp(FriendListResp packet) {
        SwingUtilities.invokeLater(() -> {
            if (packet.getCode() != 0) {
                JOptionPane.showMessageDialog(null, packet.getMessage());
                return;
            }
            friendListModel.removeAllElements();
            for (FriendListResp.Friend friend : packet.getList()) {
                friendListModel.addElement(String.format("%s<%d>", friend.getNickname(), friend.getId()));
            }
        });
    }

    // 接收到好友申请回调
    public void onFriendApplyResp(FriendApplyResp packet) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, packet.getMessage()));
    }

    // 接收到好友申请操作回调
    public void onFriendApplyResultResp(FriendApplyResultResp packet) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, packet.getMessage()));
    }

    // 接收到好友申请请求
    public void onFriendApplyMessage(FriendApplyMessage packet) {
        SwingUtilities.invokeLater(() -> {
            var title = String.format("<%s>申请加您为好友", packet.getNickname());
            var result = JOptionPane.showConfirmDialog(null, packet.getReason(), title, JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                ThreadPool.submit(() -> DefaultChatListener.shared().doFriendAccept(packet.getSenderId()));
                return;
            }
            // 不同意，输入理由
            var reason = JOptionPane.showInputDialog(null, "请输入拒绝理由");
            ThreadPool.submit(() -> DefaultChatListener.shared().doFriendReject(packet.getSenderId(), reason));
        });
    }

    // 接收到好友申请操作结果
    public void onFriendApplyResultMessage(FriendApplyResultMessage packet) {
        SwingUtilities.invokeLater(() -> {
            if (packet.getStatus() == FriendStatus.ACCEPT) {
                var message = String.format("您已成功添加<%s>为好友", packet.getNickname());
                JOptionPane.showMessageDialog(null, message);
                // 加载好友列表
                ThreadPool.submit(() -> DefaultChatListener.shared().doFriendListReq());
                return;
            }
            var title = String.format("<%s>拒绝了您的好友申请", packet.getNickname());
            JOptionPane.showMessageDialog(null, packet.getReason(), title, JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
