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
    UnAuthorization(10);

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
