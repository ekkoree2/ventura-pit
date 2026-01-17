package eu.ventura.enchantment;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public enum EnchantRarity {
    COMMON(1, 1),
    RARE(2, 1),
    LEGENDARY(3, 2),
    MYTHIC(3, 3);

    public final int maxTier;
    public final int rarityValue;

    EnchantRarity(int maxTier, int rarityValue) {
        this.maxTier = maxTier;
        this.rarityValue = rarityValue;
    }
}
