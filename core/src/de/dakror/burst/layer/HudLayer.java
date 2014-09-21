package de.dakror.burst.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public class HudLayer extends Layer
{
	@Override
	public void show()
	{
		stage = new Stage(new ScreenViewport());
	}
	
	@Override
	public void render(float delta)
	{
		int width = 400;
		stage.getBatch().begin();
		Entity.renderHpBar(stage.getBatch(), (Gdx.graphics.getWidth() - width) / 2, 20, width, Game.player.getHpPercentage());
		stage.getBatch().end();
	}
}
