package eu.ventura.model;

import lombok.Getter;

@Getter
public class PerkSlotModel {
    private final int slot;
    private final int requiredLevel;

    public PerkSlotModel(int slot, int requiredLevel) {
        this.slot = slot;
        this.requiredLevel = requiredLevel;
    }
}
