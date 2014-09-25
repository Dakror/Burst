package de.dakror.burst;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;

import de.dakror.burst.layer.DebugLayer;
import de.dakror.burst.layer.Layer;
import de.dakror.burst.layer.LoadingLayer;
import de.dakror.burst.util.InternalAssetManager;
import de.dakror.burst.util.base.GameBase;

public class Burst extends GameBase
{
	public static Burst instance;
	public static AssetManager assets;
	public static TextureAtlas img;
	public static ShapeRenderer shapeRenderer;
	public static boolean debug;
	
	float last;
	int tick;
	int ticks;
	long lastSecond;
	public int ticksPerSecond;
	
	@Override
	public void create()
	{
		instance = this;
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		assets = new AssetManager();
		InternalAssetManager.init();
		
		shapeRenderer = new ShapeRenderer();
		getMultiplexer().addProcessor(0, new GestureDetector(this));
		getMultiplexer().addProcessor(0, this);
		Gdx.input.setInputProcessor(getMultiplexer());
		
		setLayer(new LoadingLayer());
	}
	
	@Override
	public void render()
	{
		float delta = Gdx.graphics.getDeltaTime();
		if (last + delta >= 1 / 60f)
		{
			ticks++;
			tick++;
			for (Layer l : layers)
				if (l.initDone) l.tick(tick);
			last = 0;
		}
		else last += delta;
		
		if (System.currentTimeMillis() - lastSecond >= 1000)
		{
			ticksPerSecond = ticks;
			ticks = 0;
			lastSecond = System.currentTimeMillis();
		}
		
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		for (Layer l : layers)
			if (l.initDone) l.render(delta);
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		if (keycode == Keys.F1)
		{
			debug = !debug;
			toggleLayer(new DebugLayer());
			return true;
		}
		return false;
	}
}
