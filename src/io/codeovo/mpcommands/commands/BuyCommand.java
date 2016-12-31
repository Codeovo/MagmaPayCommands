package io.codeovo.mpcommands.commands;

import io.codeovo.magmapay.MagmaPay;
import io.codeovo.magmapay.objects.charges.ChargeRequest;
import io.codeovo.magmapay.objects.charges.ChargeResponse;
import io.codeovo.mpcommands.MagmaPayCommands;
import io.codeovo.mpcommands.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuyCommand implements CommandExecutor {
    private MagmaPayCommands magmaPayCommands;

    public BuyCommand(MagmaPayCommands magmaPayCommands) { this.magmaPayCommands = magmaPayCommands; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;

            if (p.hasPermission("mpcommands.use") || !magmaPayCommands.getCommandConfig().isUsePermissions()) {
                if (strings.length == 2) {
                    int quantity;

                    try {
                        quantity = Integer.parseInt(strings[1]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(magmaPayCommands.getCommandConfig().getNotNumber());
                        return true;
                    }

                    String item = strings[0].toUpperCase();

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

                    Bukkit.getScheduler().runTaskAsynchronously(magmaPayCommands, () -> {
                        ChargeResponse chargeResponse = MagmaPay.getMagmaPayAPI().chargePlayer(new ChargeRequest(p,
                                totalPrice, magmaPayCommands.getCommandConfig().getCurrencyCode(), true,
                                stripeDescriptor, statementDescriptor, null));

                        Bukkit.getScheduler().runTask(magmaPayCommands, () -> {
                            boolean toContinue = true;

                            if (chargeResponse.getEarlyFailStatus() != null) {
                                p.sendMessage(magmaPayCommands.getCommandConfig().getChargeFailed()
                                        .replace("<reason>", chargeResponse.getEarlyFailStatus().toString()));

                                toContinue = false;
                            }

                            if (toContinue) {
                                if (chargeResponse.getStatus().equalsIgnoreCase("succeeded")) {
                                    p.sendMessage(magmaPayCommands.getCommandConfig().getChargeSuccessful());

                                    ItemStack toDrop;

                                    if (strings[0].contains(":")) {
                                        String[] dataSplit = strings[0].split(":");

                                        toDrop = new ItemStack(Material.valueOf(dataSplit[0].toUpperCase()), quantity,
                                                Short.valueOf(dataSplit[1]));
                                    } else {
                                        toDrop = new ItemStack(Material.valueOf(item), quantity);
                                    }

                                    if (p.getInventory().firstEmpty() == -1) {
                                        p.getLocation().getWorld().dropItem(p.getEyeLocation(), toDrop);
                                    } else {
                                        p.getInventory().addItem(toDrop);
                                        p.updateInventory();
                                    }
                                } else {
                                    p.sendMessage(magmaPayCommands.getCommandConfig().getChargeFailed()
                                            .replace("<reason>", chargeResponse.getFailureMessage()));
                                }
                            }
                        });

                    });

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
