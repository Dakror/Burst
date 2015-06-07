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

import de.dakror.burst.game.Game;

/**
 * @author Dakror
 */
public class ParticleAction extends Action {
	String name;
	int duration;
	float x, y;
	
	public ParticleAction(String name, float x, float y) {
		this(name, x, y, 0);
	}
	
	public ParticleAction(String name, float x, float y, int duration) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean act(float delta) {
		Game.particles.add(name, x + actor.getX(), y + actor.getY(), duration);
		return true;
	}
}
