package com.github.aetherialmist.aether.essentials;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.DEFAULT_COMMAND_COLOR_CODE;
import static com.github.aetherialmist.aether.essentials.chat.ChatColorFormatter.applyDefaultMessageColor;

public class Common {

    private static JavaPlugin plugin;

    private static String onlyPlayersCanUseCommandPrefix;
    private static String noArgsExpected;
    private static String exactlyOneArgExpected;
    private static String notAnOnlinePlayer;
    private static String tooManyArgsPrefix;
    private static String minimumArgsPrefix;
    private static String notAPlayerPrefix;

    public static void init(JavaPlugin plugin) {
        Common.plugin = plugin;
        onlyPlayersCanUseCommandPrefix = applyDefaultMessageColor("Only players can use " + DEFAULT_COMMAND_COLOR_CODE + AetherEssentials.COMMAND_PREFIX);
        noArgsExpected = applyDefaultMessageColor("No arguments expected");
        exactlyOneArgExpected = applyDefaultMessageColor("Exactly one argument expected");
        notAnOnlinePlayer = applyDefaultMessageColor("Not a valid online player");
        tooManyArgsPrefix = applyDefaultMessageColor("To many arguments. Expected at most: " + DEFAULT_COMMAND_COLOR_CODE);
        minimumArgsPrefix = applyDefaultMessageColor("Expected minimum args of: ");
        notAPlayerPrefix = applyDefaultMessageColor("Not a known player: " + DEFAULT_COMMAND_COLOR_CODE);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static List<String> getOnlinePlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player player : getOnlinePlayers()) {
            names.add(player.getName());
        }
        return names;
    }

    public static List<Player> getOnlinePlayers() {
        return new ArrayList<>(plugin.getServer().getOnlinePlayers());
    }

    public static List<String> getAllPlayerNames() {
        List<String> names = new ArrayList<>();
        for (ServerOperator operator : getAllPlayers()) {
            names.add(getServerOperatorName(operator));
        }
        return names;
    }

    public static List<ServerOperator> getAllPlayers() {
        List<ServerOperator> allPlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        allPlayers.addAll(List.of(plugin.getServer().getOfflinePlayers()));

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

    public static Optional<ServerOperator> verifyArgIsPlayer(CommandSender commandSender, String arg) {
        List<ServerOperator> allPlayers = getAllPlayers();
        ServerOperator foundPlayer = null;

        for (ServerOperator operator : allPlayers) {
            String name = getServerOperatorName(operator);
            if (name != null && name.equals(arg)) {
                foundPlayer = operator;
                break;
            }
        }

        if (foundPlayer == null) {
            commandSender.sendMessage(notAPlayerPrefix + arg);
        }

        return Optional.ofNullable(foundPlayer);
    }

    private static String getServerOperatorName(ServerOperator operator) {
        String name = null;
        if (operator instanceof Player player) {
            name = player.getName();
        } else if (operator instanceof OfflinePlayer offlinePlayer) {
            name = offlinePlayer.getName();
        }
        return name;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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

    private Common() {

    }

    public static boolean verifyMinimumArgs(CommandSender commandSender, String[] args, int minimumArgs) {
        if (args.length < minimumArgs) {
            commandSender.sendMessage(minimumArgsPrefix + minimumArgs);
            return false;
        }
        return true;
    }

}
