package com.github.aetherialmist.aether.essentials.wrapper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Wraps the {@link CommandExecutor} to avoid the need for all implementations
 * to include the @NotNull annotations on method arguments, and clearly define
 * what each argument represents.
 */
public abstract class CommandWrapper implements CommandExecutor, TabCompleteWrapper {

    /**
     * @param commandSender The source that sent the command
     * @param command       The Command
     * @param commandLabel  The label of the Command
     * @param args          The arguments passed to the command
     * @return True if the command was successful, otherwise false
     */
    @SuppressWarnings({"java:S3038", "NullableProblems"})
    public abstract boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args);

}
