package io.codeovo.mpcommands;

import io.codeovo.mpcommands.config.MPCommandsConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class MagmaPayCommands extends JavaPlugin {
    private static MagmaPayCommands magmaPayCommands;

    private MPCommandsConfig mpCommandsConfig;

    @Override
    public void onEnable() {
        getLogger().info("MagmaPay: Commands - Enabling...");
        magmaPayCommands = this;

        mpCommandsConfig = new MPCommandsConfig(this);
        getLogger().info("MagmaPay: Commands - Enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MagmaPay: Commands - Disabling...");
        magmaPayCommands = null;

        getLogger().info("MagmaPay: Commands - Disabled.");
    }

    public static MagmaPayCommands getInstance() { return magmaPayCommands; }

    public MPCommandsConfig getCommandConfig() { return mpCommandsConfig; }
}
