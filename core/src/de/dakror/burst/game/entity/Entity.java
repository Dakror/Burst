package de.dakror.burst.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import de.dakror.burst.Burst;
import de.dakror.burst.util.interf.Drawable;
import de.dakror.burst.util.interf.Tickable;

/**
 * @author Dakror
 */
public abstract class Entity implements Drawable, Tickable
{
	public static final float zFac = 0.8f;
	public static final float lifeBarWidth = 100;
	
	protected Sprite spriteFg, spriteBg;
	protected String name;
	
	protected int hp, maxHp, level;
	protected float speed;
	protected final Vector3 pos;
	
	protected Rectangle bump;
	
	final Rectangle bmp = new Rectangle();
	
	protected float pulseTime = 0.5f; // a second
	protected float delta;
	protected int glowSize = 20;
	
	public Entity(float x, float y, float z)
	{
		pos = new Vector3(x, y, z);
		
		level = 0;
		maxHp = hp = 10;
		
		bump = new Rectangle();
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
		
		if (bump.width == 0)
		{
			bump.width = spriteFg.getWidth();
			bump.height = spriteFg.getHeight();
		}
		
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
		
		if (maxHp > 0)
		{}
	}
	
	public void debug(Batch batch, float delta2)
	{
		if (Burst.debug)
		{
			Burst.shapeRenderer.identity();
			Burst.shapeRenderer.begin(ShapeType.Line);
			Burst.shapeRenderer.setColor(Color.WHITE);
			Burst.shapeRenderer.rect(pos.x + bump.x, pos.y + zFac * pos.z + bump.y, bump.width, bump.height);
			Burst.shapeRenderer.end();
		}
	}
	
	/**
	 * Changing return value has no effect
	 * 
	 * @return
	 */
	public Rectangle getAbsoluteBump()
	{
		bmp.set(bump);
		bmp.x += pos.x;
		bmp.y += pos.y + zFac * pos.z;
		
		return bmp;
	}
	
	public boolean intersects(Entity o)
	{
		return getAbsoluteBump().overlaps(o.getAbsoluteBump());
	}
	
	public boolean intersects(Entity o, Vector3 tr)
	{
		Rectangle obmp = o.getAbsoluteBump();
		obmp.x += tr.x;
		obmp.y += tr.y + zFac * tr.z;
		return getAbsoluteBump().overlaps(obmp);
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
