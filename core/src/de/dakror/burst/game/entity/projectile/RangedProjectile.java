package de.dakror.burst.game.entity.projectile;

import com.badlogic.gdx.math.Vector2;

import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class RangedProjectile extends Projectile {
	protected float range, rangeDone;
	protected final Vector2 tmp = new Vector2();
	
	public RangedProjectile(Creature source, float directionX, float directionY) {
		super(source, directionX, directionY);
		rangeDone = 0;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		rangeDone += tmp.set(direction).scl(velocity).scl(delta).len();
		if (rangeDone >= range) dead = true;
	}
	
	public float getRange() {
		return range;
	}
}
