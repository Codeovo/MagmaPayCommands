package io.codeovo.mpcommands;

import io.codeovo.mpcommands.commands.BuyCommand;
import io.codeovo.mpcommands.config.MPCommandsConfig;
import io.codeovo.mpcommands.purchases.PurchaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MagmaPayCommands extends JavaPlugin {
    private MPCommandsConfig mpCommandsConfig;
    private PurchaseManager purchaseManager;

    @Override
    public void onEnable() {
        getLogger().info("MagmaPay: Commands - Enabling...");
        purchaseManager = new PurchaseManager();
        mpCommandsConfig = new MPCommandsConfig(this);

        getCommand("mbuy").setExecutor(new BuyCommand(this));
        getLogger().info("MagmaPay: Commands - Enabled.");
    }

    public MPCommandsConfig getCommandConfig() { return mpCommandsConfig; }

    public PurchaseManager getPurchaseManager() { return purchaseManager; }
}
