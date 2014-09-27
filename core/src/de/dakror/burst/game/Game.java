package de.dakror.burst.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.Burst;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.Player;
import de.dakror.burst.game.entity.enemy.Monster00;
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
	
	public final Array<Entity> entities = new Array<Entity>();
	
	TiledDrawable floor, floorPersp;
	public static final float zFac = 0.8f;
	
	
	float bloodFlashAlpha = 0;
	
	@Override
	public void show()
	{
		instance = this;
		
		camera = new OrthographicCamera();
		stage = new Stage(new ScreenViewport(camera));
		
		particles = new MultiParticleEffectPool();
		particles.addPrototype("fx/shadow.p", Burst.assets);
		
		// floor = new TiledDrawable(Burst.img.findRegion("floor"));
		// floorPersp = new TiledDrawable(Burst.img.findRegion("floorPersp"));
		
		player = new Player((Gdx.graphics.getWidth() - 150) / 2, 0, Gdx.graphics.getHeight() / 2);
		entities.add(player);
		
		entities.add(new Monster00((Gdx.graphics.getWidth() - 48) / 3, 0, Gdx.graphics.getHeight() / 2));
		initDone = true;
	}
	
	@Override
	public void update(float delta)
	{
		for (Entity entity : entities)
		{
			if (!(entity instanceof Player)) entity.update(delta);
			if (entity.isDead())
			{
				entity.onRemoval();
				entities.removeValue(entity, true);
			}
		}
		
		player.update(delta);
	}
	
	@Override
	public void render(float delta)
	{
		stage.act();
		stage.draw();
		stage.getBatch().begin();
		
		// floor.draw(stage.getBatch(), 0, zFac * Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), (1 - zFac) * Gdx.graphics.getHeight());
		// floorPersp.draw(stage.getBatch(), 0, 0, Gdx.graphics.getWidth(), zFac * Gdx.graphics.getHeight());
		for (Entity entity : entities)
			entity.render((SpriteBatch) stage.getBatch(), delta);
		
		particles.draw((SpriteBatch) stage.getBatch(), delta);
		
		// -- hud / ui -- //
		
		int width = 400;
		Entity.renderHpBar(stage.getBatch(), (Gdx.graphics.getWidth() - width) / 2, 20, width, Game.player.getHpPercentage());
		
		if (bloodFlashAlpha > 0)
		{
			stage.getBatch().setColor(1, 1, 1, bloodFlashAlpha);
			stage.getBatch().draw(Burst.img.findRegion("blood"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			stage.getBatch().setColor(1, 1, 1, 1);
			bloodFlashAlpha -= delta;
		}
		stage.getBatch().end();
		
		// -- hud / ui END -- //
		
		for (Entity entity : entities)
			entity.debug(stage.getBatch(), delta);
	}
	
	public void spawnEntity(Entity e)
	{
		entities.add(e);
	}
	
	public void showBloodFlash()
	{
		bloodFlashAlpha = 1f;
	}
}
