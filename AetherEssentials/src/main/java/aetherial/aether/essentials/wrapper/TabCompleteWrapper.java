package aetherial.aether.essentials.wrapper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public interface TabCompleteWrapper extends TabCompleter {

    @SuppressWarnings("NullableProblems")
    List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args);
}
