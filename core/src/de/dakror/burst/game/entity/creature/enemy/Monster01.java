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
