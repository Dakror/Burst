package de.dakror.burst.game.entity.creature.enemy;

import de.dakror.burst.Burst;

/**
 * @author Dakror
 */
// FIXME: find name
public class Monster01 extends Enemy {
	public Monster01(float x, float y) {
		super(x, y);
		maxHp = hp = 6;
		setName("Monster 01");
		spriteFg = Burst.img.createSprite("monster01_fg");
		spriteBg = Burst.img.createSprite("monster01_bg");
		speed = 130;
		
		attackDamage = 5;
		
		pulseTime = 1.0f;
		bump.set(51, 35, 48, 80);
	}
}
