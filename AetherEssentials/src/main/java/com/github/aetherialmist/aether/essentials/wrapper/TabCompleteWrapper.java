package com.github.aetherialmist.aether.essentials.wrapper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

/**
 * Wraps the {@link TabCompleter} to avoid the need for all implementations
 * to include the @NotNull annotations on method arguments, and clearly define
 * what each argument represents.
 */
public interface TabCompleteWrapper extends TabCompleter {

    /**
     * @param commandSender The sender of the command
     * @param command       The Command
     * @param commandLabel  The label of the Command
     * @param args          The arguments passed to the command
     * @return A list of possible completions for the argument(s) passed in
     */
    @SuppressWarnings("NullableProblems")
    List<String> onTabComplete(CommandSender commandSender, Command command, String commandLabel, String[] args);

}
