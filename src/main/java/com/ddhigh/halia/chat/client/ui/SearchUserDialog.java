package com.ddhigh.halia.chat.client.ui;

import com.ddhigh.halia.chat.client.ThreadPool;
import com.ddhigh.halia.chat.client.io.DefaultChatListener;
import com.ddhigh.halia.chat.client.io.packet.FriendApplyResp;
import com.ddhigh.halia.chat.client.io.packet.SearchUserResp;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SearchUserDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<String> userList;
    private final DefaultComboBoxModel<String> userListModel = new DefaultComboBoxModel<>();
    private final List<SearchUserResp.User> users = new ArrayList<>();
    private JTextField nicknameField;
    private final JPopupMenu popupMenu = new JPopupMenu();

    public SearchUserDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        DefaultChatListener.shared().setSearchUserDialog(this);
        setupUI();
        setupEvents();
    }

    private void setupUI() {
        userList.setModel(userListModel);
        var menuItem = new JMenuItem("加为好友");
        menuItem.addActionListener(e -> {
            var selectedIndex = userList.getSelectedIndex();
            doFriendApply(selectedIndex);
        });
        popupMenu.add(menuItem);
    }

    private void doFriendApply(int selectedIndex) {
        var userId = users.get(selectedIndex).getId();
        var reason = JOptionPane.showInputDialog(null, "请输入申请理由");
        ThreadPool.submit(() -> DefaultChatListener.shared().doFriendApply(userId, reason));
    }

    private void setupEvents() {
        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3 && userList.getSelectedIndex() >= 0) {
                    popupMenu.show(userList, e.getX(), e.getY());
                }
            }
        });
    }

    private void onOK() {
        var nickname = nicknameField.getText().trim();
        if (nickname.isEmpty()) {
            return;
        }
        buttonOK.setEnabled(false);
        ThreadPool.submit(() -> DefaultChatListener.shared().doSearchUser(nickname));
    }

    private void onCancel() {
        // add your code here if necessary
        DefaultChatListener.shared().setSearchUserDialog(null);
        dispose();
    }

    public void onSearchUserResp(SearchUserResp resp) {
        SwingUtilities.invokeLater(() -> {
            buttonOK.setEnabled(true);
            if (resp.getCode() != 0) {
                JOptionPane.showMessageDialog(null, resp.getMessage());
                return;
            }
            userListModel.removeAllElements();
            users.clear();
            users.addAll(resp.getList());
            for (SearchUserResp.User user : resp.getList()) {
                userListModel.addElement(String.format("%s<%d>", user.getNickname(), user.getId()));
            }
        });
    }

    public void onFriendApplyResp(FriendApplyResp packet) {
        SwingUtilities.invokeLater(() -> {
            if (packet.getCode() != 0) {
                JOptionPane.showMessageDialog(null, packet.getMessage());
                return;
            }
            JOptionPane.showMessageDialog(null, packet.getMessage());
            onCancel();
        });
    }
}
