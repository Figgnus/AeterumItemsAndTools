package me.figgnus.aeterumitemsandtools.tools.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RandomizerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack customShovel = createCustomItem();
            // You can add customizations to your shovel here
            player.getInventory().addItem(customShovel);
        }
        return true;
    }
    private ItemStack createCustomItem() {
        ItemStack randomiser = new ItemStack(Material.DIAMOND_SHOVEL);
        ItemMeta randomiserMeta = randomiser.getItemMeta();

        randomiserMeta.setDisplayName("Randomizer");
        randomiserMeta.setCustomModelData(1234567);
        randomiser.setItemMeta(randomiserMeta);


        return randomiser;
    }
}
