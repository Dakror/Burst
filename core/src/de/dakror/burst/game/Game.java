package de.dakror.burst.game;

import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.Player;
import de.dakror.burst.game.entity.enemy.Monster00;
import de.dakror.burst.layer.Layer;

/**
 * @author Dakror
 */
public class Game extends Layer
{
	public static Game instance;
	public static Player player;
	public static OrthographicCamera camera;
	
	public final CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();
	
	String floorTile = "castleMid";
	
	@Override
	public void show()
	{
		instance = this;
		
		camera = new OrthographicCamera();
		stage = new Stage(new ScreenViewport(camera));
		
		player = new Player((Gdx.graphics.getWidth() - 48) / 2, 0, Gdx.graphics.getHeight());
		entities.add(player);
		
		entities.add(new Monster00((Gdx.graphics.getWidth() - 48) / 3, 0, Gdx.graphics.getHeight() / 2));
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
		stage.act();
		stage.draw();
		stage.getBatch().begin();
		
		for (Entity entity : entities)
			entity.render(stage.getBatch(), delta);
		
		stage.getBatch().end();
	}
	
	public void spawnEntity(Entity e)
	{
		entities.add(e);
	}
}
