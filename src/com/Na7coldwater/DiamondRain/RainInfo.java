package com.Na7coldwater.DiamondRain;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class RainInfo {

	public final double RADIUS = 150.0;
	public final long TICK_SKIP = 10L;
	
	public World world;
	public boolean raining = false;
	public int duration = 0;
	
	private int ticks = 0;
	
	private int taskId;
	
	public DiamondRain diamondRain;
	
	RainInfo(DiamondRain diamondRain, World world)
	{
		this.diamondRain = diamondRain;
		this.world = world;
	}
	
	void start(int duration)
	{
		stop();
		this.duration = duration;
		this.ticks = 0;
		this.raining = true;
		
		world.setStorm(true);
		world.setThundering(true);
		world.setThunderDuration(duration);
		world.setWeatherDuration(duration);
		
		this.taskId = diamondRain.getServer().getScheduler()
		.scheduleSyncRepeatingTask(diamondRain, new Runnable() {
			public void run()
			{
				update();
			}
		}, TICK_SKIP, TICK_SKIP);
		diamondRain.log.info("Rain started");
	}
	
	void stop()
	{
		this.raining = false;
		world.setThundering(false);
		world.setStorm(false);
		
		diamondRain.log.info("Rain stopped");
		diamondRain.getServer().getScheduler().cancelTask(this.taskId);
	}
	
	void update()
	{
		assert(raining);
		ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
		
		List<Player> players = world.getPlayers();
		
		for(Player player : players)
		{
			if(diamondRain.random.nextDouble()<0.2)
			{
				Location center = player.getLocation().clone();
				center.setY(200.0);
				
				double direction = diamondRain.random.nextDouble()*360;
				double distance = diamondRain.random.nextDouble()*RADIUS;
				double x = Math.sin(direction) * distance;
				double z = Math.cos(direction) * distance;
				
				Location dropSpot = center.add(new Vector(x, 0, z));
				world.dropItemNaturally(dropSpot, diamond);
			}
		}
		
		this.ticks += TICK_SKIP;
		if(this.ticks > this.duration)
		{
			diamondRain.log.info("Rain stopped");
			this.raining = false;
			diamondRain.getServer().getScheduler().cancelTask(this.taskId);
		}
	}
}
