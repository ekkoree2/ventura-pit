package eu.ventura.renown;

import eu.ventura.event.PitKillEvent;
import eu.ventura.event.PitRespawnEvent;
import eu.ventura.model.PlayerModel;
import eu.ventura.model.RenownTierModel;
import eu.ventura.perks.Perk;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
@Getter
public abstract class RenownShop {
    private final String id;
    private final String displayName;
    private final RenownCategory category;
    private final Material icon;
    private final List<RenownTierModel> tiers;

    protected RenownShop(RenownCategory category, Material icon, RenownTierModel... tiers) {
        this(null, null, category, icon, tiers);
    }

    protected RenownShop(String displayName, RenownCategory category, Material icon, RenownTierModel... tiers) {
        this(null, displayName, category, icon, tiers);
    }

    protected RenownShop(String id, String displayName, RenownCategory category, Material icon, RenownTierModel... tiers) {
        this.id = id != null ? id : generateId();
        this.displayName = displayName != null ? displayName : generateDisplayName();
        this.category = category;
        this.icon = icon;
        this.tiers = Arrays.asList(tiers);
    }

    protected RenownShop(List<RenownTierModel> tiers) {
        this.id = generateId();
        this.displayName = generateDisplayName();
        this.category = null;
        this.icon = null;
        this.tiers = tiers;
    }

    private String generateId() {
        String name = this.getClass().getSimpleName();
        return name.replaceAll("(?<!^)([A-Z])", "-$1").toLowerCase();
    }

    private String generateDisplayName() {
        String name = this.getClass().getSimpleName();
        return name.replaceAll("([A-Z])", " $1").trim();
    }

    public ItemStack getIcon(PlayerModel playerModel) {
        return icon != null ? new ItemStack(icon) : getIconOverride(playerModel);
    }

    protected ItemStack getIconOverride(PlayerModel playerModel) {
        return null;
    }

    public String getCurrentBoost(int tier) {
        return null;
    }

    public Perk getPerkInstance() {
        return null;
    }

    public List<String> getMaxedTierInfo(int tier) {
        return getTier(tier).getLore();
    }

    public RenownTierModel getTier(int tierLevel) {
        return tiers.stream()
                .filter(t -> t.getTier() == tierLevel)
                .findFirst()
                .orElse(null);
    }

    public void onRespawn(PitRespawnEvent event, int level) {
    }

    public void onKill(PitKillEvent event, int tierLevel) {
    }

    public void onGoldPickup(PlayerModel playerModel, double[] amount, int tier) {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RenownShop other)) return false;
        return getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    protected static RenownTierModel tier(int tier, int renown, int prestige, String... lore) {
        return new RenownTierModel(tier, renown, prestige, Arrays.asList(lore));
    }

    protected static RenownTierModel tier(int tier, int renown, int prestige, List<String> lore) {
        return new RenownTierModel(tier, renown, prestige, lore);
    }

    protected static RenownTierModel[] repeatTiers(List<String> lore, TierData... data) {
        RenownTierModel[] tiers = new RenownTierModel[data.length];
        for (int i = 0; i < data.length; i++) {
            tiers[i] = new RenownTierModel(data[i].tier, data[i].renown, data[i].prestige, lore);
        }
        return tiers;
    }

    protected static TierData t(int tier, int renown, int prestige) {
        return new TierData(tier, renown, prestige);
    }

    @Getter
    protected static class TierData {
        private final int tier;
        private final int renown;
        private final int prestige;

        private TierData(int tier, int renown, int prestige) {
            this.tier = tier;
            this.renown = renown;
            this.prestige = prestige;
        }
    }
}
