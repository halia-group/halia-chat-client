package com.ddhigh.halia.chat.client.io.protocol;

public enum MsgType {
    TEXT(1);
    private final int value;

    MsgType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static MsgType valueOf(int value) {
        for (var msgType : MsgType.values()) {
            if (msgType.value == value) {
                return msgType;
            }
        }
        return null;
    }
}
