package advanceddeath;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * made by DieFriiks / CustomCraftDev
 */
public class EventListener implements Listener {
	
	AdvancedDeath plugin;
	
	/**
     * Constructor
     * @param AdvancedDeath regioninv
     */
	public EventListener(AdvancedDeath regioninv) {
		this.plugin = regioninv;		
	}
	
	@EventHandler
	public void PlayerDamageReceive(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			Player damaged = (Player) e.getEntity();
	
			if(( ((Damageable) damaged).getHealth() - e.getDamage()) <=0){

				if(e.getDamager() instanceof Player){
					//KilledbyPVP
					e.setCancelled(true);
					damaged.teleport(damaged.getWorld().getSpawnLocation());
					damaged.setHealth(20.0);
				}
				else{
					//KilledbyPVE
					e.setCancelled(true);
					damaged.teleport(damaged.getWorld().getSpawnLocation());
					damaged.setHealth(20.0);
				}
			}
		}
	}	
}
