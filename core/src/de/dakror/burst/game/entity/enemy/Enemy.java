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
	
	public Enemy(float x, float y, float z)
	{
		super(x, y, z);
	}
	
	@Override
	public void tick(int tick)
	{
		tmp.set(Game.player.getPos()).sub(pos);
		if (len(tmp) > speed) limit(tmp, speed);
		
		pos.add(tmp);
	}
}
