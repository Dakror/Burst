package de.dakror.burst.game.entity.enemy;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;

/**
 * @author Dakror
 */
// FIXME: find name
public class Monster00 extends Enemy
{
	public Monster00(float x, float y, float z)
	{
		super(x, y, z);
		maxHp = hp = 10;
		name = "Monster 00";
		spriteFg = Burst.img.createSprite("monster00_fg");
		spriteBg = Burst.img.createSprite("monster00_bg");
		speed = 1.5f;
		
		pulseTime = 0.75f;
		bump.set(51, 35, 48, 80);
	}
	
	@Override
	public void onPlayerTouch(float delta)
	{
		if (((System.currentTimeMillis() - touchStart) / 1000f) % attackTime == 0) Game.player.dealDamage(attackDamage);
	}
}
