package de.dakror.burst.game.entity.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class Projectile extends Entity
{
	protected Creature source;
	protected final Vector2 direction = new Vector2();
	protected float velocity;
	protected int damage;
	protected boolean frozen;
	protected boolean dieOnHit;
	protected Class<?> hitable;
	
	protected Array<Creature> appliedTargets;
	
	public Projectile(Creature source, float directionX, float directionY)
	{
		super(source.getX() + source.getWidth() / 2, source.getY() + source.getHeight() / 2);
		this.source = source;
		
		frozen = false;
		direction.set(directionX, directionY).nor();
		
		appliedTargets = new Array<>();
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if (!frozen)
		{
			moveBy(direction.x * velocity * delta, direction.y * velocity * delta);
			
			for (Actor e : Game.instance.getStage().getActors())
			{
				if (e instanceof Creature && !((Creature) e).isDead() && e != this && !appliedTargets.contains((Creature) e, true))
				{
					if (intersects((Creature) e) && (hitable == null || hitable.isAssignableFrom(e.getClass())))
					{
						((Creature) e).dealDamage(damage, direction.angle(), source);
						appliedTargets.add((Creature) e);
						if (dieOnHit)
						{
							dead = true;
							break;
						}
					}
				}
			}
		}
	}
	
	public void setDirection(float vx, float vy)
	{
		direction.set(vx, vy);
	}
	
	public Vector2 getDirection()
	{
		return direction;
	}
	
	public float getVelocity()
	{
		return velocity;
	}
	
	public void setVelocity(float velocity)
	{
		this.velocity = velocity;
	}
	
	public boolean isFrozen()
	{
		return frozen;
	}
	
	public void setFrozen(boolean frozen)
	{
		this.frozen = frozen;
	}
	
	public Creature getSource()
	{
		return source;
	}
}
