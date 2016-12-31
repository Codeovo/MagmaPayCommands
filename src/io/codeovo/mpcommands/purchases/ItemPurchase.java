package io.codeovo.mpcommands.purchases;

public class ItemPurchase {
    private int purchasePrice;
    private int quantity;

    public ItemPurchase(int purchasePrice, int quantity) {
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
    }

    public int getPurchasePrice() { return purchasePrice; }

    public int getQuantity() { return quantity; }
}
