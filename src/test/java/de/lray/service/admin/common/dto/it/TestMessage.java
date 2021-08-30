package de.lray.service.admin.common.dto.it;

public class TestMessage {
    public String message = null;

    public TestMessage() {
        /* make JSON mapper happy */
    }

    public TestMessage(String msg) {
        this.message = msg;
    }
}
