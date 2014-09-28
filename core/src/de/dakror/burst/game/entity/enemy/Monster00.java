package de.dakror.burst.game.entity.enemy;

import de.dakror.burst.Burst;

/**
 * @author Dakror
 */
// FIXME: find name
public class Monster00 extends Enemy
{
	public Monster00(float x, float y)
	{
		super(x, y);
		maxHp = hp = 10;
		name = "Monster 00";
		spriteFg = Burst.img.createSprite("monster00_fg");
		spriteBg = Burst.img.createSprite("monster00_bg");
		speed = 90;
		
		attackDamage = 3;
		
		pulseTime = 0.75f;
		bump.set(51, 35, 48, 80);
	}
}
