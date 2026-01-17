package eu.ventura.service;

import eu.ventura.perks.Perk;
import eu.ventura.perks.permanent.GravityMace;

import java.util.*;

public class PerkService {

    private static final Map<String, Perk> perks = new HashMap<>();

    static {
        registerPerk(new GravityMace());
    }

    public static void registerPerk(Perk perk) {
        perks.put(perk.getId(), perk);
    }

    public static Perk getPerk(String id) {
        return perks.get(id);
    }

    public static <T extends Perk> T getPerk(Class<T> clazz) {
        for (Perk perk : perks.values()) {
            if (clazz.isInstance(perk)) {
                return clazz.cast(perk);
            }
        }
        return null;
    }

    public static Collection<Perk> getPerks() {
        return perks.values();
    }

    public static List<Perk> getSortedPerks() {
        List<Perk> sorted = new ArrayList<>(perks.values());
        sorted.sort(Comparator.comparingInt(Perk::getPrestigeRequirement)
                .thenComparing(Perk::getDisplayName));
        return sorted;
    }
}
