package de.dakror.burst.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.dakror.burst.Burst;

public class HtmlLauncher extends GwtApplication
{
	@Override
	public GwtApplicationConfiguration getConfig()
	{
		return new GwtApplicationConfiguration(480, 320);
	}
	
	@Override
	public ApplicationListener getApplicationListener()
	{
		return new Burst();
	}
}
