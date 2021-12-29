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

/**
 * Common, shared resources and methods by modules
 */
public class Common {

    private static JavaPlugin plugin;

    private static final String ONLY_PLAYERS_CAN_USE_COMMAND_PREFIX = applyDefaultMessageColor("Only players can use " + DEFAULT_COMMAND_COLOR_CODE + AetherEssentials.COMMAND_PREFIX);
    private static final String NO_ARGS_EXPECTED = applyDefaultMessageColor("No arguments expected");
    private static final String EXACTLY_ONE_ARG_EXPECTED = applyDefaultMessageColor("Exactly one argument expected");
    private static final String NOT_AN_ONLINE_PLAYER = applyDefaultMessageColor("Not a valid online player");
    private static final String TOO_MANY_ARGS_PREFIX = applyDefaultMessageColor("To many arguments. Expected at most: " + DEFAULT_COMMAND_COLOR_CODE);
    private static final String MINIMUM_ARGS_PREFIX = applyDefaultMessageColor("Expected minimum args of: ");
    private static final String NOT_A_PLAYER_PREFIX = applyDefaultMessageColor("Not a known player: " + DEFAULT_COMMAND_COLOR_CODE);

    /**
     * Initialize the common resources
     *
     * @param plugin This plugin
     */
    public static void init(JavaPlugin plugin) {
        Common.plugin = plugin;
    }

    /**
     * @return This plugin
     */
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * @return The names of all online players
     */
    public static List<String> getOnlinePlayerNames() {
        List<String> names = new ArrayList<>();
        for (Player player : getOnlinePlayers()) {
            names.add(player.getName());
        }
        return names;
    }

    /**
     * @return The online players
     */
    public static List<Player> getOnlinePlayers() {
        return new ArrayList<>(plugin.getServer().getOnlinePlayers());
    }

    /**
     * @return The names of all online and offline Players
     */
    public static List<String> getAllPlayerNames() {
        List<String> names = new ArrayList<>();
        for (ServerOperator operator : getAllPlayers()) {
            names.add(getServerOperatorName(operator));
        }
        return names;
    }

    /**
     * The returned {@link ServerOperator}s will be either a {@link Player} or
     * an {@link OfflinePlayer}. The caller will have to use 'instanceof' and
     * cast to the appropriate type as needed.
     *
     * @return All online and offline Players
     */
    public static List<ServerOperator> getAllPlayers() {
        List<ServerOperator> allPlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        allPlayers.addAll(List.of(plugin.getServer().getOfflinePlayers()));

        return allPlayers;
    }

    /**
     * @param commandSender The sender of a command
     * @param commandLabel  The command label
     * @return True if the sender is a {@link Player}, otherwise false
     */
    public static Optional<Player> verifyCommandSenderIsPlayer(CommandSender commandSender, String commandLabel) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ONLY_PLAYERS_CAN_USE_COMMAND_PREFIX + commandLabel);
            return Optional.empty();
        }
        return Optional.of(player);
    }

    /**
     * @param commandSender The sender of a command
     * @param arg           The String that may be the name of a {@link Player}
     * @return True if the arg is the name of an online Player, otherwise false
     */
    public static Optional<Player> verifyArgIsOnlinePlayer(CommandSender commandSender, String arg) {
        Player player = plugin.getServer().getPlayer(arg);
        if (player == null) {
            commandSender.sendMessage(NOT_AN_ONLINE_PLAYER);
            return Optional.empty();
        }
        return Optional.of(player);
    }

    /**
     * @param commandSender The sender of a command
     * @param arg           The String that may be the name of a {@link Player}
     * @return True if the arg is the name of an online or offline Player, otherwise false
     */
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
            commandSender.sendMessage(NOT_A_PLAYER_PREFIX + arg);
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

    /**
     * @param commandSender The sender of a command
     * @param args          The arguments passed to the command
     * @param limit         The maximum number of allowed args
     * @return True if there are equal or fewer arguments than the limit, otherwise false
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean verifyUpToNumberOfArgs(CommandSender commandSender, String[] args, int limit) {
        if (args.length > limit) {
            commandSender.sendMessage(TOO_MANY_ARGS_PREFIX);
            return false;
        }
        return true;
    }

    /**
     * @param commandSender The sender of a command
     * @param args          The arguments passed to the command
     * @return True if there is exactly one argument, otherwise false
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean verifyExactlyOneArg(CommandSender commandSender, String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage(EXACTLY_ONE_ARG_EXPECTED);
            return false;
        }
        return true;
    }

    /**
     * @param commandSender The sender of a command
     * @param args          The arguments passed to the command
     * @return True if there are no arguments, otherwise false
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean verifyNoArgs(CommandSender commandSender, String[] args) {
        if (args.length != 0) {
            commandSender.sendMessage(NO_ARGS_EXPECTED);
            return false;
        }
        return true;
    }

    /**
     * @param partial The potential beginning of an online Player's name
     * @return A list of names of online Players that start with the partial
     */
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

    /**
     * @param commandSender The sender of a command
     * @param args          The arguments passed to the command
     * @param minimumArgs   The minimum amount of arguments expected
     * @return True if there are at least the minimum amount of arguments, otherwise false
     */
    public static boolean verifyMinimumArgs(CommandSender commandSender, String[] args, int minimumArgs) {
        if (args.length < minimumArgs) {
            commandSender.sendMessage(MINIMUM_ARGS_PREFIX + minimumArgs);
            return false;
        }
        return true;
    }

}
