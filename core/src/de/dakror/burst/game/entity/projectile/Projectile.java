package de.dakror.burst.game.entity.projectile;

import com.badlogic.gdx.math.Vector2;

import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class Projectile extends Entity
{
	protected Creature source;
	protected final Vector2 velocity = new Vector2();
	protected boolean frozen;
	
	public Projectile(Creature source, float velocityX, float velocityY)
	{
		super(source.getX() + source.getWidth() / 2, source.getY() + source.getHeight() / 2);
		this.source = source;
		
		frozen = false;
		velocity.set(velocityX, velocityY);
	}
	
	public Projectile(Creature source)
	{
		this(source, 0, 0);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if (!frozen) moveBy(velocity.x * delta, velocity.y * delta);
	}
	
	public void setVelocity(float vx, float vy)
	{
		velocity.set(vx, vy);
	}
	
	public Vector2 getVelocity()
	{
		return velocity;
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
