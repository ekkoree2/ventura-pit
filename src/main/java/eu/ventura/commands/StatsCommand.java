package eu.ventura.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import eu.ventura.model.PlayerModel;
import eu.ventura.service.PlayerService;
import eu.ventura.util.LevelUtil;
import org.bukkit.entity.Player;

@CommandPermission("rank.owner")
@CommandAlias("stats")
public class StatsCommand extends BaseCommand {
    @Default
    @HelpCommand
    @Syntax("page")
    private void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("renown set")
    @Syntax("player")
    public void renown(Player player, OnlinePlayer target, int amount) {
        Player victim = target.getPlayer();
        PlayerModel victimModel = PlayerService.getPlayer(victim);
        victimModel.setRenown(amount);
        player.sendMessage("§aSuccess!");
    }

    @CommandPermission("rank.admin")
    @Subcommand("prestige set")
    @Syntax("player")
    private void prestigeSet(Player player, OnlinePlayer target, int amount) {
        Player victim = target.getPlayer();
        amount = Math.min(35, amount);
        PlayerModel victimModel = PlayerService.getPlayer(victim);
        victimModel.requiredXP = LevelUtil.xpToNextLevel(amount, victimModel.getLevel());
        victimModel.setPrestige(amount);
        player.sendMessage("§aSuccess!");
    }

    @CommandPermission("rank.admin")
    @Subcommand("gold set")
    @Syntax("player amount")
    private void setGold(Player player, OnlinePlayer target, double amount) {
        Player victim = target.getPlayer();
        PlayerModel victimModel = PlayerService.getPlayer(victim);
        victimModel.setGold(amount);
        victimModel.addGoldReq(amount);
        player.sendMessage("§aUpdated gold for " + victim.getDisplayName());
    }

    @CommandPermission("rank.admin")
    @Subcommand("xp add")
    @Syntax("player amount")
    private void addXP(Player player, OnlinePlayer target, int amount) {
        Player victim = target.getPlayer();
        PlayerModel victimModel = PlayerService.getPlayer(victim);
        victimModel.addXp(amount);
        player.sendMessage("§aUpdated XP for " + victim.getDisplayName());
    }

    @CommandPermission("rank.gm")
    @Subcommand("level set")
    @Syntax("player level")
    private void setLevel(Player player, OnlinePlayer target, int level) {
        level = Math.min(120, level);
        Player victim = target.getPlayer();
        PlayerModel victimModel = PlayerService.getPlayer(victim);
        victimModel.setLevel(level);
        victimModel.requiredXP = LevelUtil.xpToNextLevel(victimModel.getPrestige(), level);
        player.sendMessage("§aUpdated level for " + victim.getDisplayName());
    }

    @CommandPermission("rank.gm")
    @Subcommand("bounty set")
    @Syntax("player amount")
    private void setBounty(Player player, OnlinePlayer target, int amount) {
        Player victim = target.getPlayer();
        PlayerModel victimModel = PlayerService.getPlayer(victim);
        victimModel.setBounty(amount);
        player.sendMessage("§aUpdated bounty for " + victim.getDisplayName());
    }

    @CommandPermission("rank.admin")
    @Subcommand("streak set")
    @Syntax("player amount")
    private void setStreak(Player player, OnlinePlayer target, int amount) {
        Player victim = target.getPlayer();
        PlayerModel victimModel = PlayerService.getPlayer(victim);
        victimModel.streak = amount;
        player.sendMessage("§aUpdated streak for " + victim.getDisplayName());
    }

    @Subcommand("wipe")
    @Syntax("[username]")
    @CommandCompletion("@dbplayers")
    public void wipe(Player player, @Optional String username) {
        if (username == null || username.isEmpty()) {
            player.sendMessage("§cYou must specify a username!");
            return;
        }
        PlayerService.wipe(username);
        player.sendMessage("§aWiped " + username + "!");
    }

    @Subcommand("move")
    @Syntax("<from> <to>")
    @CommandCompletion("@dbplayers @dbplayers")
    public void move(Player player, String fromUsername, String toUsername) {
        if (fromUsername.equals(toUsername)) {
            player.sendMessage("§cCannot move data to the same account!");
            return;
        }
        boolean success = PlayerService.move(fromUsername, toUsername);
        if (success) {
            player.sendMessage("§aMoved data from " + fromUsername + " to " + toUsername + "!");
        } else {
            player.sendMessage("§cFailed to move data!");
        }
    }
}
