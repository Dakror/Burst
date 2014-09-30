package de.dakror.burst.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.dakror.burst.Burst;

public class DesktopLauncher
{
	public static void main(String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 0;
		config.vSyncEnabled = false;
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		config.title = "Burst";
		// config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
		config.fullscreen = false;
		
		config.addIcon("img/logo.png", FileType.Internal);
		config.addIcon("img/logo_64.png", FileType.Internal);
		config.addIcon("img/logo_32.png", FileType.Internal);
		config.addIcon("img/logo_16.png", FileType.Internal);
		
		new LwjglApplication(new Burst(), config);
	}
}
