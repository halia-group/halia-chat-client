package com.ddhigh.halia.chat.client.io.protocol;

public enum FriendStatus {
    APPLY(0),
    ACCEPT(1),
    REJECT(2);
    private final int value;

    FriendStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static FriendStatus valueOf(int value) {
        for (var status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
