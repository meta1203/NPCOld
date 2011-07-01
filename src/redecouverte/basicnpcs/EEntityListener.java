package redecouverte.basicnpcs;

import java.util.logging.Logger;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.HumanEntity;
import redecouverte.npcspawner.BasicHumanNpc;
import redecouverte.npcspawner.NpcEntityTargetEvent;
import redecouverte.npcspawner.NpcEntityTargetEvent.NpcTargetReason;
import redecouverte.npcspawner.NpcSpawner;

public class EEntityListener extends EntityListener {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private final Main parent;

    public EEntityListener(Main parent) {
        this.parent = parent;
    }

    public void onEntityDamage(EntityDamageEvent event1) {
    	if (event1 instanceof EntityDamageByEntityEvent) {
    	
    	org.bukkit.event.entity.EntityDamageByEntityEvent event = (org.bukkit.event.entity.EntityDamageByEntityEvent)event1;
        if (event.getEntity() instanceof HumanEntity) {
            BasicHumanNpc npc = parent.HumanNPCList.getBasicHumanNpc(event.getEntity());

            if (npc != null && event.getDamager() instanceof Player) {

                Player p = (Player) event.getDamager();
                p.sendMessage("<" + npc.getName() + "> Don't hit me so much :P");

                NpcSpawner.RemoveBasicHumanNpc(npc);
                parent.HumanNPCList.remove(npc.getUniqueId());
                
                event.setCancelled(true);

            }
        }
        }
    }


    @Override
    public void onEntityTarget(EntityTargetEvent event) {

        if (event instanceof NpcEntityTargetEvent) {
            NpcEntityTargetEvent nevent = (NpcEntityTargetEvent)event;

            BasicHumanNpc npc = parent.HumanNPCList.getBasicHumanNpc(event.getEntity());

            if (npc != null && event.getTarget() instanceof Player) {
                if (nevent.getNpcReason() == NpcTargetReason.CLOSEST_PLAYER) {
                    Player p = (Player) event.getTarget();
                    p.sendMessage("<" + npc.getName() + "> Hello friend, I'm an NPC!");
                    event.setCancelled(true);

                } else if (nevent.getNpcReason() == NpcTargetReason.NPC_RIGHTCLICKED) {
                    Player p = (Player) event.getTarget();
                    p.sendMessage("<" + npc.getName() + "> You right-clicked me!");
                    event.setCancelled(true);
                    
                } else if (nevent.getNpcReason() == NpcTargetReason.NPC_BOUNCED) {
                    Player p = (Player) event.getTarget();
                    p.sendMessage("<" + npc.getName() + "> Stop bouncing on me!");
                    event.setCancelled(true);
                }
            }
        }

    }
}
