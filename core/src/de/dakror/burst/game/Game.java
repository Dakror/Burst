package de.dakror.burst.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.Burst;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Player;
import de.dakror.burst.game.entity.creature.enemy.Monster00;
import de.dakror.burst.game.entity.projectile.Projectile;
import de.dakror.burst.game.skill.SkillType;
import de.dakror.burst.layer.HudLayer;
import de.dakror.burst.layer.Layer;
import de.dakror.burst.util.D;
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
	public static boolean paused = false;
	public static HudLayer hud;
	
	int kills;
	boolean noWave;
	long time;
	
	float runTime;
	
	public boolean anyCreatureTargeted;
	
	ShaderProgram plasma;
	
	final Vector2 resCache = new Vector2();
	
	@Override
	public void show()
	{
		instance = this;
		
		plasma = new ShaderProgram(Gdx.files.internal("shader/plasma.vs"), Gdx.files.internal("shader/plasma.fs"));
		if (!plasma.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + plasma.getLog());
		
		camera = new OrthographicCamera(1920, 1080);
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
		if (!paused)
		{
			stage.act(delta);
			if (System.currentTimeMillis() - time > 3000 && noWave)
			{
				for (int i = 0; i < MathUtils.random(1, 3); i++)
					spawnEnemy();
				noWave = false;
			}
		}
	}
	
	@Override
	public void render(float delta)
	{
		stage.getBatch().begin();
		
		stage.getBatch().setShader(plasma);
		plasma.setUniformf("u_time", runTime += delta);
		plasma.setUniformf("u_resolution", resCache.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		plasma.setUniformf("u_speed", 0.5f);
		
		stage.getBatch().draw(Burst.assets.get("img/background.png", Texture.class), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		stage.getBatch().end();
		
		stage.getBatch().setShader(null);
		
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
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		if (!anyCreatureTargeted && player.getSelectedSkill() != null && player.getSelectedSkill().getType() != SkillType.Targeted && (D.android() || button == Buttons.LEFT))
		{
			player.activateSelectedSkill(null);
		}
		
		anyCreatureTargeted = false;
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		if (keycode == Keys.SPACE) paused = !paused;
		return false;
	}
}
