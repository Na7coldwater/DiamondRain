package com.Na7coldwater.DiamondRain;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DiamondRain extends JavaPlugin{

	Logger log = Logger.getLogger("Minecraft");
	
	Random random = new Random();
	
	final public int DEFAULT_DURATION = 60;
	
	public Map<World, RainInfo> worldMap = new HashMap<World, RainInfo>();
	
	public void onEnable()
	{
		log.info("DiamondRain enabled");
	}
	
	public void onDisable()
	{
		for(RainInfo rainInfo : worldMap.values())
		{
			rainInfo.stop();
		}
		log.info("DiamondRain disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("DiamondRain cannot be started from the console!");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("diamondrain") && args.length > 0)
		{
			Player player = (Player) sender;
			World world = player.getWorld();
			
			RainInfo rainInfo;
			
			if(!worldMap.containsKey(world))
			{
				rainInfo = new RainInfo(this, world);
				worldMap.put(world, rainInfo);
			}
			else
			{
				rainInfo = worldMap.get(world);
			}
			
			if(args[0].equalsIgnoreCase("start"))
			{
				int duration = DEFAULT_DURATION;
				if(args.length > 1)
				{
					try
					{
						duration = Integer.parseInt(args[1]);
					}
					catch(NumberFormatException e)
					{
						// How I want Integer.tryParse()...
						sender.sendMessage("Duration was not a number");
						return false;
					}
				}
				
				rainInfo.start(duration * 20);
				sender.sendMessage("Rain started for " + duration + " seconds");
				return true;
			}
			else if(args[0].equalsIgnoreCase("stop"))
			{
				rainInfo.stop();
				sender.sendMessage("Rain stopped");
				return true;
			}
		}
		return false;
	}
}
