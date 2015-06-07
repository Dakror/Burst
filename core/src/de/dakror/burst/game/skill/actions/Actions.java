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


package de.dakror.burst.game.skill.actions;

import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class Actions {
	public static EntityAction entity(Entity entity) {
		return new EntityAction(entity);
	}
	
	public static AttackAction attack(Creature target) {
		return new AttackAction(target);
	}
	
	public static AttackAction attack(Creature target, float time) {
		return new AttackAction(target).setTime(time);
	}
	
	public static AttackAction attack(int damage, Creature target) {
		return new AttackAction(damage, target);
	}
	
	public static AttackAction attack(int damage, Creature target, float time) {
		return new AttackAction(damage, target).setTime(time);
	}
	
	public static AttackAction attack(float ampl, Creature target) {
		return new AttackAction(ampl, target);
	}
	
	public static AttackAction attack(float ampl, Creature target, float time) {
		return new AttackAction(ampl, target).setTime(time);
	}
	
	public static ParticleAction particle(String name, float x, float y) {
		return particle(name, x, y, 0);
	}
	
	public static ParticleAction particle(String name, float x, float y, int duration) {
		return new ParticleAction(name, x, y);
	}
}
