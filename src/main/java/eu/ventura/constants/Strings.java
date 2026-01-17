package eu.ventura.constants;

import eu.ventura.model.PlayerModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class Strings {
    @RequiredArgsConstructor
    @Getter
    public enum Language {
        POLISH("pl"), ENGLISH("en");

        private final String shortcut;

        public String from(String s, int count) {
            return switch (this) {
                case ENGLISH -> (count == 1 ? s : s + "s");
                case POLISH -> {
                    int mod10 = count % 10;
                    int mod100 = count % 100;
                    String root = s.substring(0, s.length() - 1);
                    String ending = s.substring(s.length() - 1);

                    if (count == 1) yield s;
                    if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) {
                        switch (ending) {
                            case "a" -> {
                                yield root + "y";
                            }
                            case "k" -> {
                                yield root + "ki";
                            }
                            case "g" -> {
                                yield root + "gi";
                            }
                        }
                        yield s + "y";
                    }
                    yield root + "ów";
                }
            };
        }
    }

    public enum Simple {
        DEATH("&c&lŚMIERC!", "&c&lDEATH!"),
        MAXED("§fXP: §bᴍᴀᴋѕʏᴍᴀʟɴʏ", "§fXP: §bᴍᴀхᴇᴅ"),
        CRATER_EXTRA("&aʜɪᴛʏ ᴏᴅʙɪᴊᴀᴊᴀ ᴡ ɢᴏʀᴇ!", "&aʜɪᴛѕ ᴘᴜɴᴄʜ ᴜᴘᴡᴀʀᴅѕ!"),
        RARE_PREFIX("&dʀᴀʀᴇ "),
        CRIT("&9&lKRYT!", "&9&lCRIT!"),
        KILL_1("§a§lZABÓJSTWO!", "§a§lKILL!"),
        KILL_2("§a§lPODWÓJNE ZABÓJSTWO!", "§a§lDOUBLE KILL!"),
        KILL_3("§a§lPOTRÓJNE ZABÓJSTWO!", "§a§lTRIPLE KILL!"),
        KILL_4("§a§lZWYKLE ZABÓJSTWO!", "§a§lQUADRA KILL!"),
        KILL_5("§a§lPIĘCIOKROTNE ZABÓJSTWO!", "§a§lPENTA KILL!"),
        KILL_MULTI("§a§lWIELE ZABÓJSTW! §7({0})", "§a§lMULTI KILL! §7({0})"),
        CANT_RESPAWN_HERE("§cNie mozesz /respawn tutaj!", "§cYou cannot /respawn here!"),
        RESPAWN_COOLDOWN("§cMozesz uzyc /respawn co 10 sekund!", "§cYou may only /respawn every 10 seconds!"),
        GRAVITY_MACE("&7Zadawaj &c+10%&7 obrazen podczas spadania.", "&7Deal &c+10%&7 damage while falling."),
        PERK_BACK("&7&lWRÓĆ", "&7&lBACK"),
        PERK_NO_PERK("&7Brak zainstalowanego perka", "&7No perk equipped"),
        PERK_SLOT_LOCKED_MSG("&cSlot nie jest jeszcze odblokowany!", "&cSlot not unlocked yet!"),
        PERK_TOO_LOW_LEVEL("&cJesteś zbyt niskiego poziomu aby zakupić ten perk!", "&cYou are too low level to acquire this perk!"),
        PERK_NOT_ENOUGH_GOLD("&cNie masz wystarczająco dużo złota aby zakupić ten perk!", "&cYou don't have enough gold to afford this!"),
        PERK_ALREADY_SELECTED("&cTen perk jest już wybrany!", "&cThis perk is already selected!"),
        PERK_LEVEL_10_REQUIRED("&cMusisz mieć poziom 10 aby uzyskać dostęp!", "&cYou must be level 10 to access this!"),
        PERM_UPGRADES("&8Permanentne ulepszenia", "&8Permanent upgrades"),
        PERK_CLICK_TO_CHOOSE("&eKliknij aby wybrać perk!", "&eClick to choose perk!"),
        PERK_SELECT_A_PERK("&7Wybierz perk aby wypełnić ten slot.", "&7Select a perk to fill this slot."),
        PERK_CLICK_TO_PURCHASE("&eKliknij aby kupić!", "&eClick to purchase!"),
        PERK_CLICK_TO_SELECT("&eKliknij aby wybrać!", "&eClick to select!"),
        PERK_NOT_ENOUGH_GOLD_DISPLAY("&cNiewystarczająco złota!", "&cNot enough gold!"),
        PERK_HARDCORE("&7Jesteś wystarczająco hardcore że nie", "&7Are you hardcore enough that you"),
        PERK_DONT_NEED("&7nie potrzebujesz żadnego perka do tego slotu?", "&7don't need any perk for this slot?"),
        PERK_CLICK_TO_REMOVE("&eKliknij tutaj aby usunąć perk!", "&eClick here to remove perk!"),
        PERM_UPGRADES_BACK("&7Do Permanentne ulepszenia", "&7To Permanent upgrades"),
        PERK_UNLOCKED_IN_RENOWN("&cOdblokuj w sklepie renomy!", "&cUnlocked in renown shop!"),
        CONFIRM_CANCEL("&cAnuluj", "&cCancel"),
        CONFIRM_CANCEL_DESC("&7Wróć do menu", "&7Return to menu"),
        CONFIRM_CONFIRM("&a&lPOTWIERDŹ", "&a&lCONFIRM"),
        CONFIRM_CONFIRM_DESC("&7Kliknij aby potwierdzić zakup", "&7Click to confirm purchase"),
        CONFIRM_CANCEL_TITLE("&c&lANULUJ", "&c&lCANCEL"),
        CONFIRM_CANCEL_TITLE_DESC("&7Kliknij aby anulować zakup", "&7Click to cancel purchase"),
        PERK_SELECT_TITLE("Wybierz perk", "Choose a perk"),
        CHOOSE_A_PERK_TITLE("Wybierz perk", "Choose a perk"),
        ITEM_SWORD("ᴍɪᴇᴄᴢ", "ѕᴡᴏʀᴅ"),
        ITEM_BOW("ʟᴜᴋ", "ʙᴏᴡ"),
        ITEM_PANTS("ѕᴘᴏᴅɴɪᴇ", "ᴘᴀɴᴛѕ"),
        BOUNTY_ACTION_OF("nadano", "of"),
        BOUNTY_ACTION_BUMP("zwiekszono", "bump")

        ;

        private final String polish;
        private final String english;

        Simple(String polish, String english) {
            this.polish = polish;
            this.english = english;
        }

        Simple(String both) {
            this.polish = both;
            this.english = both;
        }

        public String get(Language lang) {
            String message = switch (lang) {
                case ENGLISH -> english;
                case POLISH -> polish;
            };
            return message.replaceAll("&([0-9a-fk-or])", "§$1");
        }

        public String get(Player player) {
            Language lang = PlayerModel.getInstance(player).getLanguage();
            String message = switch (lang) {
                case ENGLISH -> english;
                case POLISH -> polish;
            };
            return message.replaceAll("&([0-9a-fk-or])", "§$1");
        }
    }

    public enum Formatted {
        KILL_MESSAGE("&a§l{0}&7 na {1} &b+{2}XP &6+{3}$", "§a§l{0}&7 on {1} §b+{2}XP §6+{3}$"),
        ASSIST_MESSAGE("§a§lASYSTA!§7 {0}% na {1} §b+{2}XP §6{3}$", "§a§lASSIST!§7 {0}% on {1} §b+{2}XP §6{3}$"),
        DEATH_MESSAGE("&c&lŚMIERC!&7 od {0}", "&c&lDEATH!&7 by {0}"),
        CRATER("&7Zadawaj &c+{0}%&7 obrazen za kazdy &fblok&7 roznicy wysokosci", "&7Deal &c+{0}%&7 damage per &fblock&7 you're above"),
        PRESTIGE("§fPrestiż: §e{0}", "§fPrestige: §e{0}"),
        LEVEL("§fPoziom: {0}", "§fLevel: {0}"),
        KILL_TITLE("{0} &a&lZABOJSTWO!", "{0} &a&lKILL!"),
        NEEDED_XP("§fWymagany XP: §b{0}", "§fNeeded XP: §b{0}"),
        GOLD("§fZłoto: §6{0}", "§fGold: §6{0}"),
        ENCHANT_DISPLAY_NAME("{0}&9{1}"),
        STATUS("&fStatus: {0}"),
        LEVEL_UP_TITLE("§b§lNOWY POZIOM!", "§b§lLEVEL UP!"),
        LEVEL_UP_MESSAGE("§b§lNOWY POZIOM! {0}", "§b§lPIT LEVEL UP! {0}"),
        CANT_RESPAWN_FIGHTING("§c§lCZEKAJ!§7 Nie mozesz respawn podczas walki (§c{0}s§7 pozostalo)", "§c§lHOLD UP!§7 Can't respawn while fighting (§c{0}s§7 left)"),
        PERK_SLOT_LEVEL("&7Wymagany poziom: &c{0}", "&7Required level: &c{0}"),
        PERK_SLOT("{0}Perk Slot #{1}"),
        PERK_SLOT_LOCKED_TITLE("&cPerk Slot #{0}"),
        PERK_SELECTED("&7Wybrany: &a{0}", "&7Selected: &a{0}"),
        PERK_PURCHASE_MSG("&a&lZAKUP!&6 {0}", "&a&lPURCHASE!&6 {0}"),
        PERK_COST_FORMAT("&7Koszt: &6{0}g", "&7Cost: &6{0}g"),
        LIVES("&7Zycia: &a{1}&7/{2}{0}", "&7Lives: &a{1}&7/{2}{0}"),
        TIER_ITEM("{2} ʀᴀɴɢɪ {1}", "{0}ᴛɪᴇʀ {1} {2}"),
        SWORD_ATTACK_DAMAGE("&9+6.5 Attack Damage"),
        PANTS_STRENGTH_IRON("&7Tak mocne jak żelazo", "&7As strong as iron"),
        MYSTIC_WELL_USAGE("&7Używane w mystic well", "&7Used in the mystic well"),
        BOUNTY("&6&lŁUP! &7{0} &6&l{1}g &7na {2} &7za wysoki streak", "&6&lBOUNTY! &7{0} &6&l{1}g &7on {2} &7for high streak"),
        BOUNTY_CLAIMED("&6&lŁUP ODEBRANY!&7 {0}&7 zabil {1}&7 za &6&l{2}$", "&6&lBOUNTY CLAIMED!&7 {0}&7 killed {1}&7 for &6&l{2}$"),
        STREAK("&fStreak: &a{0}"),
        BOUNTY_SCOREBOARD("&fŁup: &6{0}$", "&fBounty: &6{0}$")

        ;

        private final String polish;
        private final String english;

        Formatted(String polish, String english) {
            this.polish = polish;
            this.english = english;
        }

        Formatted(String both) {
            this.polish = both;
            this.english = both;
        }

        public String format(Language language, Object... args) {
            String message = switch (language) {
                case ENGLISH -> english;
                case POLISH -> polish;
            };

            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(args[i]));
            }
            return message.replaceAll("&([0-9a-fk-or])", "§$1");
        }

        public String format(Player player, Object... args) {
            String message = switch (PlayerModel.getInstance(player).getLanguage()) {
                case ENGLISH -> english;
                case POLISH -> polish;
            };

            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(args[i]));
            }
            return message.replaceAll("&([0-9a-fk-or])", "§$1");
        }
    }
}
