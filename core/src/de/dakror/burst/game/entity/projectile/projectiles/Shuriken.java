/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.burst.game.entity.projectile.projectiles;

import de.dakror.burst.Burst;
import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.creature.enemy.Enemy;
import de.dakror.burst.game.entity.projectile.RangedProjectile;

/**
 * @author Dakror
 */
public class Shuriken extends RangedProjectile {
	public Shuriken(Creature source, float directionX, float directionY) {
		super(source, directionX, directionY);
		
		spriteFg = Burst.img.createSprite("shuriken");
		spriteFg.setSize(32, 32);
		spriteFg.setOrigin(16, 16);
		velocity = 1000f;
		range = 300f;
		
		damage = 8;
		hitable = Enemy.class;
		dieOnHit = false;
		
		bump.set(0, 0, 32, 32);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (!frozen) spriteFg.rotate(360 * delta);
	}
}
