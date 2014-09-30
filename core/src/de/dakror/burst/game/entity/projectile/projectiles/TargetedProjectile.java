package de.dakror.burst.game.entity.projectile.projectiles;

import com.badlogic.gdx.math.Vector2;

import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.projectile.Projectile;

/**
 * @author Dakror
 */
public class TargetedProjectile extends Projectile
{
	Creature target;
	
	final Vector2 tmp = new Vector2();
	
	public TargetedProjectile(Creature source, Creature target)
	{
		super(source);
		
		this.target = target;
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		tmp.set(target.getPos()).sub(getPos());
		
		boolean reachedEnd = false;
		
		float speed = velocity.len();
		if (tmp.len() > speed * delta) tmp.limit(speed * delta);
		else
		{
			tmp.scl(delta);
			reachedEnd = true;
		}
		
		if (!frozen) moveBy(tmp.x, tmp.y);
		
		if (reachedEnd) dead = true;
	}
	
	public Creature getTarget()
	{
		return target;
	}
}
