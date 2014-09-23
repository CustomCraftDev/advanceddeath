package advanceddeath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * @author DieFriiks / CustomCraftDev / undeaD_D
 * @category Advanced Death plugin
 * @version 1.0
 */
public class AdvancedDeath extends JavaPlugin {
	
    FileConfiguration config;
    String nopermission_msg;
    boolean debug;
    boolean isplayer;
    boolean onlyfrompvp;
    boolean useeco;
    ArrayList<Object[]> groups;
    Economy economy;

	/**
     * on Plugin enable
     */
	public void onEnable() {
		
		loadConfig();
    	say("Config loaded");
    	
    	useeco = setupEconomy();
    	say("Vault hooked");
    	
    	this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    	say("Eventlistener loaded");
	}


	/**
     * on Plugin disable
     */
	public void onDisable() {
		reloadConfig();
		saveConfig();
	}

	
	/*
	 *  setup Economy with vault
	 */
    private boolean setupEconomy()
    {
    	try{
	        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
	        if (economyProvider != null) {
	            economy = economyProvider.getProvider();
	        }
        }catch(Exception e){
        	if(debug){
        		e.printStackTrace();
        	}
        }
        return (economy != null);
    }
    
	
	/**
     * on Command
     * @param sender - command sender
     * @param cmd - command
     * @param alias
     * @return true or false
     */
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		isplayer = false;
		Player p = null;
		
		if ((sender instanceof Player)) {
			p = (Player)sender;
			isplayer = true;
		}
			if(cmd.getName().equalsIgnoreCase("ad") && args.length != 0){
								
				// disable
				if(args[0].equalsIgnoreCase("disable")){
					if(isplayer){
						if(p.hasPermission("advanceddeath.disable")){
							this.setEnabled(false);
							p.sendMessage(ChatColor.RED + "[Advanced Death] was disabled");
							say("disabled by " + p.getName());
						return true;
					}
						else{
							p.sendMessage(nopermission_msg);
							return true;
						}
					}
					else{
							this.setEnabled(false);
						System.out.println("[Advanced Death] was disabled");
						return true;
					}
				}
				
				// reset
				if(args[0].equalsIgnoreCase("reset")){
					if(isplayer){
						if(p.hasPermission("advanceddeath.reset")){
						    File configFile = new File(getDataFolder(), "config.yml");
						    configFile.delete();
						    saveDefaultConfig();
							p.sendMessage(ChatColor.RED + "[Advanced Death] config reset");
						    reload();
							p.sendMessage(ChatColor.RED + "[Advanced Death] was reloaded");
							say("reset by " + p.getName());
						return true;
						}
						else{
							p.sendMessage(nopermission_msg);
							return true;
						}
					}
					else{
					    File configFile = new File(getDataFolder(), "config.yml");
					    configFile.delete();
					    saveDefaultConfig();
					    System.out.println("[Advanced Death] config reset");
					    reload();
					    System.out.println("[Advanced Death] was reloaded");
					    return true;
					}
				}
				
				// reload
				if(args[0].equalsIgnoreCase("reload")){
					if(isplayer){
						if(p.hasPermission("advanceddeath.reload")){
							reload();
							p.sendMessage(ChatColor.RED + "[Advanced Death] was reloaded");
							say("reloaded by " + p.getName());
						return true;
					}
						else{
							p.sendMessage(nopermission_msg);
							return true;
						}
					}
					else{
						    reload();
					    System.out.println("[Advanced Death] was reloaded");
						return true;
				    }
				}
			}
		
		// nothing to do here \o/
		return false;
	}
	

	/**
     * load config settings
     */
	private void loadConfig() {
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		
		debug = config.getBoolean("debug");
		nopermission_msg = ChatColor.translateAlternateColorCodes('&', config.getString("msg.noperm"));
		onlyfrompvp = config.getBoolean("Onlyfrompvp");
    	groups = loadGroups();
	}
	
	
	/**
     * load group settings
     */
	private ArrayList<Object[]> loadGroups() {
		List<String> groups = config.getStringList("Groups");    
		ArrayList<Object[]> list = new ArrayList<Object[]>();	 
		
		for(int i = 0; i < groups.size(); i++){
			String s = groups.get(i);
				Object[] temp = new Object[10];
					temp[0] = s;
					temp[1] = config.getBoolean("Settings." + s + ".Respawn");
					temp[2] = config.getInt("Settings." + s + ".Inventory");
					temp[3] = config.getInt("Settings." + s + ".Armor");
					temp[4] = config.getInt("Settings." + s + ".Hotbar");
					temp[5] = config.getBoolean("Settings." + s + ".Drop_Experience");
					temp[6] = config.getBoolean("Settings." + s + ".Drop_Economy");
					temp[7] = config.getInt("Settings." + s + ".Economy");
					temp[8] = config.getStringList("Settings." + s + ".Command");
					temp[9] = config.getStringList("Settings." + s + ".Effects");
			list.add(temp);
		}
		
		return list;
	} 
    
	
    /**
     * reload
     */
    private void reload(){
 	   	try {
			    config = null;
			    nopermission_msg = null;
			    groups = null;
			    economy = null;
			    
			    System.gc();
				reloadConfig();
			
 	   	} catch (Exception e) {
        	if(debug){
        		e.printStackTrace();
        	}
        }
    }
    
    
    /**
     * print to console
     * @param message to print
     */
	public void say(String out) {
		if(debug){
			System.out.println("[Advanced Death] [DEBUG] " + out);
		}
	}	
}
