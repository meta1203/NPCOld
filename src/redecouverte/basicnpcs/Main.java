package redecouverte.basicnpcs;

import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.CreatureType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;
import redecouverte.npcspawner.BasicHumanNpc;
import redecouverte.npcspawner.BasicHumanNpcList;
import redecouverte.npcspawner.NpcSpawner;

public class Main extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private EEntityListener mEntityListener;
    public BasicHumanNpcList HumanNPCList;

    public Main() {

    }

    public void onEnable() {
        try {
            PluginManager pm = getServer().getPluginManager();

            mEntityListener = new EEntityListener(this);
            pm.registerEvent(Type.ENTITY_TARGET, mEntityListener, Priority.Normal, this);
            pm.registerEvent(Type.ENTITY_DAMAGE, mEntityListener, Priority.Normal, this);

            this.HumanNPCList = new BasicHumanNpcList();

            PluginDescriptionFile pdfFile = this.getDescription();
            logger.log(Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " enabled.");
        } catch (Exception e) {
            logger.log(Level.WARNING, "BasicNPCs error: " + e.getMessage() + e.getStackTrace().toString());
            e.printStackTrace();
            return;
        }
    }

    public void onDisable() {
        try {
            PluginDescriptionFile pdfFile = this.getDescription();
            logger.log(Level.INFO, pdfFile.getName() + " version " + pdfFile.getVersion() + " disabled.");
        } catch (Exception e) {
            logger.log(Level.WARNING, "BasicNPCs : error: " + e.getMessage() + e.getStackTrace().toString());
            e.printStackTrace();
            return;
        }
    }
    static boolean spawnHuman = true;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        try {

            if (!command.getName().toLowerCase().equals("bnpc")) {
                return false;
            }
            if (!(sender instanceof Player)) {
                return false;
            }

            if (args.length < 1) {
                return false;
            }

            String subCommand = args[0].toLowerCase();

            Player player = (Player) sender;
            Location l = player.getLocation();


            // create npc-id npc-name
            if (subCommand.equals("create")) {
                if (args.length < 3) {
                    return false;
                }

                if (this.HumanNPCList.get(args[1]) != null) {
                    player.sendMessage("This npc-id is already in use.");
                    return true;
                }

                BasicHumanNpc hnpc = NpcSpawner.SpawnBasicHumanNpc(args[1], args[2], player.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
                this.HumanNPCList.put(args[1], hnpc);
                
                ItemStack is = new ItemStack(Material.BOOKSHELF);
                is.setAmount(1);
                hnpc.getBukkitEntity().setItemInHand(is);


            // attackme npc-id
            } else if (subCommand.equals("attackme")) {

                if (args.length < 2) {
                    return false;
                }

                BasicHumanNpc npc = this.HumanNPCList.get(args[1]);
                if (npc != null) {
                    npc.attackLivingEntity(player);
                    return true;
                }

            // move npc-id
            } else if (subCommand.equals("move")) {
                if (args.length < 2) {
                    return false;
                }

                BasicHumanNpc npc = this.HumanNPCList.get(args[1]);
                if (npc != null) {
                    npc.moveTo(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
                    return true;
                }

            // spawnpig
            } else if (subCommand.equals("spawnpig")) {
                NpcSpawner.SpawnMob(CreatureType.PIG, player.getWorld(), l.getX(), l.getY(), l.getZ());
            }


        } catch (Exception e) {
            sender.sendMessage("An error occured.");
            logger.log(Level.WARNING, "BasicNPCs: error: " + e.getMessage() + e.getStackTrace().toString());
            e.printStackTrace();
            return true;
        }

        return true;
    }
}
