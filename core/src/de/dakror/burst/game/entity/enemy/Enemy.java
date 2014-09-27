package de.dakror.burst.game.entity.enemy;

import com.badlogic.gdx.math.Vector3;

import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public class Enemy extends Entity
{
	final Vector3 tmp = new Vector3();
	
	protected long touchStart;
	
	public Enemy(float x, float y, float z)
	{
		super(x, y, z);
	}
	
	@Override
	public void update(float delta)
	{
		tmp.set(Game.player.getPos()).sub(pos);
		if (len(tmp) > speed) limit(tmp, speed);
		
		if (Game.player.intersects(this, tmp))
		{
			if (touchStart == 0) touchStart = System.currentTimeMillis();
			onPlayerTouch(delta);
		}
		else
		{
			pos.add(tmp);
			touchStart = 0;
		}
	}
	
	public void onPlayerTouch(float delta)
	{}
}
