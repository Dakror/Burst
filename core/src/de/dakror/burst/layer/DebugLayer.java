package de.dakror.burst.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.TimeUtils;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;

/**
 * @author Dakror
 */
public class DebugLayer extends Layer
{
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	FloatArray renderTimes = new FloatArray();
	FloatArray tickTimes = new FloatArray();
	
	long lastTick;
	int max = 500;
	
	@Override
	public void show()
	{
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		
		initDone = true;
	}
	
	@Override
	public void tick(int tick)
	{
		if (lastTick == 0) lastTick = TimeUtils.nanoTime();
		long delta = TimeUtils.nanoTime() - lastTick;
		if (delta > 0)
		{
			tickTimes.add(delta / 1000000000f);
			while (tickTimes.size > max)
				tickTimes.removeIndex(0);
			lastTick = TimeUtils.nanoTime();
		}
	}
	
	@Override
	public void render(float delta)
	{
		renderTimes.add(delta);
		while (renderTimes.size > max)
			renderTimes.removeIndex(0);
		
		spriteBatch.begin();
		
		drawString("Burst infdev 0.1", 0, Gdx.graphics.getHeight());
		drawString("FPS: " + Gdx.graphics.getFramesPerSecond() + ", UPS: " + Burst.instance.ticksPerSecond, 0, Gdx.graphics.getHeight() - 14);
		// drawString("E: " + Game.instance.entities.size, 0, Gdx.graphics.getHeight() - 14 * 3);
		drawString("X: " + Game.player.getPos().x, 0, Gdx.graphics.getHeight() - 14 * 4);
		drawString("Y: " + Game.player.getPos().y, 0, Gdx.graphics.getHeight() - 14 * 5);
		drawString("Z: " + Game.player.getPos().z, 0, Gdx.graphics.getHeight() - 14 * 6);
		
		int full = 500;
		int fac = 25;
		drawString(fac + "ms", 0, full + 14);
		drawString(fac + "ms", max, full + 14);
		spriteBatch.end();
		
		Burst.shapeRenderer.setProjectionMatrix(spriteBatch.getProjectionMatrix());
		Burst.shapeRenderer.identity();
		Burst.shapeRenderer.begin(ShapeType.Filled);
		Burst.shapeRenderer.setColor(Color.BLACK);
		Burst.shapeRenderer.rect(0, 0, 5, full);
		for (int i = 0; i < renderTimes.size; i++)
		{
			float rt = renderTimes.get(i) * fac;
			Color c = new Color(rt, 0, 0, 0.5f);
			Burst.shapeRenderer.rect(5 + i, 0, 1, rt * full, Color.BLACK, Color.BLACK, c, c);
		}
		
		Burst.shapeRenderer.rect(max, 0, 5, full);
		for (int i = 0; i < tickTimes.size; i++)
		{
			float rt = tickTimes.get(i) * fac;
			Color c = new Color(rt, 0, 0, 0.5f);
			Burst.shapeRenderer.rect(5 + i + max, 0, 1, rt * full, Color.BLACK, Color.BLACK, c, c);
		}
		
		Burst.shapeRenderer.end();
	}
	
	public void drawString(String s, int x, int y)
	{
		// TextBounds tb = font.getBounds(s);
		// Vloxlands.skin.getDrawable("shadow_mm").draw(spriteBatch, x, y - tb.height - 1, tb.width, tb.height);
		font.draw(spriteBatch, s, x, y);
	}
	
	@Override
	public void resize(int width, int height)
	{
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
}
