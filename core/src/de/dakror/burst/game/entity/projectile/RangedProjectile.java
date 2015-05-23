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
