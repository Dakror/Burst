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
 

package de.dakror.burst.util;

import java.security.InvalidParameterException;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;


/**
 * @author Dakror
 */
public class MultiParticleEffectPool {
	public static final int MAX_POOL_SIZE = 2;
	public static String assetDir;
	
	ObjectMap<String, ParticleEffectPool> pools;
	ObjectMap<String, Array<PooledEffect>> effects;
	
	public MultiParticleEffectPool() {
		this("fx");
	}
	
	public MultiParticleEffectPool(String assetDir) {
		MultiParticleEffectPool.assetDir = assetDir;
		pools = new ObjectMap<>();
		effects = new ObjectMap<>();
	}
	
	public void addPrototype(String name, AssetManager assets) {
		if (pools.containsKey(name)) throw new InvalidParameterException("This effect is already loaded in: " + assetDir + "/" + name);
		
		pools.put(name, new ParticleEffectPool(assets.get(assetDir + "/" + name, ParticleEffect.class), 1, MAX_POOL_SIZE));
		effects.put(name, new Array<PooledEffect>());
	}
	
	public PooledEffect add(String name, float x, float y) {
		return add(name, x, y, 0);
	}
	
	public PooledEffect add(String name, float x, float y, int duration) {
		PooledEffect e = create(name, x, y, duration);
		effects.get(name).add(e);
		return e;
	}
	
	public void add(String name, PooledEffect effect) {
		effects.get(name).add(effect);
	}
	
	public PooledEffect create(String name, float x, float y) {
		return create(name, x, y, 0);
	}
	
	public PooledEffect create(String name, float x, float y, int duration) {
		PooledEffect e = obtain(name);
		e.setPosition(x, y);
		if (duration != 0) e.setDuration(duration);
		return e;
	}
	
	public ParticleEffectPool pool(String name) {
		if (!pools.containsKey(name)) throw new InvalidParameterException("This effect is not loaded in yet: " + assetDir + "/" + name);
		
		return pools.get(name);
	}
	
	public PooledEffect obtain(String name) {
		return pool(name).obtain();
	}
	
	public void draw(SpriteBatch batch, float delta) {
		for (Array<PooledEffect> arr : effects.values()) {
			for (PooledEffect e : arr) {
				e.draw(batch, delta);
				if (e.isComplete()) {
					e.free();
					arr.removeValue(e, true);
				}
			}
		}
	}
}
