package me.sudura.armorkits;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;


public class ArmorListener implements Listener {
    private ArrayList<Player> miners = new ArrayList<>();

    private ArrayList<Player> explorers = new ArrayList<>();

    static final Material[] minerKit = {Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET};
    static final Material[] explorerKit = {Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET};

    static final PotionEffect[] minerEffect = {new PotionEffect(PotionEffectType.FAST_DIGGING, 160, 1), new PotionEffect(PotionEffectType.NIGHT_VISION, 160, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 160, 0)};
    static final PotionEffect[] explorerEffect = {new PotionEffect(PotionEffectType.SPEED, 160, 1)};

    BukkitRunnable effectTimer = new BukkitRunnable() {
        @Override
        public void run() {
            for(Player p : miners){
                for(PotionEffect eff : minerEffect){
                    p.addPotionEffect(eff);
                }
            }
            for(Player p : explorers){
                for(PotionEffect eff : explorerEffect){
                    p.addPotionEffect(eff);
                }
            }
        }
    };

    public ArmorListener(ArmorKits instance) {
        effectTimer.runTaskTimer(instance, 60, 120);
    }

    public void checkForKit(Player p){
        if(isPlayerWearingKit(p) == null){
            explorers.remove(p);
            miners.remove(p);
            return;
        }
        if(isPlayerWearingKit(p).equals("miner")){
            explorers.remove(p);
            miners.add(p);
            for(PotionEffect eff : minerEffect){
                p.sendMessage("You've equipped the Miner kit!");
                p.addPotionEffect(eff);
            }
            return;
        }
        if(isPlayerWearingKit(p).equals("explorer")){
            explorers.remove(p);
            miners.remove(p);
            for(PotionEffect eff : explorerEffect){
                p.sendMessage("You've equipped the Explorer kit!");
                p.addPotionEffect(eff);
            }
            return;
        }
    }

    public static String isPlayerWearingKit(Player p){
        Material[] armor = new Material[4];
        int n = 0;
        for(ItemStack i : p.getInventory().getArmorContents()){
            if(i == null){
                return null;
            }
            armor[n] = i.getType();
            n++;
        }
        if(armor[0].equals(Material.IRON_BOOTS)){
            for(int i = 1; i < 4; i++){
                if(!(armor[i] == minerKit[i])) return null;
            }
            return "miner";
        }
        if(armor[0].equals(Material.LEATHER_BOOTS)){
            for(int i = 1; i < 4; i++){
                if(!(armor[i] == explorerKit[i])) return null;
            }
            return "explorer";
        }
        return null;
    }

    @EventHandler
    public void onChangeArmor (PlayerArmorChangeEvent event) {
        if (event.getOldItem() != null && event.getNewItem() != null && event.getOldItem().getType() == event.getNewItem().getType()) {
            return;
        }

        checkForKit(event.getPlayer());
    }
}
