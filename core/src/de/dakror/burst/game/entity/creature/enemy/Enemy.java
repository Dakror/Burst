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

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class Enemy extends Creature {
	final Vector2 tmp = new Vector2();
	
	protected long touchStart;
	
	public Enemy(float x, float y) {
		super(x, y);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (Game.player.isVisible()) {
			tmp.set(Game.player.getPos()).sub(getPos());
			
			if (tmp.len() > speed * delta) tmp.limit(speed * delta);
			else tmp.scl(delta);
			
			if (Game.player.isInAttackRange(this, tmp) && !Game.player.isDead()) {
				if (touchStart == 0) touchStart = System.currentTimeMillis();
				onPlayerTouch(delta);
			} else {
				float deltaX = tmp.x, deltaY = tmp.y;
				
				for (Actor e : Game.instance.getStage().getActors()) {
					if (e instanceof Creature && !((Entity) e).isDead() && e != this) {
						if (intersects((Entity) e, tmp.set(-deltaX, 0))) deltaX = 0;
						if (intersects((Entity) e, tmp.set(0, -deltaY))) deltaY = 0;
						
						if (deltaX == 0 && deltaY == 0) break;
					}
				}
				
				moveBy(deltaX, deltaY);
				touchStart = 0;
			}
		}
	}
	
	@Override
	public void dealDamage(int dmg, float angleDegrees, Entity source) {
		super.dealDamage(dmg, angleDegrees, source);
		
		PooledEffect e = Game.particles.create("blood.p", getX() + 75, getY() + 75);
		e.getEmitters().first().getAngle().setHighMin(angleDegrees - 45);
		e.getEmitters().first().getAngle().setHighMax(angleDegrees + 45);
		Game.particles.add("blood.p", e);
	}
	
	public void onPlayerTouch(float delta) {
		if (Math.round((System.currentTimeMillis() - touchStart) / 1000f) >= attackTime) {
			attack(Game.player);
			touchStart = System.currentTimeMillis() + Math.round(attackTime * 1000);
		}
	}
}
