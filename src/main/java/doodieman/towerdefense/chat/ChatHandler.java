package doodieman.towerdefense.chat;

import lombok.Getter;

public class ChatHandler {

    @Getter
    private final ChatListener listener;

    public ChatHandler() {
        this.listener = new ChatListener(this);
    }

}
