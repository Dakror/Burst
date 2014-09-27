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
public class MultiParticleEffectPool
{
	public static final int MAX_POOL_SIZE = 2;
	
	ObjectMap<String, ParticleEffectPool> pools;
	ObjectMap<String, Array<PooledEffect>> effects;
	
	public MultiParticleEffectPool()
	{
		pools = new ObjectMap<>();
		effects = new ObjectMap<>();
	}
	
	public void addPrototype(String path, AssetManager assets)
	{
		if (pools.containsKey(path)) throw new InvalidParameterException("This effect is already loaded in: " + path);
		
		pools.put(path, new ParticleEffectPool(assets.get(path, ParticleEffect.class), 1, MAX_POOL_SIZE));
	}
	
	public ParticleEffectPool pool(String path)
	{
		if (!pools.containsKey(path)) throw new InvalidParameterException("This effect is not loaded in yet: " + path);
		
		return pools.get(path);
	}
	
	public PooledEffect obtain(String path)
	{
		return pool(path).obtain();
	}
	
	public void draw(SpriteBatch batch, float delta)
	{
		for (Array<PooledEffect> arr : effects.values())
		{
			for (PooledEffect e : arr)
			{
				e.draw(batch, delta);
				if (e.isComplete())
				{
					arr.removeValue(e, true);
					e.free();
				}
			}
		}
	}
}
