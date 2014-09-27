package de.dakror.burst.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.dakror.burst.Burst;

public class DesktopLauncher
{
	public static void main(String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		config.vSyncEnabled = false;
		config.width = 1280;
		config.height = 720;
		config.resizable = true;
		config.title = "Burst";
		config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
		config.fullscreen = true;
		
		new LwjglApplication(new Burst(), config);
	}
}
