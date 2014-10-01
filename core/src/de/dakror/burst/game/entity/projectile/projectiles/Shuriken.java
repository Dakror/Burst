package de.dakror.burst.game.entity.projectile.projectiles;

import de.dakror.burst.Burst;
import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.projectile.RangedProjectile;

/**
 * @author Dakror
 */
public class Shuriken extends RangedProjectile
{
	public Shuriken(Creature source, float velocityX, float velocityY)
	{
		super(source, 135, velocityX, velocityY);
		
		spriteFg = Burst.img.createSprite("circle");
	}
}
