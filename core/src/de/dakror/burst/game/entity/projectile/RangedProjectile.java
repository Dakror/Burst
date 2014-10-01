package de.dakror.burst.game.entity.projectile;

import com.badlogic.gdx.math.Vector2;

import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class RangedProjectile extends Projectile
{
	float range, rangeDone;
	final Vector2 tmp = new Vector2();
	
	public RangedProjectile(Creature source, float range, float velocityX, float velocityY)
	{
		super(source, velocityX, velocityY);
		this.range = range;
		rangeDone = 0;
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		rangeDone += tmp.set(velocity).scl(delta).len();
		if (rangeDone >= range) dead = true;
	}
	
	public float getRange()
	{
		return range;
	}
}
