package eu.ventura.service;

import eu.ventura.perks.Perk;
import eu.ventura.perks.permanent.Dirty;
import eu.ventura.perks.permanent.GoldenApple;
import eu.ventura.perks.permanent.GoldenHeads;
import eu.ventura.perks.permanent.GravityMace;
import eu.ventura.perks.permanent.Ignition;
import eu.ventura.perks.permanent.LuckyDiamond;
import eu.ventura.perks.permanent.Momentum;
import eu.ventura.perks.permanent.Rambo;
import eu.ventura.perks.permanent.Streaker;
import eu.ventura.perks.permanent.Vampire;

import java.util.*;

public class PerkService {

    private static final Map<String, Perk> perks = new LinkedHashMap<>();

    static {
        addPerk(new GoldenApple());
        addPerk(new GoldenHeads());
        addPerk(new GravityMace());
        addPerk(new Momentum());
        //addPerk(new Calculated());
        addPerk(new LuckyDiamond());
        addPerk(new Ignition());
        addPerk(new Streaker());
        addPerk(new Vampire());
        addPerk(new Dirty());
        addPerk(new Rambo());
    }

    public static void addPerk(Perk perk) {
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
        return perks.values().stream().filter(p -> !p.isHidden()).toList();
    }
}
