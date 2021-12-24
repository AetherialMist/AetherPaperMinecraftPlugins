package aetherial.aether.essentials.teleportation.command.tp;

import aetherial.aether.essentials.AetherEssentials;
import aetherial.aether.essentials.Common;
import aetherial.aether.essentials.exception.AlreadyInitialized;
import aetherial.aether.essentials.teleportation.TpRegistration;
import aetherial.aether.essentials.wrapper.CommandWrapper;
import aetherial.spigot.plugin.annotation.command.CommandTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static aetherial.aether.essentials.chat.ChatColorFormatter.*;

@CommandTag(
    name = TpRegistration.TP_TOGGLE,
    usage = AetherEssentials.COMMAND_PREFIX + TpRegistration.TP_TOGGLE,
    desc = "Toggle [allow] or [auto-deny] teleport requests",
    permission = TpToggle.PERMISSION
)
public class TpToggle extends CommandWrapper {

    public static final String PERMISSION = AetherEssentials.PERMISSION_BASE + TpRegistration.TP_TOGGLE;

    private final String failedToggle = applyColor("Failed to toggle auto-deny");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Optional<Player> optionalPlayer = Common.verifyCommandSenderIsPlayer(commandSender, label);
        if (optionalPlayer.isEmpty() || !Common.verifyNoArgs(commandSender, args)) {
            return false;
        }
        Player sender = optionalPlayer.get();

        boolean autoDenyEnabled = autoDenyEnabledList.contains(sender.getUniqueId());
        boolean changedEnabled = autoDenyEnabled ? autoDenyEnabledList.remove(sender.getUniqueId()) : autoDenyEnabledList.add(sender.getUniqueId());

        if (!changedEnabled) {
            sender.sendMessage(failedToggle);
            return true;
        }

        sender.sendMessage(senderMessagePrefix + !autoDenyEnabled);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] strings) {
        return Collections.emptyList();
    }

    //==========================================================================

    private static TpToggle instance;
    private static final List<UUID> autoDenyEnabledList = Collections.synchronizedList(new ArrayList<>());

    public static TpToggle getInstance() {
        if (instance == null) {
            throw new NullPointerException("TpToggle has not yet been initialized.");
        }
        return instance;
    }

    public static void init() {
        if (instance != null) {
            throw new AlreadyInitialized("TpToggle has already been initialized");
        }
        instance = new TpToggle();
    }

    private final String senderMessagePrefix = applyColor(DEFAULT_MESSAGE_COLOR_CODE + "Tp request auto-deny set to: " + DEFAULT_COMMAND_COLOR_CODE);

    /**
     * @param player The player to check if they have auto-deny enabled
     * @return True if the player has enabled auto-deny, otherwise false
     */
    public boolean getAutoDenyEnabled(Player player) {
        return autoDenyEnabledList.contains(player.getUniqueId());
    }

}
