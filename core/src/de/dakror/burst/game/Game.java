package de.dakror.burst.game;

import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.layer.Layer;

/**
 * @author Dakror
 */
public class Game extends Layer
{
	public static Game instance;
	
	OrthographicCamera camera;
	SpriteBatch batch;
	CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
	
	@Override
	public void show()
	{
		instance = this;
		
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
	}
	
	@Override
	public void resize(int width, int height)
	{
		camera.setToOrtho(false, width, height);
	}
	
	@Override
	public void tick(int tick)
	{
		for (Entity entity : entities)
			entity.tick(tick);
	}
	
	@Override
	public void render(float delta)
	{
		batch.begin();
		
		for (Entity entity : entities)
			entity.render(batch, delta);
		
		batch.end();
	}
	
	public void spawnEntity(Entity e)
	{
		entities.add(e);
	}
}
