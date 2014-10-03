package de.dakror.burst.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class HudLayer extends Layer
{
	float bloodFlashAlpha = 0;
	
	BitmapFont killDisplay;
	
	HorizontalGroup skillGroup;
	
	@Override
	public void show()
	{
		stage = new Stage(new ScreenViewport());
		
		killDisplay = Burst.assets.get("font/tele.fnt", BitmapFont.class);
		
		initDone = true;
	}
	
	@Override
	public void render(float delta)
	{
		stage.getBatch().begin();
		
		int width = 400;
		Creature.renderHpBar(stage.getBatch(), (Gdx.graphics.getWidth() - width) / 2, 20, width, Game.player.getHpPercentage());
		
		TextBounds tb = killDisplay.getBounds(Game.instance.getKills() + "");
		killDisplay.draw(stage.getBatch(), Game.instance.getKills() + "", (Gdx.graphics.getWidth() - tb.width) / 2, Gdx.graphics.getHeight() - tb.height / 2);
		
		if (bloodFlashAlpha > 0)
		{
			stage.getBatch().setColor(1, 1, 1, bloodFlashAlpha);
			stage.getBatch().draw(Burst.assets.get("img/blood.png", Texture.class), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			stage.getBatch().setColor(1, 1, 1, 1);
			bloodFlashAlpha -= delta;
		}
		
		stage.getBatch().end();
	}
	
	public void showBloodFlash()
	{
		bloodFlashAlpha = 1f;
	}
}
