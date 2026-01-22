package eu.ventura.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
@Getter
@RequiredArgsConstructor
public enum NBTTag {
    MYSTIC_ITEM("pit-mystic-item"),
    MYSTIC_TIER("pit-mystic-tier"),
    MYSTIC_LIVES("pit-mystic-lives"),
    MYSTIC_MAX_LIVES("pit-mystic-max-lives"),
    PANTS_COLOR("pit-pants-color"),
    ENCHANT_PREFIX("pit-enchant-", true),
    DEFAULT_ITEM("pit-default-item"),
    PERK_ITEM("pit-perk-item"),
    EVENT_ITEM("pit-event-item"),
    RESTRICTED_ITEM("pit-restricted-item");

    private final String value;
    private final boolean isPrefix;

    NBTTag(String value) {
        this(value, false);
    }

    public String of(String suffix) {
        if (!isPrefix) {
            throw new IllegalStateException("Only prefix tags can use of()");
        }
        return value + suffix;
    }
}
