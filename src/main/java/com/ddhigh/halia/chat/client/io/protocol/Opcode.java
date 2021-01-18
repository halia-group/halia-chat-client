package com.ddhigh.halia.chat.client.io.protocol;

public enum Opcode {
    PING(1),
    PONG(2),
    RegisterReq(3),
    RegisterResp(4),
    LoginReq(5),
    LoginResp(6),
    PublicChatReq(7),
    PublicChatResp(8),
    PublicChatMessage(9),
    UnAuthorization(10),
    FriendListReq(11),
    FriendListResp(12),
    FriendApplyReq(13),
    FriendApplyResp(14),
    FriendApplyMessage(15),
    FriendApplyResultReq(16),
    FriendApplyResultResp(17),
    FriendApplyResultMessage(18),
    SearchUserReq(19),
    SearchUserResp(20);

    private final int value;

    Opcode(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static Opcode valueOf(int value) {
        for (var opcode : Opcode.values()) {
            if (opcode.value == value) {
                return opcode;
            }
        }
        return null;
    }
}
