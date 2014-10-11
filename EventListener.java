package advanceddeath;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * made by DieFriiks / CustomCraftDev
 */
public class EventListener implements Listener {
	
	advanceddeath plugin;
	
	/**
     * Constructor
     * @param advanceddeath plugin
     */
	public EventListener(advanceddeath plugin) {
		this.plugin = plugin;		
	}
	
	public void drop(Player p, int i){
		if(i != -1){
				//drop with permissions / config
				System.out.println("drop with config");
		}
		else{
			// drop inventory
	        for (ItemStack j : p.getInventory().getContents()){
	        	if(j != null && j.getType() != Material.AIR){
	        		p.getWorld().dropItemNaturally(p.getLocation(), j);
	        		p.getInventory().remove(j);
	        	}
	        }
	        
	        // drop armor
	        for (ItemStack k : p.getInventory().getArmorContents()){
	        	if(k != null && k.getType() != Material.AIR){
	        		p.getWorld().dropItemNaturally(p.getLocation(), k);
	        	}
	        }
	        p.getInventory().setArmorContents(new ItemStack[] { null, null, null, null } );
	        
	        // drop exp 
	        ((ExperienceOrb)p.getWorld().spawn(p.getLocation(), ExperienceOrb.class)).setExperience(p.getExpToLevel());
	        p.setExp(0);
	        
		}
	}
	
	public int checkgroup(Player p){
		for(int i = 0; i < plugin.groups.size(); i++){
			if(p.hasPermission((String) plugin.groups.get(i)[0])){
				return i;
			}
		}
		return -1;
	}
	
	public boolean cause(DamageCause d){
		switch(d){
			case BLOCK_EXPLOSION:
				return false;
			case CONTACT:
				return false;
			case CUSTOM:
				return false;
			case DROWNING:
				return false;
			case ENTITY_ATTACK:
				return true;
			case ENTITY_EXPLOSION:
				return false;
			case FALL:
				return false;
			case FALLING_BLOCK:
				return false;
			case FIRE:
				return false;
			case FIRE_TICK:
				return false;
			case LAVA:
				return false;
			case LIGHTNING:
				return false;
			case MAGIC:
				return true;
			case MELTING:
				return false;
			case POISON:
				return true;
			case PROJECTILE:
				return true;
			case STARVATION:
				return false;
			case SUFFOCATION:
				return false;
			case SUICIDE:
				return false;
			case THORNS:
				return true;
			case VOID:
				return false;
			case WITHER:
				return false;	
			default: return false;
		}
	}
	
	@EventHandler
	public void PlayerDamageReceive(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player damaged = (Player) e.getEntity();
	
			if(( ((Damageable) damaged).getHealth() - e.getDamage()) <=0){

				if(cause(e.getCause())){
					drop(damaged, checkgroup(damaged));
					e.setCancelled(true);
					damaged.teleport(damaged.getWorld().getSpawnLocation());
					damaged.setHealth(20.0);
				}
				else{
					if(plugin.onlyfrompvp){
						drop(damaged, checkgroup(damaged));
					}
					e.setCancelled(true);
					damaged.teleport(damaged.getWorld().getSpawnLocation());
					damaged.setHealth(20.0);
				}
				
				damaged.setFireTicks(0);
				damaged.setFoodLevel(20);
				damaged.setTicksLived(1);
				
			    for (PotionEffect effect : damaged.getActivePotionEffects()){
			    	damaged.removePotionEffect(effect.getType());
			    }

			}
		
		}
	}	
}
