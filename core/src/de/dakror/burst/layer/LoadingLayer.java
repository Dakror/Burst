package de.dakror.burst.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.util.InternalAssetManager;

/**
 * @author Dakror
 */
public class LoadingLayer extends Layer
{
	BitmapFont font;
	
	@Override
	public void show()
	{
		modal = true;
		
		font = new BitmapFont();
		stage = new Stage(new ScreenViewport());
		
		Burst.assets.load("img/pack.atlas", TextureAtlas.class);
		Burst.assets.load("font/tele.fnt", BitmapFont.class);
		
		ParticleEffectParameter pep = new ParticleEffectParameter();
		pep.atlasFile = "img/pack.atlas";
		InternalAssetManager.scheduleDirectory(Burst.assets, "fx/", ParticleEffect.class, false, pep);
		
		initDone = true;
	}
	
	@Override
	public void render(float delta)
	{
		if (Burst.assets.update())
		{
			Burst.img = Burst.assets.get("img/pack.atlas", TextureAtlas.class);
			Burst.instance.removeLayer(this);
			Burst.instance.addLayer(new Game());
			return;
		}
		
		stage.getBatch().begin();
		TextBounds tb = font.getBounds("Loading...");
		font.draw(stage.getBatch(), "Loading...", (Gdx.graphics.getWidth() - tb.width) / 2, Gdx.graphics.getHeight() / 2 - tb.height);
		stage.getBatch().end();
	}
}

// BEST LOADING SCREEN EVAAARRR!!!!
