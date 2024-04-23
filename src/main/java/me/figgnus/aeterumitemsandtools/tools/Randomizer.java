package me.figgnus.aeterumitemsandtools.tools;

import me.figgnus.aeterumitemsandtools.AeterumItemsAndTools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


import java.util.*;

public class Randomizer implements Listener {
    private final AeterumItemsAndTools plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public Randomizer(AeterumItemsAndTools plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        ItemStack item = player.getInventory().getItemInMainHand();


        //Check cooldown
        if (cooldowns.containsKey(playerUuid)) {
            long lastPlaced = cooldowns.get(playerUuid);
            long timeNow = System.currentTimeMillis();

            if (timeNow - lastPlaced < 100) {
                return;
            }
        }
        if (!player.hasPermission("trowel.use") && item.getType() == Material.DIAMOND_SHOVEL && item.hasItemMeta() && item.getItemMeta().getCustomModelData() == 123456789){
            player.sendMessage(ChatColor.RED + "You don't have permission to use this cool item");
            return;
        }

        // Check if the player is holding the custom shovel
        if (event.getItem() != null && item.getType() == Material.DIAMOND_SHOVEL && item.hasItemMeta() && item.getItemMeta().getCustomModelData() == 123456789) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Get the block that was right-clicked and the face of the block that was hit
                Block clickedBlock = event.getClickedBlock();
                BlockFace blockFace = event.getBlockFace();


                if (clickedBlock != null && blockFace != null) {
                    // Calculate the position for the new block
                    Block adjacentBlock = clickedBlock.getRelative(blockFace);
                    //select random block viz lower method
                    Material selectMaterial = selectRandomMaterial(player);

                    // Place the block if the adjacent block is air
                    if (adjacentBlock.getType() == Material.AIR && !(isLocationOccupied(adjacentBlock.getLocation()))) {
                        adjacentBlock.setType(selectMaterial);

                        //subtract durability
                        item.setDurability((short) (item.getDurability() + 1));
                        //Play sound
                        playPlacemendSound(player, selectMaterial);

                        // Subtract from inventory if in survival mode
                        if (player.getGameMode() == GameMode.SURVIVAL) {
                            subtractMaterialFromInventory(player, selectMaterial);
                        }
                    }
                }
            }
        }
        cooldowns.put(playerUuid, System.currentTimeMillis());
    }
    private boolean isLocationOccupied(Location location) {
        World world = location.getWorld();
        if (world == null){
            return false;
        }
        List<Entity> entities = (List<Entity>) world.getNearbyEntities(location, 0.5,0.5,0.5);
        for (Entity entity : entities){
            if(!(entity instanceof Item) && !(entity instanceof ExperienceOrb)){
                //found entity that is not an item or experience orb
                return true;
            }
        }
        return false;
    }

    //Play sound method
    private void playPlacemendSound(Player player, Material selectMaterial) {
        Sound sound = getSoundOfMaterial(selectMaterial);
        if (sound != null) {
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        }
    }

    //Getting the sound
    private Sound getSoundOfMaterial(Material selectMaterial) {

        return Sound.BLOCK_AMETHYST_BLOCK_PLACE;
    }

    private void subtractMaterialFromInventory(Player player, Material selectMaterial) {
        PlayerInventory inventory = player.getInventory();


        for (int i = 0; i < 9; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == selectMaterial) {
                int amount = item.getAmount();
                if (amount > 1) {
                    item.setAmount(amount - 1);
                } else {
                    inventory.clear(i);
                }
                break;
            }
        }
    }

    private Material selectRandomMaterial(Player player) {
        ArrayList<Material> materials = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType().isBlock() && item.getAmount() > 0) {
                materials.add(item.getType());
            }
        }
        if (materials.isEmpty()) {
            return Material.AIR;
        }
        Random random = new Random();
        return materials.get(random.nextInt(materials.size()));
    }
}
