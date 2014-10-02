package de.dakror.burst.game.entity.projectile.projectiles;

import de.dakror.burst.Burst;
import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.creature.enemy.Enemy;
import de.dakror.burst.game.entity.projectile.RangedProjectile;

/**
 * @author Dakror
 */
public class Shuriken extends RangedProjectile
{
	public Shuriken(Creature source, float directionX, float directionY)
	{
		super(source, directionX, directionY);
		
		spriteFg = Burst.img.createSprite("shuriken");
		spriteFg.setSize(32, 32);
		spriteFg.setOrigin(16, 16);
		velocity = 1000f;
		range = 250f;
		
		damage = 8;
		hitable = Enemy.class;
		dieOnHit = false;
		
		bump.set(0, 0, 32, 32);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if (!frozen) spriteFg.rotate(360 * delta);
	}
}
