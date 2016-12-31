package io.codeovo.mpcommands.commands;

import io.codeovo.mpcommands.MagmaPayCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuyCommand implements CommandExecutor {
    private MagmaPayCommands magmaPayCommands;

    public BuyCommand(MagmaPayCommands magmaPayCommands) { this.magmaPayCommands = magmaPayCommands; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;

            if (p.hasPermission("mpcommands.use")) {
                if (strings.length == 2) {


                } else {
                    p.sendMessage(magmaPayCommands.getCommandConfig().getIncorrectSyntax());
                    return true;
                }
            } else {
                p.sendMessage(magmaPayCommands.getCommandConfig().getNoPermission());
                return true;
            }
        } else {
            commandSender.sendMessage("MagmaPay: Commands - You must be a player to use this command.");
            return true;
        }
    }
}
