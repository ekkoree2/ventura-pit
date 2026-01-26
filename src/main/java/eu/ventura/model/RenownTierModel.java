package eu.ventura.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/24/2026
 */
@SuppressWarnings("all")
@Getter
@AllArgsConstructor
public class RenownTierModel {
    private final int tier;
    private final int renown;
    private final int prestige;
    private final List<String> lore;
}
