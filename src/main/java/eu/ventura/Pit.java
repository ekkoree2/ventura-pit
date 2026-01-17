package eu.ventura;

import co.aikar.commands.PaperCommandManager;
import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import dev.kyro.arcticapi.ArcticAPI;
import eu.ventura.commands.EnchantCommand;
import eu.ventura.commands.FreshCommand;
import eu.ventura.commands.GUICommand;
import eu.ventura.commands.SpawnCommand;
import eu.ventura.constants.PitMap;
import eu.ventura.listener.AssistListener;
import eu.ventura.listener.BountyListener;
import eu.ventura.listener.DamageListener;
import eu.ventura.listener.PlayerListener;
import eu.ventura.listener.ScoreboardListener;
import eu.ventura.listener.ServerListener;
import eu.ventura.service.CombatService;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
@Getter
public class Pit extends JavaPlugin {
    @Getter
    private static Pit instance;
    @Getter
    private static PitMap map = PitMap.KINGS;

    @Override
    public void onEnable() {
        instance = this;

        CombatService.getInstance().start();
        ArcticAPI.init(this, "", "");

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new SpawnCommand());
        commandManager.registerCommand(new EnchantCommand());
        commandManager.registerCommand(new FreshCommand());
        commandManager.registerCommand(new GUICommand());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new AssistListener(), this);
        getServer().getPluginManager().registerEvents(new BountyListener(), this);
        getServer().getPluginManager().registerEvents(new ServerListener(), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;

        CombatService.getInstance().stop();

        HandlerList.unregisterAll(this);
    }
}
