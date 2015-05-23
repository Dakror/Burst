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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.FloatArray;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;

/**
 * @author Dakror
 */
public class DebugLayer extends Layer {
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	FloatArray renderTimes = new FloatArray();
	
	int max = 500;
	
	@Override
	public void show() {
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		
		initDone = true;
	}
	
	@Override
	public void render(float delta) {
		renderTimes.add(delta);
		while (renderTimes.size > max)
			renderTimes.removeIndex(0);
		
		spriteBatch.begin();
		
		drawString("Burst infdev 0.1", 0, Gdx.graphics.getHeight());
		drawString("FPS: " + Gdx.graphics.getFramesPerSecond(), 0, Gdx.graphics.getHeight() - 14);
		drawString("E: " + Game.instance.getStage().getActors().size, 0, Gdx.graphics.getHeight() - 14 * 2);
		drawString("X: " + Game.player.getPos().x, 0, Gdx.graphics.getHeight() - 14 * 3);
		drawString("Y: " + Game.player.getPos().y, 0, Gdx.graphics.getHeight() - 14 * 4);
		
		int full = 500;
		int fac = 25;
		drawString(fac + "ms", 0, full + 14);
		spriteBatch.end();
		
		Burst.shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
		Burst.shapeRenderer.identity();
		Burst.shapeRenderer.begin(ShapeType.Filled);
		Burst.shapeRenderer.setColor(Color.WHITE);
		Burst.shapeRenderer.rect(0, 0, 5, full);
		for (int i = 0; i < renderTimes.size; i++) {
			float rt = renderTimes.get(i) * fac;
			Color c = new Color(rt, 0, 0, 1.0f);
			Burst.shapeRenderer.rect(5 + i, 0, 1, rt * full, Color.WHITE, Color.WHITE, c, c);
		}
		
		Burst.shapeRenderer.end();
	}
	
	public void drawString(String s, int x, int y) {
		// TextBounds tb = font.getBounds(s);
		// Vloxlands.skin.getDrawable("shadow_mm").draw(spriteBatch, x, y - tb.height - 1, tb.width, tb.height);
		font.draw(spriteBatch, s, x, y);
	}
	
	@Override
	public void resize(int width, int height) {
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
}
