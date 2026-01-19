package eu.ventura;

import co.aikar.commands.PaperCommandManager;
import dev.kyro.arcticapi.ArcticAPI;
import eu.ventura.commands.*;
import eu.ventura.constants.PitMap;
import eu.ventura.listener.*;
import eu.ventura.perks.permanent.Calculated;
import eu.ventura.service.CombatService;
import hvh.ventura.VenturaCore;
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

        VenturaCore.init(this);

        CombatService.getInstance().start();
        ArcticAPI.init(this, "", "");

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new SpawnCommand());
        commandManager.registerCommand(new EnchantCommand());
        commandManager.registerCommand(new FreshCommand());

        getServer().getPluginManager().registerEvents(new PlayerListener(
                VenturaCore.getNpcManager(),
                VenturaCore.getHologramApi()
        ), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new AssistListener(), this);
        getServer().getPluginManager().registerEvents(new BountyListener(), this);
        getServer().getPluginManager().registerEvents(new ServerListener(), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
    }

    @Override
    public void onDisable() {
        VenturaCore.shutdown();

        instance = null;

        CombatService.getInstance().stop();

        HandlerList.unregisterAll(this);
    }
}
