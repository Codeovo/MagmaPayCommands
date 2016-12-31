package io.codeovo.mpcommands.config;

import io.codeovo.mpcommands.MagmaPayCommands;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MPCommandsConfig {
    private MagmaPayCommands magmaPayCommands;
    private FileConfiguration config;

    private String currencyCode;

    private String stripeDescription;
    private String statementDescription;

    private String chargeSuccessful;
    private String chargeFailed;

    private String notNumber;
    private String invalidItem;
    private String incorrectSyntax;
    private String noPermission;

    public MPCommandsConfig(MagmaPayCommands magmaPayCommands) {
        this.magmaPayCommands = magmaPayCommands;

        this.magmaPayCommands.saveDefaultConfig();
        this.config = magmaPayCommands.getConfig();

        loadConfig();
    }

    private void loadConfig() {
        currencyCode = config.getString("general.currency");

        stripeDescription = config.getString("messages.stripe-description");
        statementDescription = config.getString("messages.statement-descriptor");

        chargeSuccessful = colourString(config.getString("messages.charge-successful"));
        chargeFailed = colourString(config.getString("messages.charge-failed"));

        notNumber = colourString(config.getString("messages.not-number"));
        invalidItem = colourString(config.getString("messages.invalid-item"));
        incorrectSyntax = colourString(config.getString("messages.incorrect-syntax"));
        noPermission = colourString(config.getString("messages.no-permission"));
    }

    public String getCurrencyCode() { return currencyCode; }

    public String getStripeDescription() { return stripeDescription; }

    public String getStatementDescription() { return statementDescription; }

    public String getChargeSuccessful() { return chargeSuccessful; }

    public String getChargeFailed() { return chargeFailed; }

    public String getNotNumber() { return notNumber; }

    public String getInvalidItem() { return invalidItem; }

    public String getIncorrectSyntax() { return incorrectSyntax; }

    public String getNoPermission() { return noPermission; }

    private String colourString(String toColour) {
        return ChatColor.translateAlternateColorCodes('&', toColour);
    }
}
