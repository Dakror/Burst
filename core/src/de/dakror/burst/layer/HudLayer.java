package de.dakror.burst.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public class HudLayer extends Layer
{
	public static HudLayer instance;
	
	float bloodFlashAlpha = 0;
	
	public void showBloodFlash()
	{
		bloodFlashAlpha = 1f;
	}
	
	@Override
	public void show()
	{
		instance = this;
		stage = new Stage();
		
		initDone = true;
	}
	
	@Override
	public void render(float delta)
	{
		int width = 400;
		stage.getBatch().begin();
		Entity.renderHpBar(stage.getBatch(), (Gdx.graphics.getWidth() - width) / 2, 20, width, Game.player.getHpPercentage());
		
		if (bloodFlashAlpha > 0)
		{
			stage.getBatch().setColor(1, 1, 1, bloodFlashAlpha);
			stage.getBatch().draw(Burst.img.findRegion("blood"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			stage.getBatch().setColor(1, 1, 1, 1);
			bloodFlashAlpha -= delta;
		}
		stage.getBatch().end();
	}
}
