package de.dakror.burst.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.Burst;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Player;
import de.dakror.burst.game.entity.creature.enemy.Monster00;
import de.dakror.burst.game.entity.projectile.Projectile;
import de.dakror.burst.layer.HudLayer;
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
	
	public static HudLayer hud;
	
	int kills;
	boolean noWave;
	long time;
	
	@Override
	public void show()
	{
		instance = this;
		
		camera = new OrthographicCamera();
		stage = new Stage(new ScreenViewport(camera));
		
		particles = new MultiParticleEffectPool();
		particles.addPrototype("shadow.p", Burst.assets);
		particles.addPrototype("death.p", Burst.assets);
		particles.addPrototype("blood.p", Burst.assets);
		
		player = new Player((Gdx.graphics.getWidth() - 150) / 2, (Gdx.graphics.getHeight() - 150) / 2);
		Burst.instance.getMultiplexer().addProcessor(0, player);
		spawnEntity(player);
		
		spawnEnemy();
		initDone = true;
	}
	
	@Override
	public void update(float delta)
	{
		stage.act();
		if (System.currentTimeMillis() - time > 3000 && noWave)
		{
			for (int i = 0; i < MathUtils.random(1, 3); i++)
				spawnEnemy();
			noWave = false;
		}
	}
	
	@Override
	public void render(float delta)
	{
		stage.draw();
		stage.getBatch().begin();
		
		particles.draw((SpriteBatch) stage.getBatch(), delta);
		
		stage.getBatch().end();
	}
	
	public void spawnEnemy()
	{
		int side = MathUtils.random(0, 3);
		float ranX = MathUtils.random(0, Gdx.graphics.getWidth() - 150);
		float ranY = MathUtils.random(0, Gdx.graphics.getHeight() - 150);
		
		float x = 0, y = 0;
		
		if (side == 0) // left
		{
			x = -150;
			y = ranY;
		}
		if (side == 1) // top
		{
			x = ranX;
			y = Gdx.graphics.getHeight();
		}
		if (side == 2) // right
		{
			x = Gdx.graphics.getWidth();
			y = ranY;
		}
		if (side == 3) // bottom
		{
			x = ranX;
			y = -150;
		}
		
		spawnEntity(new Monster00(x, y));
	}
	
	public void spawnEntity(Entity e)
	{
		if (e instanceof Projectile) e.moveBy(-e.getSpriteFg().getWidth() / 2, -e.getSpriteFg().getHeight() / 2);
		getStage().addActor(e);
	}
	
	public void addKill()
	{
		kills++;
		int next = MathUtils.random(0, 2);
		noWave = next == 0;
		if (noWave) time = System.currentTimeMillis();
		for (int i = 0; i < next; i++)
			spawnEnemy();
	}
	
	public int getKills()
	{
		return kills;
	}
}
