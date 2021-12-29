package com.github.aetherialmist.aether.essentials.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatColorEventListener implements Listener {

    private final ChatColorFormatter colorFormatter;

    public ChatColorEventListener() {
        this.colorFormatter = ChatColorFormatter.getInstance();
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatEvent(AsyncChatEvent event) {
        if (!event.isAsynchronous()) {
            return;
        }

        if (!(event.message() instanceof TextComponent textComponent)) {
            return;
        }

        String message = textComponent.content();
        TextComponent formattedMessage = textComponent.content(colorFormatter.applyColorFormat(message));
        event.message(formattedMessage);
    }

}
