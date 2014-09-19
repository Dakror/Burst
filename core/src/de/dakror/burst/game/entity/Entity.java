package de.dakror.burst.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import de.dakror.burst.Burst;
import de.dakror.burst.util.interf.Drawable;
import de.dakror.burst.util.interf.Tickable;

/**
 * @author Dakror
 */
public abstract class Entity implements Drawable, Tickable
{
	public static final float zFac = (float) Math.sqrt(2) / 2;
	
	protected Sprite spriteFg, spriteBg;
	protected String name;
	
	protected int hp, maxHp, level;
	protected float speed;
	protected final Vector3 pos;
	
	protected float pulseTime = 0.5f; // a second
	protected float delta;
	protected int glowSize = 20;
	
	public Entity(float x, float y, float z)
	{
		pos = new Vector3(x, y, z);
		
		level = 0;
		maxHp = hp = 10;
	}
	
	public boolean isDead()
	{
		return hp <= 0;
	}
	
	public Sprite getSpriteFg()
	{
		return spriteFg;
	}
	
	public Sprite getSpriteBg()
	{
		return spriteBg;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Vector3 getPos()
	{
		return pos;
	}
	
	@Override
	public void render(Batch batch, float delta)
	{
		if (isDead() || spriteFg == null) return;
		
		if (spriteBg != null)
		{
			this.delta += delta;
			
			float fac = (float) Math.sin(this.delta * Math.PI / pulseTime);
			float fac2 = (float) Math.cos(this.delta * Math.PI / pulseTime);
			float glowAdd = fac * glowSize;
			float w = spriteBg.getWidth(), h = spriteBg.getHeight();
			
			spriteBg.setX(pos.x - glowAdd / 2);
			spriteBg.setY(pos.y + zFac * pos.z - glowAdd / 2);
			spriteBg.setSize(w + glowAdd, h + glowAdd);
			spriteBg.draw(batch, (fac2 + 1) / 2f);
			spriteBg.setSize(w, h);
			
			if (this.delta > pulseTime) this.delta = -pulseTime;
		}
		
		spriteFg.setX(pos.x);
		spriteFg.setY(pos.y + zFac * pos.z);
		spriteFg.draw(batch);
		
		if (Burst.debug)
		{
			// Burst.shapeRenderer.
		}
	}
	
	public static float len(Vector3 v)
	{
		return (float) Math.sqrt(v.x * v.x + v.y * v.y + 0.25f * v.z * v.z);
	}
	
	public static Vector3 limit(Vector3 v, float len)
	{
		return v.nor().scl(len, len, len * 0.5f);
	}
}
