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
public class TargetedProjectile extends Projectile {
	Creature target;
	
	final Vector2 tmp = new Vector2();
	
	public TargetedProjectile(Creature source, Creature target, float velocityX, float velocityY) {
		super(source, velocityX, velocityY);
		
		this.target = target;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		tmp.set(target.getPos()).sub(getPos());
		
		boolean reachedEnd = false;
		
		float speed = direction.len();
		if (tmp.len() > speed * delta) tmp.limit(speed * delta);
		else {
			tmp.scl(delta);
			reachedEnd = true;
		}
		
		if (!frozen) moveBy(tmp.x, tmp.y);
		
		if (reachedEnd) dead = true;
	}
	
	public Creature getTarget() {
		return target;
	}
}
