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

import com.badlogic.gdx.scenes.scene2d.Action;

import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class AttackAction extends Action {
	float ampl;
	float time;
	int damage;
	Creature target;
	boolean queued;
	
	public AttackAction(Creature target) {
		this(0, target);
	}
	
	public AttackAction(int damage, Creature target) {
		this.damage = damage;
		this.target = target;
		
		queued = false;
	}
	
	public AttackAction(float ampl, Creature target) {
		this.ampl = ampl;
		this.target = target;
		
		queued = false;
	}
	
	public AttackAction setTime(float time) {
		this.time = time;
		return this;
	}
	
	@Override
	public boolean act(float delta) {
		if (!queued) {
			if (damage > 0) {
				if (time > 0) ((Creature) actor).attack(target, damage, time);
				else ((Creature) actor).attack(target, damage);
			} else if (ampl > 1) {
				if (time > 0) ((Creature) actor).attack(target, ampl, time);
				else ((Creature) actor).attack(target, ampl);
			} else ((Creature) actor).attack(target);
			queued = true;
		}
		return ((Creature) actor).attackDone;
	}
}
