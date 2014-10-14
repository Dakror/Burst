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
import de.dakror.burst.util.D;
import de.dakror.burst.util.InternalAssetManager;
import de.dakror.burst.util.base.GameBase;

public class Burst extends GameBase
{
	public static Burst instance;
	public static AssetManager assets;
	public static TextureAtlas img;
	public static ShapeRenderer shapeRenderer;
	public static boolean debug;
	
	public static boolean smartCast = true;
	
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
		for (Layer l : layers)
			if (l.initDone) l.update(delta);
		
		Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		for (Layer l : layers)
			if (l.initDone) l.render(delta);
		
		D.p(Gdx.graphics.getDensity());
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
		if (keycode == Keys.F11)
		{
			setFullscreen(!Gdx.graphics.isFullscreen());
			
			return true;
		}
		return false;
	}
	
	public void setFullscreen(boolean fullscreen)
	{
		if (!fullscreen) Gdx.graphics.setDisplayMode(1280, 720, false);
		else Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
	}
}
