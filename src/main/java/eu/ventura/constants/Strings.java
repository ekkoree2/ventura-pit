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

        NOTE_BUG("&e&lEJ!&7 Jezeli znalazles jakis bug i chcesz by zostal naprawiony, uzyj &9/bug report&7!", "&6&lNOTE! If you found a bug and you want it fixed, use &9/bug report&7!"),

        CANT_DROP("1&c&lNIE!&7 Nie mozesz wyrzucic tego przedmiotu!", "§c§lNOPE!§7 You cannot drop this item!"),

        OOF_CD("§c§lKURWA!§7 Mozesz uzywac /oof co 10 sekund!", "§c§lCHILL OUT!§7 You may only /oof every 10 seconds!"),
        OOF_NO("&c&lNIE KURWA!&7 Nie mozesz uzywac /oof na spawnie!", "§c§lNOPE!§7 Can't /oof in spawn!"),

        HEADER("&bGrasz na &e&lHVH.VENTURACLIENT.EU", "&bYou are playing on &e&lHVH.VENTURACLIENT.EU"),
        FOOTER("&aDolacz na discorda!&9 discord.gg/venturaclient", "&aJoin us at discord!&9 discord.gg/venturaclient"),
        IGNITION("Zapłon", "Ignition"),

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
        PERK_LUCKY_DIAMOND("Szczęśliwy diament", "Lucky Diamond"),
        STREAKER("Streaker"),
        PERK_DIRTY("Brudny", "Dirty"),
        PERK_RAMBO("Rambo"),
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

        PRESTIGE_TITLE("§e§lPRESTIGE!"),
        PRESTIGE_MAX("§aOsiągnąłeś maksymalny prestiż!", "§aYou reached the max prestige!"),
        PRESTIGE_TOO_LOW_LEVEL("§cJesteś zbyt niskiego poziomu żeby awansować!", "§cYou are too low level to prestige yet!"),
        PRESTIGE_TOO_LOW_LEVEL_DREAM("§cMarz duży i może kiedyś!", "§cDream big and maybe someday!"),
        PRESTIGE_GRIND_MORE("§cGrinduj więcej i może kiedyś!", "§cGrind some more and maybe someday!"),
        PRESTIGE_MENU_TITLE("§8Prestiż & Renown", "§8Prestige & Renown"),
        PRESTIGE_MENU_BUTTON("§bPrestiż", "§bPrestige"),
        PRESTIGE_CONFIRM_TITLE("§8Prestiż?", "§8Prestige?"),
        PRESTIGE_ARE_YOU_SURE_YELLOW("§eNA PEWNO?", "§eARE YOU SURE?"),
        PRESTIGE_ARE_YOU_SURE_GREEN("§aNA PEWNO?", "§aARE YOU SURE?"),
        PRESTIGE_WAIT_AND_READ("Czekaj i czytaj!", "Wait and read!"),
        PRESTIGE_CLICK_TO_PRESTIGE("Kliknij aby awansować!", "Click to prestige!"),
        PRESTIGE_LEVEL_120_REQUIRED("§cMusisz być poziom 120 żeby uzyskać dostęp!", "§cYou must be level 120 to access this!"),
        PRESTIGE_RENOWN_SHOP("§eRenown shop"),
        PRESTIGE_RESET_LEVEL("§c§l■ §7Resetuje §bpoziom§7 do 1", "§c§l■ §7Resets §blevel§c to 1"),
        PRESTIGE_RESET_GOLD("§c§l■ §7Resetuje §6złoto§7 do 0", "§c§l■ §7Resets §6gold§c to 0"),
        PRESTIGE_RESET_PERKS("§c§l■ §7Resetuje §cWSZYSTKIE§a perki i ulepszenia", "§c§l■ §7Resets §cALL§a perks and upgrades"),
        PRESTIGE_RESET_INVENTORY("§c§l■ §7Resetuje §ctwój ekwipunek", "§c§l■ §7Resets §cyour inventory"),
        PRESTIGE_RENOWN_KEPT("§7§oUlepszenia Renown są zachowane", "§7§oRenown upgrades are kept"),
        PRESTIGE_ENDERCHEST_KEPT("§7§oEnder chest jest zachowany", "§7§oEnder chest is kept"),
        PRESTIGE_CLICK_TO_PURCHASE("§eKliknij aby kupić!", "§eClick to purchase!"),
        PRESTIGE_MAX_REACHED("§7Jakimś cudem osiągnąłeś maksymalny prestiż. Gratulacje!", "§7You somehow reached the max prestige. Congrats!"),
        PRESTIGE_LEVEL_REQ("§7Wymagany poziom: ", "§7Required Level: "),
        PRESTIGE_LEVEL_UP_TO_PRESTIGE("§7Zdobądź poziomy żeby awansować!", "§7Level up to prestige!"),
        PRESTIGE_COST("§7Koszt:", "§7Cost:"),
        PRESTIGE_RESETTING_LEVEL("§c§lRESETOWANIE POZIOMU!", "§c§lRESETTING LEVEL!"),
        PRESTIGE_RESETTING_PERKS("§c§lRESETOWANIE PERKÓW!", "§c§lRESETTING PERKS!"),
        PRESTIGE_RESETTING_UPGRADES("§c§lRESETOWANIE ULEPSZEŃ!", "§c§lRESETTING UPGRADES!"),
        PRESTIGE_RESETTING_INVENTORY("§c§lRESETOWANIE EKWIPUNKU!", "§c§lRESETTING INVENTORY!"),

        LEADERBOARD_HEADER("&b&lɴᴀᴊᴡɪᴇᴋѕᴢᴇ ɴᴇʀᴅʏ", "&b&lᴛᴏᴘ ᴀᴄᴛɪᴠᴇ ᴘʟᴀʏᴇʀѕ"),
        LEADERBOARD_SUBTITLE("&7ᴘᴏᴢɪᴏᴍ ᴘɪᴛ", "&7ᴘɪᴛ ʟᴇᴠᴇʟ"),

        RENOWN_SHOP_TITLE("§8Sklep Renown", "§8Renown Shop"),
        RENOWN_UPGRADES_TITLE("§8Ulepszenia Renown", "§8Renown Upgrades"),
        RENOWN_PERKS_TITLE("§8Perki Renown", "§8Renown Perks"),
        RENOWN_ITEMS_TITLE("§8Przedmioty Renown", "§8Renown Shop Items"),
        RENOWN_KILLSTREAKS_TITLE("§8Killstreaki Renown", "§8Renown Killstreaks"),
        RENOWN_NOT_ENOUGH("§cNie masz wystarczająco renown!", "§cYou don't have enough renown to afford this!"),
        RENOWN_PRESTIGE_TOO_LOW("§cJesteś zbyt niskiego prestige!", "§cYou are too low prestige to acquire this!"),
        RENOWN_ALREADY_UNLOCKED("§aJuż odblokowałeś to ulepszenie!", "§aYou already unlocked this upgrade!"),
        RENOWN_ALREADY_UNLOCKED_LAST("§aJuż odblokowałeś ostatnie ulepszenie!", "§aYou already unlocked last upgrade!"),
        RENOWN_ALREADY_UNLOCKED_PERK("§aJuż odblokowałeś ten perk!", "§aYou already unlocked this perk!"),
        RENOWN_AUTOFAIL("§c§lAUTOFAIL!§7 Niewystarczająco złota na ", "§c§lAUTOFAIL!§7 Not enough gold for "),
        RENOWN_UPGRADES_ICON("§aUlepszenia", "§aUpgrades"),
        RENOWN_PERKS_ICON("§bPerki", "§bPerks"),
        RENOWN_UPGRADES_DESC_1("§7Różne ulepszenia, buffy i", "§7Variety of upgrades, buffs and"),
        RENOWN_UPGRADES_DESC_2("§7specjalne odblokowania.", "§7special unlocks."),
        RENOWN_PERKS_DESC_1("§7Odblokuj nowe perki dostępne do", "§7Unlock new perks available for"),
        RENOWN_PERKS_DESC_2("§7zakupu w sklepie perków.", "§7purchase at the upgrades npc."),
        RENOWN_MAX_TIER_UNLOCKED("§aOdblokowano max tier!", "§aMax tier unlocked!"),
        RENOWN_UNLOCKED("§aOdblokowano!", "§aUnlocked!"),
        RENOWN_UNKNOWN_UPGRADE("§cNieznane ulepszenie", "§cUnknown upgrade"),
        RENOWN_TOO_LOW_PRESTIGE("§cZa niski prestige!", "§cToo low prestige!"),
        RENOWN_NOT_ENOUGH_SHORT("§cNiewystarczająco renown!", "§cNot enough renown!"),
        RENOWN_CLICK_TO_PURCHASE("§eKliknij aby zakupić!", "§eClick to purchase!"),
        RENOWN_CLICK_TO_BROWSE("§eKliknij aby przeglądać!", "§eClick to browse!"),
        RENOWN_NICE("§a§lNICE!!"),
        RENOWN_GO_BACK("§aWróć", "§aGo Back"),
        RENOWN_EACH_TIER("§7Każdy tier:", "§7Each tier:"),
        RENOWN_PERK_UNLOCK_1("§7§oTo jest odblokowanie perka.", "§7§oThis is a perk unlock."),
        RENOWN_PERK_UNLOCK_2("§7§oMusisz jeszcze kupić", "§7§oYou still have to purchase"),
        RENOWN_PERK_UNLOCK_3("§7§oten perk w sklepie", "§7§othe perk in the upgrades"),
        RENOWN_PERK_UNLOCK_4("§7§oulepszeń.", "§7§oshop."),

        MAJOR_EVENT_ONE_MINUTE("§5§lDUZY EVENT! §7Zaczyna sie za §e1 minute", "§5§lMAJOR EVENT! §7Starting in §e1 minute"),
        MAJOR_EVENT_ONE_MINUTE_PLURAL("§5§lDUZY EVENT! §7Zaczyna sie za §e{0} minuty", "§5§lMAJOR EVENT! §7Starting in §e{0} minutes"),

        TIME_ONE_MINUTE("§e1 minuta", "§e1 minute"),

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
        SHARP_ENCH("&7Zadawaj &c+{0}%&7 wiecej obrazen", "&7Deal &c+{0}%&7 more damage"),
        RAGE_PIT_PLACE("&c{0}❤ zadane &7(ranga #{1})", "&c{0}❤ dealt &7(ranked #{1})"),
        RAGE_PIT_TOP_ENTRY("  &e&l#{0} {1}&e z &c{2}❤ zadanym", "  &e&l#{0} {1}&e with &c{2}❤ dealt"),
        PRESTIGE_SUBTITLE("§7Odblokowałeś prestiż §e{0}", "§7You unlocked prestige §e{0}"),
        PRESTIGE_BROADCAST("§e§lPRESTIGE! {0}{1}§7 odblokował prestiż §e{2}§7, gg!", "§e§lPRESTIGE! {0}{1}§7 unlocked prestige §e{2}§7, gg!"),
        PRESTIGE_CURRENT("§7Aktualny: §e{0}", "§7Current: §e{0}"),
        PRESTIGE_GOLD_GRINDED("§c§l■ §7Zgrindowane §6{0}§7/§6{1}g", "§c§l■ §7Grinded §6{0}§c/§6{1}g"),
        PRESTIGE_RENOWN_REWARD("§7Nagroda: §e{0} Renown", "§7Reward: §e{0} Renown"),
        PRESTIGE_NEW_PRESTIGE("§7Nowy prestiż: §e{0}", "§7New prestige: §e{0}"),
        PRESTIGE_XP_PENALTY("§b+{0}%§7 wymagany xp niż normalnie!", "§b+{0}%§7 needed xp than normal!"),
        PRESTIGE_NOT_ENOUGH_GOLD("§cNie zgrindowałeś wystarczająco! Zgrindowałeś §6{0}g§c z §6{1}g", "§cYou did not grind enough! You grinded §6{0}g§c out of §6{1}g"),
        PRESTIGE_RENOWN_DISPLAY("§7Renown: §e{0} Renown"),
        PRESTIGE_RENOWN_SHOP_LORE_1("§7Użyj §eRenown§7 zdobyte z", "§7Use §eRenown§7 earned from"),
        PRESTIGE_RENOWN_SHOP_LORE_2("§bPrestige§7 aby odblokować unikalne", "§bPrestige§7 to unlock unique"),
        PRESTIGE_RENOWN_SHOP_LORE_3("§7ulepszenia!", "§7upgrades!"),
        PRESTIGE_RENOWN_SHOP_LORE_4("§7§oTe ulepszenia są bezpieczne od", "§7§oThese upgrades are safe from"),
        PRESTIGE_RENOWN_SHOP_LORE_5("§7§oresetu prestige.", "§7§oprestige reset."),
        PRESTIGE_CLICK_TO_BROWSE("§eKliknij żeby przeglądać!", "§eClick to browse!"),
        LEADERBOARD_ENTRY("§e{0}. §f{1} §e{2}§7 - §b{3} XP"),
        LEADERBOARD_SUFFIX("&7Posiadasz: &b{0} XP", "&7You have: &b{0} XP"),
        MAJOR_EVENT_STARTING("§5§lDUZY EVENT!§5§l {0}!§7 Zaczyna sie za {1}", "§5§lMAJOR EVENT!§5§l {0}!§7 Starting in {1}"),
        MAJOR_EVENT_CHAT("§5§lDUZY EVENT! {0} §7za {1} min{2}", "§5§lMAJOR EVENT! {0} §7in {1} min{2}"),
        MAJOR_EVENT_BOSSBAR_STARTING("§7Zaczyna sie za §a{0}", "§7Starting in §a{0}"),
        TIME_MINUTES("§e{0} minuty", "§e{0} minutes"),
        STREAK_BROADCAST("§c§lSTREAK!§7 z §c{0}§7 killi przez {1}", "§c§lSTREAK!§7 of §c{0}§7 kills by {1}"),
        LUCKY_DIAMOND_MSG("§b§lSZCZĘŚLIWY DIAMENT!§7 {0}", "§b§lLUCKY DIAMOND!§7 {0}"),
        RENOWN_UNLOCKED_COUNT("§7Odblokowano: §e{0}/{1}", "§7Unlocked: §e{0}/{1}"),
        RENOWN_UNKNOWNS("§8Nieznane: {0}", "§8Unknowns: {0}"),
        RENOWN_DISPLAY("§7Renown: §e{0} Renown"),
        RENOWN_COST("§7Koszt: §e{0} Renown", "§7Cost: §e{0} Renown"),
        RENOWN_YOU_HAVE("§7Posiadasz: §e{0} Renown", "§7You have: §e{0} Renown"),
        RENOWN_REQUIRED_PRESTIGE("§7Wymagany prestige: §e{0}", "§7Required prestige: §e{0}"),
        RENOWN_PRESTIGE_DISPLAY("§7Prestige: §e{0}"),
        RENOWN_CURRENT("§7Aktualny: {0}", "§7Current: {0}"),
        RENOWN_TIER("§7Tier: §a{0}"),
        RENOWN_LEVEL("§7Wymagany poziom: {0}", "§7Required level: {0}"),
        RENOWN_GOLD_COST("§7Koszt: §6{0}", "§7Cost: §6{0}"),
        RENOWN_PURCHASE_MSG("§a§lZAKUP!§6 {0}", "§a§lPURCHASE!§6 {0}"),
        RENOWN_UNLOCK_MSG("§a§lODBLOKOWANO!§6 {0}", "§a§lUNLOCK!§6 {0}"),
        RENOWN_TO_PANEL("§7Do {0}", "§7To {0}"),
        RENOWN_PERK_PREFIX("Perk: {0}")
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
        RAGE_PIT_POTATO(new LoreBuilderWrapper(
                new LoreBuilder()
                        .add("§7Eventowy przedmiot")
                        .addNewline("§9Speed I (0:08)")
                        .addNewline("§9Resistance II (0:08)")
                        .addNewline("§7ODRADZA 1 ZYCIE!!")
                        .addNewline("§72 SERCA ABSORPCJI!!")
                ,
                new LoreBuilder()
                        .add("§7Event item")
                        .addNewline("§9Speed I (0:08)")
                        .addNewline("§9Resistance II (0:08)")
                        .addNewline("§7HEALS 1 HEART!!")
                        .addNewline("§72 ABSORPTION HEARTS!!")
        )),
        MOMENTUM(new LoreBuilderWrapper(
           new LoreBuilder()
                   .add("&7Zdobywaj §c3❤&7 i &9Speed I &7(0:08) po zabojstwie."),
           new LoreBuilder()
                   .add("§7Heal §c3❤ §7and gain §9Speed I &7(0:08) on kill.")
        )),
        IGNITION(new LoreBuilderWrapper(
           new LoreBuilder()
                   .add("§7Pierwsze §dkilka killi §7każdego życia daje §b+12 XP §6+9g§7."),
           new LoreBuilder()
                   .add("§7First &dfew kills&7 each life reward &b+12 XP &6+9g&7.")
        )),
        LUCKY_DIAMOND(new LoreBuilderWrapper(
           new LoreBuilder()
                   .add("§79% szans na ulepszenie zalozynej zbroi do §bdiamentu§7 po zabojstwie."),
           new LoreBuilder()
                   .add("§79% chance to upgrade equipped armor to §bdiamond§7 on kill.")
        )),
        TENACITY(new LoreBuilderWrapper(
           new LoreBuilder()
                   .add("§7Każdy tier:")
                   .addNewline("§7Ulecz §c0.5❤§7 po zabójstwie."),
           new LoreBuilder()
                   .add("§7Each tier:")
                   .addNewline("§7Heal §c0.5❤§7 on kill.")
        )),
        DIRTY_DESC(new LoreBuilderWrapper(
           new LoreBuilder()
                   .add("§7Zdobywaj &9Resistance II&7 (0:04) po zabójstwie."),
           new LoreBuilder()
                   .add("§7Gain &9Resistance II&7 (0:04) on kill.")
        )),
        RAMBO_DESC(new LoreBuilderWrapper(
           new LoreBuilder()
                   .add("§7Nie zdobywasz złotych jabłek.")
                   .addNewline("§7Maks. zdrowie: §c8❤")
                   .addNewline("§7Uzupełnij całe zdrowie po zabójstwie."),
           new LoreBuilder()
                   .add("§7Don't earn golden apples.")
                   .addNewline("§7Max health: §c8❤")
                   .addNewline("§7Refill all health on kill.")
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
