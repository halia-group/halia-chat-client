package com.ddhigh.halia.chat.client.ui;

import com.ddhigh.halia.chat.client.ThreadPool;
import com.ddhigh.halia.chat.client.io.DefaultChatListener;
import com.ddhigh.halia.chat.client.io.packet.*;
import com.ddhigh.halia.chat.client.io.protocol.FriendStatus;
import com.ddhigh.halia.chat.client.io.protocol.MsgType;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainWindow {
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
    // 登录用户信息
    private int currentUserId;
    private String currentNickname;
    // UI
    private final JFrame frame;
    private JPanel panel;
    private JTabbedPane tabPublic;
    private JList<String> friendList;
    private final List<FriendListResp.Friend> friends = new ArrayList<>();
    private DefaultComboBoxModel<String> friendListModel;
    private JTextArea chatTextArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton addFriendButton;

    // Tab数据
    private final Map<Integer, PrivateChatPanel> privatePanelMap = new HashMap<>();// 用户ID -> 私立Panel，用来更新消息
    private final Map<Integer, String> tabFriendNicknameMap = new HashMap<>(); // tabIndex->好友

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
        friendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && friendList.getSelectedIndex() > -1) {
                    handleOpenPrivateChat(friends.get(friendList.getSelectedIndex()));
                }
            }
        });
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
        tabPublic.addChangeListener(e -> {
            var selectedIndex = tabPublic.getSelectedIndex();
            if (selectedIndex > 0 && tabFriendNicknameMap.containsKey(selectedIndex)) { // 私聊
                var nickname = tabFriendNicknameMap.get(selectedIndex);
                if (!tabPublic.getTitleAt(selectedIndex).equals(nickname)) {
                    tabPublic.setTitleAt(selectedIndex, nickname);
                }
            }
        });
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentNickname() {
        return currentNickname;
    }

    public void setCurrentNickname(String currentNickname) {
        this.currentNickname = currentNickname;
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
    // 收到公共聊天消息
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
            friends.clear();
            friends.addAll(packet.getList());
            for (FriendListResp.Friend friend : packet.getList()) {
                friendListModel.addElement(String.format("%s<%d>", friend.getNickname(), friend.getId()));
            }
        });
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

    // 私聊回调
    public void onPrivateChatResp(PrivateChatResp packet) {
        SwingUtilities.invokeLater(() -> {
            if (!privatePanelMap.containsKey(packet.getReceiverId())) {
                return;
            }
            var privatePanel = privatePanelMap.get(packet.getReceiverId());
            privatePanel.onPrivateChatResp(packet);
        });
    }

    // 收到私聊消息
    // 1. 如果未打开，则新增tab，标题为 好友昵称<新消息>
    // 2. 如果已打开，直接追加消息
    public void onPrivateChatMessage(PrivateChatMessage packet) {
        SwingUtilities.invokeLater(() -> {
            var senderId = packet.getSenderId();
            PrivateChatPanel privatePanel;
            // 本人
            if (senderId == currentUserId) {
                privatePanel = privatePanelMap.get(packet.getReceiverId());
                privatePanel.onPrivateChatMessage(packet);
                return;
            }
            // 新用户找我私聊
            if (!privatePanelMap.containsKey(senderId)) {
                // 新增tab
                privatePanel = new PrivateChatPanel(senderId);
                tabPublic.addTab(String.format("%s<新消息>", packet.getNickname()), privatePanel.getPanel());
                tabFriendNicknameMap.put(tabPublic.getTabCount() - 1, packet.getNickname());
                privatePanelMap.put(senderId, privatePanel);
                privatePanel.onPrivateChatMessage(packet);
                return;
            }
            // 已打开的聊天
            if (privatePanelMap.containsKey(senderId)) {
                privatePanel = privatePanelMap.get(senderId);
                privatePanel.onPrivateChatMessage(packet);
                // 如果发消息的好友tab未激活，则添加 <新消息> 标识
                var senderTabIndex = tabPublic.indexOfComponent(privatePanel.getPanel());
                if (senderTabIndex != tabPublic.getSelectedIndex()) {
                    tabPublic.setTitleAt(senderTabIndex, String.format("%s<新消息>", packet.getNickname()));
                }
            }
        });
    }

    // 打开私聊
    private void handleOpenPrivateChat(FriendListResp.Friend friend) {
        if (privatePanelMap.containsKey(friend.getId())) {
            // 激活panel
            tabPublic.setSelectedComponent(privatePanelMap.get(friend.getId()).getPanel());
            return;
        }
        var privatePanel = new PrivateChatPanel(friend.getId());
        tabPublic.addTab(friend.getNickname(), privatePanel.getPanel());
        tabPublic.setSelectedComponent(privatePanel.getPanel());
        privatePanelMap.put(friend.getId(), privatePanel);
        tabFriendNicknameMap.put(tabPublic.getTabCount() - 1, friend.getNickname());
    }
}
