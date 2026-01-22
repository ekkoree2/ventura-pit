package eu.ventura.constants;

import  eu.ventura.model.PlayerModel;
import eu.ventura.util.LoreBuilder;
import eu.ventura.util.LoreBuilderWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * author: ekkoree
 * created at: 1/15/2025
 */
public class Strings {
    private static final String COLOR_PATTERN = "&([0-9a-fk-or])";
    private static final String COLOR_REPLACEMENT = "§$1";

    private static String colorize(String text) {
        return text.replaceAll(COLOR_PATTERN, COLOR_REPLACEMENT);
    }

    private static Language getLanguage(Player player) {
        return PlayerModel.getInstance(player).language;
    }

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
                        yield switch (ending) {
                            case "a" -> root + "y";
                            case "k" -> root + "ki";
                            case "g" -> root + "gi";
                            default -> s + "y";
                        };
                    }
                    yield root + "ów";
                }
            };
        }
    }

    @RequiredArgsConstructor
    @Getter
    public enum Simple {
        DEATH("&c&lŚMIERC!", "&c&lDEATH!"),
        MAXED("§fXP: §bMAKSYMALNY!", "§fXP: §bMAXED!"),
        RARE_PREFIX("&dRARE! "),
        CRIT("&9&lKRYT!", "&9&lCRIT!"),
        PERK_GOLDEN_HEADS("Złote Głowy", "Golden Heads"),
        PERK_GRAVITY_MACE("Buława Grawitacji", "Gravity Mace"),
        PERK_CALCULATED("Wyrachowany", "Calculated"),
        PERK_VAMPIRE("Wampir", "Vampire"),
        RAGE_PIT_DIDNT_PARTICIPATE("&7Nie uczestniczyles!", "&7Didn't participate!"),

        CANT_DROP("&c&lNIE!&7 Nie mozesz wyrzucic tego przedmiotu!", "§c§lNOPE!§7 You cannot drop this item!"),

        OOF_CD("§c§lKURWA!§7 Mozesz uzywac /oof co 10 sekund!", "§c§lCHILL OUT!§7 You may only /oof every 10 seconds!"),
        OOF_NO("&c&lNIE KURWA!&7 Nie mozesz uzywac /oof na spawnie!", "§c§lNOPE!§7 Can't /oof in spawn!"),

        HEADER("&bGrasz na &e&lHVH.VENTURACLIENT.EU", "&bYou are playing on &e&lHVH.VENTURACLIENT.EU"),
        FOOTER("&aDolacz na discorda!&9 discord.gg/venturaclient", "&aJoin us at discord!&9 discord.gg/venturaclient"),

        KILL_1("§a§lZABÓJSTWO!", "§a§lKILL!"),
        KILL_2("§a§lPODWÓJNE ZABÓJSTWO!", "§a§lDOUBLE KILL!"),
        KILL_3("§a§lPOTRÓJNE ZABÓJSTWO!", "§a§lTRIPLE KILL!"),
        KILL_4("§a§lPOCZWORNE ZABÓJSTWO!", "§a§lQUADRA KILL!"),
        KILL_5("§a§lPIĘCIOKROTNE ZABÓJSTWO!", "§a§lPENTA KILL!"),
        KILL_MULTI("§a§lWIELE ZABÓJSTW! §7({0})", "§a§lMULTI KILL! §7({0})"),
        CANT_RESPAWN_HERE("§cNie mozesz /respawn tutaj!", "§cYou cannot /respawn here!"),
        RESPAWN_COOLDOWN("§cMozesz uzyc /respawn co 10 sekund!", "§cYou may only /respawn every 10 seconds!"),
        LANG_CD("&cSpowolnij!", "&cWoah there, slow down!"),
        PERK_BACK("&aWróć", "&aCome back"),
        STREAKER("Streaker"),
        PERK_NO_PERK("&cBez perka", "&cNo perk"),
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
        PERK_HARDCORE("&7Jesteś wystarczająco dobry że nie potrzebujesz żadnego perka do tego slotu?", "&7Are you hardcore enough that you don't need any perk for this slot?"),
        PERK_CLICK_TO_REMOVE("&eKliknij tutaj aby usunąć perk!", "&eClick here to remove perk!"),
        PERM_UPGRADES_BACK("&7Do Permanentne ulepszenia", "&7To Permanent upgrades"),
        PERK_UNLOCKED_IN_RENOWN("&cOdblokuj w sklepie renown!", "&cUnlocked in renown shop!"),
        PERK_UNKNOWN("Nieznany perk", "Unknown perk"),
        CHOOSE_A_PERK_TITLE("Wybierz perk", "Choose a perk"),
        ITEM_SWORD("ᴍɪᴇᴄᴢ", "ѕᴡᴏʀᴅ"),
        ITEM_BOW("ʟᴜᴋ", "ʙᴏᴡ"),
        ITEM_PANTS("ѕᴘᴏᴅɴɪᴇ", "ᴘᴀɴᴛѕ"),
        BOUNTY_ACTION_OF("nadano", "of"),
        BOUNTY_ACTION_BUMP("zwiekszono", "bump"),
        GOLDEN_HEADS_NAME("&6Złota Głowa", "&6Golden Head"),
        SHOP_TITLE("Tymczasowe itemy", "Non-permanent items"),
        SHOP_NOT_ENOUGH_GOLD("&cNiewystarczająco złota!", "&cNot enough gold!"),
        SHOP_INVENTORY_FULL("&cTwój ekwipunek jest pełny!", "&cYour inventory is full!"),
        SHOP_CLICK_TO_PURCHASE("&eKliknij aby zakupić!", "&eClick to purchase!"),
        SHOP_LOST_ON_DEATH("&7§oTracisz przy śmierci.", "&7§oLost on death."),
        SHOP_ITEM_DIAMOND_SWORD("Diamentowy Miecz", "Diamond Sword"),
        SHOP_ITEM_DIAMOND_BOOTS("Diamentowe Buty", "Diamond Boots"),
        SHOP_ITEM_DIAMOND_CHESTPLATE("Diamentowa Zbroja", "Diamond Chestplate"),
        SHOP_ITEM_OBSIDIAN("Obsydian", "Obsidian"),
        SHOP_MSG_AUTO_EQUIP("&7Automatycznie zakłada przy zakupie!", "&7Auto-equips on buy!"),
        SHOP_MSG_OBSIDIAN_DURATION("&7Pozostaje przez 120 sekund.", "&7Remains for 120 seconds."),
        SHOP_MSG_DIAMOND_SWORD_BOOST("§9+20% obrażeń przeciw nagrodzonym", "§9+20% damage vs bountied"),
        CONFIRM_PANEL_TITLE("&8Jesteś pewien?", "&8Are you sure?"),
        CONFIRM_PANEL_CONFIRM("&aPotwierdź", "&aConfirm"),
        CONFIRM_PANEL_PURCHASING("&7Kupujesz: ", "&7Purchasing: "),
        CONFIRM_PANEL_COST("&7Koszt: ", "&7Cost: "),
        CONFIRM_PANEL_CANCEL("&cAnuluj", "&cCancel"),
        CONFIRM_PANEL_RETURN("&7Wróć do poprzedniego menu.", "&7Return to previous menu."),

        LANG_ERROR("&cTen jezyk jest juz wybrany!", "&cThis language is already selected!"),
        EVENT_END_TOP_HEADER("&6&lNajlepsi gracze:", "&6&lTop players:"),
        EVENT_END_FOOTER("&6&m" + ("-".repeat(22))),

        ;

        private final String polish;
        private final String english;

        Simple(String both) {
            this.polish = both;
            this.english = both;
        }

        private String getRaw(Language lang) {
            return lang == Language.ENGLISH ? english : polish;
        }

        public String get(Language lang) {
            return colorize(getRaw(lang));
        }

        public String get(Player player) {
            return get(getLanguage(player));
        }
    }

    @RequiredArgsConstructor
    public enum Formatted {
        KILL_MESSAGE("&a§l{0}&7 na {1} &b+{2}XP &6+{3}$", "§a§l{0}&7 on {1} §b+{2}XP §6+{3}$"),
        ASSIST_MESSAGE("§a§lASYSTA!§7 {0}% na {1} §b+{2}XP §6{3}$", "§a§lASSIST!§7 {0}% on {1} §b+{2}XP §6{3}$"),
        DEATH_MESSAGE("&c&lŚMIERC!&7 od {0}", "&c&lDEATH!&7 by {0}"),
        CRATER("&7Zadawaj &c+{0}%&7 obrazen za kazdy &fblok&7 roznicy wysokosci", "&7Deal &c+{0}%&7 damage per &fblock&7 you're above"),
        PRESTIGE("§fPrestiż: §e{0}", "§fPrestige: §e{0}"),
        LEVEL("§fPoziom:&7 {0}", "§fLevel:&7 {0}"),
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
        SHOP_COST("&7Koszt: &6{0}g", "&7Cost: &6{0}g"),
        SHOP_PURCHASE("&a&lZAKUP!&6 {0}", "&a&lPURCHASE!&6 {0}"),
        BOUNTY_SCOREBOARD("&fŁup: &6{0}$", "&fBounty: &6{0}$"),
        RAGE_PIT_BOSSY("&5&lDUZY EVENT!&c&l RAGE PIT!&7 Koniec za &a{0}", "&5&lMAJOR EVENT!&c&l RAGE PIT!&7 Ending in &a{0}"),
        RAGE_PIT_SUCCESS(
                "&a&lSUKCES!&7 zdobyto &c{0} Wszystkich Killi&7 z &a600&&7 wymaganych",
                "&a&lSUCCESS!&7 with &c{0} Total Kills out of &a600&7 needed"
        ),
        RAGE_PIT_FAIL(
                "&c&lPIZDA!&7 {0} wszystkich zabojstw z 600 wymaganych",
                "&c&lFAILED!&7 {0} total kills out of 600 needed"
        ),
        RAGE_PIT_PLACE("&c{0}❤ zadane &7(ranga #{1})", "&c{0}❤ dealt &7(ranked #{1})"),
        RAGE_PIT_TOP_ENTRY("  &e&l#{0} {1}&e z &c{2}❤ zadanym", "  &e&l#{0} {1}&e with &c{2}❤ dealt"),
        ;

        private final String polish;
        private final String english;

        Formatted(String both) {
            this.polish = both;
            this.english = both;
        }

        private String getRaw(Language lang) {
            return lang == Language.ENGLISH ? english : polish;
        }

        private String applyArgs(String message, Object... args) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(args[i]));
            }
            return colorize(message);
        }

        public String format(Language language, Object... args) {
            return applyArgs(getRaw(language), args);
        }

        public String format(Player player, Object... args) {
            return format(getLanguage(player), args);
        }
    }

    @RequiredArgsConstructor
    public enum Lore {
        GOLDEN_HEADS_ITEM(new LoreBuilderWrapper(
                new LoreBuilder()
                        .add("§7Przedmiot Perka")
                        .addNewline("§9Prędkość I (0:08)")
                        .addNewline("§9Regeneracja II (0:05)")
                        .addNewline("§62❤ absorpcji!")
                        .addNewline("§71 sekunda między jedzeniem"),
                new LoreBuilder()
                        .add("§7Perk Item")
                        .addNewline("§9Speed I (0:08)")
                        .addNewline("§9Regeneration II (0:05)")
                        .addNewline("§62❤ absorption!")
                        .addNewline("§71 second between eats")
        )),
        CALCULATED_PERK(new LoreBuilderWrapper(
                new LoreBuilder()
                        .add("§7Zadawaj §c+28%§7 obrażeń za celny cios. Pudło nakłada §9Weakness II §7(0:01)."),
                new LoreBuilder()
                        .add("&7Landing a hit grants &c+28%§7 damage. Missing a hit applies &9Weakness II &7(0:01).")
        )),
        GRAVITY_MACE_DESC(new LoreBuilderWrapper(
                new LoreBuilder().add("§7Zadawaj §c+10%§7 obrażeń podczas spadania."),
                new LoreBuilder().add("§7Deal §c+10%§7 damage while falling.")
        )),
        GOLDEN_HEADS_DESC(new LoreBuilderWrapper(
                new LoreBuilder().add("§7Złote jabłka, które zdobędziesz, zamieniają się w §6Złote Głowy§7."),
                new LoreBuilder().add("§7Golden apples you earn turn into §6Golden Heads§7.")
        )),
        STREAKER_DESC(new LoreBuilderWrapper(
                new LoreBuilder().add("§7Potrój &bXP&7 zdobyte ze streaka."),
                new LoreBuilder().add("§7Triple streak kill §bXP§7 bonus.")
        )),
        VAMPIRE_DESC(new LoreBuilderWrapper(
                new LoreBuilder()
                        .setMaxLength(30)
                        .add("§7Nie zdobywasz złotych jabłek.")
                        .addNewline("§7Ulecz §c1❤§7 trafieniem.")
                        .addNewline("§7Ulecz §c2❤§7 strzałą.")
                        .addNewline("§cRegen I§7 (8s) przy zabójstwie."),
                new LoreBuilder()
                        .add("§7Don't earn golden apples.")
                        .addNewline("§7Heal §c1❤§7 on hit.")
                        .addNewline("§7Heal §c2❤§7 on arrow shot.")
                        .addNewline("§cRegen I§7 (8s) on kill.")
        )),
        RAGE_PIT_SCOREBOARD(new LoreBuilderWrapper(
                new LoreBuilder()
                        .setMaxLength(30)
                        .add("§fEvent: &c&lRAGE PIT")
                        .addNewline("§fPozostalo: &a{0}")
                        .addNewline("§fObrazenia: &c{1}❤&7 {2}")
                        .addNewline("§fWszystkie kille: {3}{4}&7/600"),
                new LoreBuilder()
                        .setMaxLength(30)
                        .add("§fEvent: &c&lRAGE PIT")
                        .addNewline("§fRemaining: &a{0}")
                        .addNewline("§fDamage dealt: &c{1}❤&7{2}")
                        .addNewline("§fAll kills: {3}{4}&7/600")
        )),
        EVENT_END(new LoreBuilderWrapper(
                new LoreBuilder()
                        .setMaxLength(Integer.MAX_VALUE)
                        .add("&6&m" + ("-".repeat(22)))
                        .addNewline("&6&lEVENT ZAKONCZONY: {0}")
                        .addNewline("&6&lTwoje nagrody: &b+{1}XP &6+{2}$&e{3}")
                        .addNewline("&6&lBonus dla wszystkich: {4}")
                        .addNewline("&6&lTy: {5}"),
                new LoreBuilder()
                        .setMaxLength(Integer.MAX_VALUE)
                        .add("&6&m" + ("-".repeat(22)))
                        .addNewline("&6&lPIT EVENT ENDED: {0}")
                        .addNewline("&6&lYour rewards: &b+{1}XP &6+{2}$&e{3}")
                        .addNewline("&6&lBonus for all: {4}")
                        .addNewline("&6&lYou: {5}")
        )),


        ;

        private final LoreBuilderWrapper wrapper;

        public LoreBuilder get(Language lang) {
            return wrapper.get(lang);
        }

        public LoreBuilder get(Player player) {
            return wrapper.get(player);
        }

        public List<String> compile(Language lang) {
            return wrapper.compile(lang);
        }

        public List<String> compile(Player player) {
            return wrapper.compile(player);
        }
    }
}
