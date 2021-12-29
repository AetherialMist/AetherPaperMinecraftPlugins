package com.github.aetherialmist.aether.essentials.teleportation.command;

import com.github.aetherialmist.aether.essentials.AetherEssentials;
import com.github.aetherialmist.aether.essentials.teleportation.TeleportationRegistrar;
import com.github.aetherialmist.aether.essentials.wrapper.CommandWrapper;
import com.github.aetherialmist.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.*;

/**
 * Deny a teleport request
 */
@CommandTag(
    name = TeleportationRegistrar.TP_DENY,
    usage = AetherEssentials.COMMAND_PREFIX + TeleportationRegistrar.TP_DENY,
    desc = "Decline the most recent pending teleport request",
    permission = TpDeny.PERMISSION
)
public class TpDeny extends CommandWrapper {

    /**
     * The permission associated with this command
     */
    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TeleportationRegistrar.TP_DENY;

    private final String senderMessagePrefix = applyDefaultMessageColor("Your teleport request to " + DEFAULT_PLAYER_COLOR_CODE);
    private final String senderMessageSuffix = applyDefaultMessageColor(" was denied");
    private final String accepterMessagePrefix = applyDefaultMessageColor("You denied the teleport request from: " + DEFAULT_COMMAND_COLOR_CODE);

    // This should always return true
    @SuppressWarnings("java:S3516")
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        // Get the sender of the request (not this command's sender)
        Optional<Player> optional = TpRequest.replyToRequest(commandSender, commandLabel, args, false);
        if (optional.isEmpty()) {
            return true;
        }
        Player requestSender = optional.get();

        commandSender.sendMessage(accepterMessagePrefix + requestSender.getName());
        requestSender.sendMessage(senderMessagePrefix + commandSender.getName() + senderMessageSuffix);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args) {
        return Collections.emptyList();
    }

}
