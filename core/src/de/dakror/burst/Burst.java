package de.dakror.burst;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

import de.dakror.burst.layer.Layer;
import de.dakror.burst.layer.LoadingLayer;
import de.dakror.burst.util.InternalAssetManager;
import de.dakror.burst.util.base.GameBase;

public class Burst extends GameBase
{
	public static Burst instance;
	public static AssetManager assets;
	
	@Override
	public void create()
	{
		instance = this;
		
		assets = new AssetManager();
		InternalAssetManager.init();
		
		new Updater();
		
		setLayer(new LoadingLayer());
	}
	
	@Override
	public void render()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		for (Layer l : layers)
			l.render(Gdx.graphics.getDeltaTime());
	}
}
