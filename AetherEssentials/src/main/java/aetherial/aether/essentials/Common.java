package aetherial.aether.essentials;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static aetherial.aether.essentials.chat.ChatColorFormatter.DEFAULT_COMMAND_COLOR_CODE;
import static aetherial.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

public class Common {

    private Common() {

    }

    private static JavaPlugin plugin;

    private static String onlyPlayersCanUseCommandPrefix;
    private static String noArgsExpected;
    private static String exactlyOneArgExpected;
    private static String notAnOnlinePlayer;
    private static String tooManyArgsPrefix;

    public static void init(JavaPlugin plugin) {
        Common.plugin = plugin;
        onlyPlayersCanUseCommandPrefix = applyDefaultMessageColor("Only players can use " + DEFAULT_COMMAND_COLOR_CODE + AetherEssentials.COMMAND_PREFIX);
        noArgsExpected = applyDefaultMessageColor("No arguments expected");
        exactlyOneArgExpected = applyDefaultMessageColor("Exactly one argument expected");
        notAnOnlinePlayer = applyDefaultMessageColor("Not a valid online player");
        tooManyArgsPrefix = applyDefaultMessageColor("To many arguments. Expected at most: " + DEFAULT_COMMAND_COLOR_CODE);
    }

    public static List<Player> getOnlinePlayers() {
        return new ArrayList<>(plugin.getServer().getOnlinePlayers());
    }

    public static List<String> getOnlinePlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player player : getOnlinePlayers()) {
            names.add(player.getName());
        }
        return names;
    }

    public static List<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        OfflinePlayer[] offlinePlayers = plugin.getServer().getOfflinePlayers();

        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            allPlayers.add(offlinePlayer.getPlayer());
        }

        return allPlayers;
    }

    public static Optional<Player> verifyCommandSenderIsPlayer(CommandSender commandSender, String commandLabel) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(onlyPlayersCanUseCommandPrefix + commandLabel);
            return Optional.empty();
        }
        return Optional.of(player);
    }

    public static Optional<Player> verifyArgIsOnlinePlayer(CommandSender commandSender, String arg) {
        Player player = plugin.getServer().getPlayer(arg);
        if (player == null) {
            commandSender.sendMessage(notAnOnlinePlayer);
            return Optional.empty();
        }
        return Optional.of(player);
    }

    public static boolean verifyUpToNumberOfArgs(CommandSender commandSender, String[] args, int limit) {
        if (args.length > limit) {
            commandSender.sendMessage(tooManyArgsPrefix);
            return false;
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean verifyExactlyOneArg(CommandSender commandSender, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage(exactlyOneArgExpected);
            return false;
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean verifyNoArgs(CommandSender commandSender, String[] args) {
        if (args.length != 0) {
            commandSender.sendMessage(noArgsExpected);
            return false;
        }
        return true;
    }

    public static List<String> getOnlinePlayerNamesStartsWith(String partial) {
        List<String> names = getOnlinePlayerNames();
        List<String> possibleNames = new ArrayList<>();

        for (String name : names) {
            if (name.startsWith(partial)) {
                possibleNames.add(name);
            }
        }

        return possibleNames;
    }



}
