package de.dakror.burst.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.Burst;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.creature.Player;
import de.dakror.burst.game.entity.creature.enemy.Monster00;
import de.dakror.burst.layer.Layer;
import de.dakror.burst.util.MultiParticleEffectPool;

/**
 * @author Dakror
 */
public class Game extends Layer
{
	public static Game instance;
	public static Player player;
	public static OrthographicCamera camera;
	public static MultiParticleEffectPool particles;
	
	float bloodFlashAlpha = 0;
	
	@Override
	public void show()
	{
		instance = this;
		
		camera = new OrthographicCamera();
		stage = new Stage(new ScreenViewport(camera));
		
		particles = new MultiParticleEffectPool();
		particles.addPrototype("shadow.p", Burst.assets);
		particles.addPrototype("death.p", Burst.assets);
		
		player = new Player((Gdx.graphics.getWidth() - 150) / 2, (Gdx.graphics.getHeight() - 150) / 2);
		Burst.instance.getMultiplexer().addProcessor(0, player);
		spawnEntity(player);
		
		spawnEntity(new Monster00((Gdx.graphics.getWidth() - 48) / 3, (Gdx.graphics.getHeight() - 150) / 2));
		initDone = true;
	}
	
	@Override
	public void update(float delta)
	{
		stage.act();
	}
	
	@Override
	public void render(float delta)
	{
		stage.draw();
		stage.getBatch().begin();
		
		particles.draw((SpriteBatch) stage.getBatch(), delta);
		
		// -- hud / ui -- //
		
		int width = 400;
		Creature.renderHpBar(stage.getBatch(), (Gdx.graphics.getWidth() - width) / 2, 20, width, Game.player.getHpPercentage());
		
		if (bloodFlashAlpha > 0)
		{
			stage.getBatch().setColor(1, 1, 1, bloodFlashAlpha);
			stage.getBatch().draw(Burst.img.findRegion("blood"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			stage.getBatch().setColor(1, 1, 1, 1);
			bloodFlashAlpha -= delta;
		}
		stage.getBatch().end();
	}
	
	public void spawnEntity(Entity e)
	{
		getStage().addActor(e);
	}
	
	public void showBloodFlash()
	{
		bloodFlashAlpha = 1f;
	}
}
