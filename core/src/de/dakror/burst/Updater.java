package de.dakror.burst;

import de.dakror.burst.layer.Layer;

/**
 * @author Dakror
 */
public class Updater extends Thread
{
	public static Updater instance;
	
	public int ticksPerSecond;
	long last;
	int tick;
	int ticks;
	
	public Updater()
	{
		instance = this;
		setName("Updater Thread");
		last = System.currentTimeMillis();
		start();
	}
	
	@Override
	public void run()
	{
		while (true)
		{
			tick++;
			ticks++;
			
			for (Layer l : Burst.instance.layers)
				l.tick(tick);
			
			if (System.currentTimeMillis() - last >= 1000)
			{
				ticksPerSecond = ticks;
				ticks = 0;
				last = System.currentTimeMillis();
			}
			
			try
			{
				Thread.sleep(16);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
