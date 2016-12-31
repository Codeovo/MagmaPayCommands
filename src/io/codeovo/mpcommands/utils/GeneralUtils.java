package io.codeovo.mpcommands.utils;

public class GeneralUtils {
    public static String getFormattedAmount(int amount) {
        int cents = amount % 100;
        int dollars = (amount - cents) / 100;

        String cAmount;
        if (cents <= 9) {
            cAmount = 0 + "" + cents;
        } else {
            cAmount = "" + cents;
        }

        return dollars + "." + cAmount;
    }
}
