package me.figgnus.aeterumitemsandtools;

import me.figgnus.aeterumitemsandtools.tools.Randomizer;
import me.figgnus.aeterumitemsandtools.tools.commands.RandomizerCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class AeterumItemsAndTools extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Randomizer(this), this);
        getCommand("randomizer").setExecutor(new RandomizerCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
