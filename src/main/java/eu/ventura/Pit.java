package eu.ventura;

import co.aikar.commands.PaperCommandManager;
import dev.kyro.arcticapi.ArcticAPI;
import eu.ventura.commands.*;
import eu.ventura.constants.PitMap;
import eu.ventura.listener.*;
import eu.ventura.service.CombatService;
import eu.ventura.service.PlayerService;
import eu.ventura.util.MongoUtil;
import hvh.ventura.VenturaCore;
import io.github.cdimascio.dotenv.Dotenv;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * author: ekkoree
 * created at: 12/26/2025
 */
public class Pit extends JavaPlugin {
    public static Pit instance;
    public static final PitMap map = PitMap.KINGS;

    @Override
    public void onEnable() {
        instance = this;

        Dotenv dotenv = Dotenv.configure()
                .directory(getDataFolder().getAbsolutePath())
                .ignoreIfMissing()
                .load();

        String mongoUri = dotenv.get("MONGO_URI", "mongodb://localhost:27017");
        String mongoDatabase = dotenv.get("MONGO_DATABASE", "ventura_pit");
        MongoUtil.initialize(mongoUri, mongoDatabase);

        VenturaCore.init(this);

        CombatService.getInstance().start();
        ArcticAPI.init(this, "", "");

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new SpawnCommand());
        commandManager.registerCommand(new EnchantCommand());
        commandManager.registerCommand(new FreshCommand());
        commandManager.registerCommand(new StatsCommand());

        getServer().getPluginManager().registerEvents(new PlayerListener(
                VenturaCore.getNpcManager(),
                VenturaCore.getHologramApi()
        ), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new AssistListener(), this);
        getServer().getPluginManager().registerEvents(new BountyListener(), this);
        getServer().getPluginManager().registerEvents(new ServerListener(), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
        getServer().getPluginManager().registerEvents(new ResourceListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        PlayerService.saveAll();
        MongoUtil.shutdown();

        VenturaCore.shutdown();

        instance = null;

        CombatService.getInstance().stop();

        HandlerList.unregisterAll(this);
    }
}
