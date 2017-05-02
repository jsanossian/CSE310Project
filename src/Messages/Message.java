package Messages;

import Constants.MessageType;
import java.io.Serializable;

public class Message implements Serializable {
    private MessageType type;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
