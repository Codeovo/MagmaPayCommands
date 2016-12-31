package io.codeovo.mpcommands.commands;

import io.codeovo.magmapay.MagmaPay;
import io.codeovo.magmapay.objects.charges.ChargeRequest;
import io.codeovo.magmapay.objects.charges.ChargeResponse;
import io.codeovo.mpcommands.MagmaPayCommands;
import io.codeovo.mpcommands.utils.GeneralUtils;

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
                    int quantity;

                    try {
                        quantity = Integer.parseInt(strings[1]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(magmaPayCommands.getCommandConfig().getNotNumber());
                        return true;
                    }

                    String item = strings[1].toUpperCase();

                    if (!magmaPayCommands.getPurchaseManager().itemExists(item)) {
                        p.sendMessage(magmaPayCommands.getCommandConfig().getInvalidItem());
                        return true;
                    }

                    int totalPrice = magmaPayCommands.getPurchaseManager().getItemPrice(item) * quantity;
                    String formattedPrice = GeneralUtils.getFormattedAmount(totalPrice);

                    String stripeDescriptor = magmaPayCommands.getCommandConfig().getStripeDescription()
                            .replace("<player>", p.getDisplayName()).replace("<item>", item)
                            .replace("<quantity>", String.valueOf(quantity));
                    String statementDescriptor = magmaPayCommands.getCommandConfig().getStatementDescription()
                            .replace("<player>", p.getDisplayName()).replace("<item>", item)
                            .replace("<quantity>", String.valueOf(quantity));

                    p.sendMessage(magmaPayCommands.getCommandConfig().getPaymentInitiated()
                            .replace("<price>", formattedPrice));

                    ChargeResponse chargeResponse = MagmaPay.getMagmaPayAPI().chargePlayer(new ChargeRequest(p,
                            totalPrice, magmaPayCommands.getCommandConfig().getCurrencyCode(), true,
                            stripeDescriptor, statementDescriptor, null));

                    if (chargeResponse.getStatus().equalsIgnoreCase("succeeded")) {
                        p.sendMessage(magmaPayCommands.getCommandConfig().getChargeSuccessful());

                        // GIVE ITEMS
                    } else {
                        if (chargeResponse.getEarlyFailStatus() != null) {
                            p.sendMessage(magmaPayCommands.getCommandConfig().getChargeFailed()
                                    .replace("<reason>", chargeResponse.getEarlyFailStatus().toString()));
                        } else {
                            p.sendMessage(magmaPayCommands.getCommandConfig().getChargeFailed()
                                    .replace("<reason>", chargeResponse.getFailureMessage()));
                        }
                    }

                    return true;
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
