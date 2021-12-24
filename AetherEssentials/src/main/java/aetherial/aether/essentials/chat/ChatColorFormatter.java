package aetherial.aether.essentials.chat;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Supports using the {@value #FORMAT_CHAR} as well as the {@value ChatColor#COLOR_CHAR} for text formatting.
 */
public class ChatColorFormatter {

    public static final char FORMAT_CHAR = '&';
    public static final String DEFAULT_COMMAND_COLOR_CODE = FORMAT_CHAR + "" + ChatColor.DARK_RED.getChar();
    public static final String DEFAULT_MESSAGE_COLOR_CODE = FORMAT_CHAR + "" + ChatColor.GOLD.getChar();
    public static final String DEFAULT_PLAYER_COLOR_CODE = FORMAT_CHAR + "" + ChatColor.YELLOW.getChar();

    private static ChatColorFormatter instance;

    @SuppressWarnings("unused")
    private static final Pattern sectionSymbolFormatPattern = Pattern.compile(ChatColor.COLOR_CHAR + "+([0-9a-fk-orA-FK-OR])");
    private static final Pattern ampersandSymbolFormatPattern = Pattern.compile("(&)?&([0-9a-fk-orA-FK-OR])"); // Double '&&' means escaped '&'
    private static final List<Character> validFormatCodes = new ArrayList<>();

    public static ChatColorFormatter getInstance() {
        if (ChatColorFormatter.instance == null) {
            ChatColorFormatter.instance = new ChatColorFormatter();
        }
        return ChatColorFormatter.instance;
    }

    public static String applyColor(String message) {
        return ChatColorFormatter.getInstance().applyColorFormat(message);
    }

    public static String applyDefaultMessageColor(String message) {
        return ChatColorFormatter.getInstance().applyColorFormat(DEFAULT_MESSAGE_COLOR_CODE + message);
    }

    private ChatColorFormatter() {
        // ChatColor.class contains ALL format options, not just colors...
        for (ChatColor color : EnumSet.allOf(ChatColor.class)) {
            validFormatCodes.add(color.getChar());
        }
    }

    public String applyColorFormat(String message) {
        StringBuilder formattedMessageBuilder = new StringBuilder();
        Matcher formatMatches = ampersandSymbolFormatPattern.matcher(message);

        while (formatMatches.find()) {
            boolean escaped = formatMatches.group(1) != null;
            if (!escaped) {
                char code = formatMatches.group(2).toLowerCase().charAt(0);
                if (validFormatCodes.contains(code)) {
                    // Example: replace and append '&c' to '§c'
                    // '$2' refers to group 2 of the Matcher's current match
                    formatMatches.appendReplacement(formattedMessageBuilder, ChatColor.COLOR_CHAR + "$2");
                }
            } else {
                formatMatches.appendReplacement(formattedMessageBuilder, "&$2");
            }
        }
        formatMatches.appendTail(formattedMessageBuilder);

        return formattedMessageBuilder.toString();
    }

}
