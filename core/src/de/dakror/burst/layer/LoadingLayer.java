/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.burst.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader.ParticleEffectParameter;
import com.badlogic.gdx.graphics.Texture;
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
public class LoadingLayer extends Layer {
	BitmapFont font;
	
	@Override
	public void show() {
		modal = true;
		
		font = new BitmapFont();
		stage = new Stage(new ScreenViewport());
		
		Burst.assets.load("img/blood.png", Texture.class);
		Burst.assets.load("img/background.png", Texture.class);
		
		Burst.assets.load("img/pack.atlas", TextureAtlas.class);
		Burst.assets.load("font/tele.fnt", BitmapFont.class);
		
		ParticleEffectParameter pep = new ParticleEffectParameter();
		pep.atlasFile = "img/pack.atlas";
		InternalAssetManager.scheduleDirectory(Burst.assets, "fx/", ParticleEffect.class, false, pep);
		
		initDone = true;
	}
	
	@Override
	public void render(float delta) {
		if (Burst.assets.update()) {
			Burst.img = Burst.assets.get("img/pack.atlas", TextureAtlas.class);
			Burst.instance.removeLayer(this);
			Burst.instance.addLayer(new Game());
			Burst.instance.addLayer(Game.hud = new HudLayer());
			
			return;
		}
		
		stage.getBatch().begin();
		TextBounds tb = font.getBounds("Loading...");
		font.draw(stage.getBatch(), "Loading...", (Gdx.graphics.getWidth() - tb.width) / 2, Gdx.graphics.getHeight() / 2 - tb.height);
		stage.getBatch().end();
	}
}

// BEST LOADING SCREEN EVAAARRR!!!!
