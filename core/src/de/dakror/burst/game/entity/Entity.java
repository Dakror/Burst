package de.dakror.burst.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.util.MultiParticleEffectPool;

/**
 * @author Dakror
 */
public abstract class Entity extends Actor
{
	public static final float lifeBarWidth = 100;
	
	protected Sprite spriteFg, spriteBg;
	protected String name;
	
	protected MultiParticleEffectPool particles;
	
	protected int hp, maxHp, level, attackDamage;
	protected float speed, attackTime;
	float z;
	
	protected Rectangle bump;
	
	final Rectangle bmp = new Rectangle();
	final Vector3 tmp = new Vector3();
	
	protected float pulseTime = 0.5f; // * 1 second
	protected float delta;
	protected int glowSize = 20;
	protected boolean showHpBar;
	
	public Entity(float x, float y, float z)
	{
		setPosition(x, y);
		this.z = z;
		
		level = 0;
		maxHp = hp = 10;
		showHpBar = true;
		bump = new Rectangle();
		
		attackDamage = 1;
		attackTime = 0.75f;
		
		particles = new MultiParticleEffectPool();
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if (isDead())
		{
			onRemoval();
			remove();
		}
	}
	
	public boolean isDead()
	{
		return hp <= 0;
	}
	
	public float getHpPercentage()
	{
		return hp / (float) maxHp;
	}
	
	public void dealDamage(int dmg)
	{
		hp = Math.max(0, hp - dmg);
	}
	
	public Sprite getSpriteFg()
	{
		return spriteFg;
	}
	
	public Sprite getSpriteBg()
	{
		return spriteBg;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	public float getZ()
	{
		return z;
	}
	
	public Vector3 getPos()
	{
		return tmp.set(getX(), getY(), getZ());
	}
	
	public void moveBy(float x, float y, float z)
	{
		super.moveBy(x, y);
		this.z += z;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (spriteFg == null) return;
		
		if (bump.width == 0)
		{
			bump.width = spriteFg.getWidth();
			bump.height = spriteFg.getHeight();
		}
		
		if (spriteBg != null)
		{
			delta += delta;
			
			float fac = (float) Math.sin(delta * Math.PI / pulseTime);
			float fac2 = (float) Math.cos(delta * Math.PI / pulseTime);
			float glowAdd = fac * glowSize;
			float w = spriteBg.getWidth(), h = spriteBg.getHeight();
			
			spriteBg.setX(getX() - glowAdd / 2);
			spriteBg.setY(getY() + Game.zFac * z - glowAdd / 2);
			spriteBg.setSize(w + glowAdd, h + glowAdd);
			spriteBg.draw(batch, (fac2 + 1) / 2f);
			spriteBg.setSize(w, h);
			
			if (delta > pulseTime) delta = -pulseTime;
		}
		
		spriteFg.setX(getX());
		spriteFg.setY(getY() + Game.zFac * z);
		spriteFg.draw(batch);
		
		if (maxHp > 0 && showHpBar)
		{
			float x = getX() + bump.x + (bump.width - lifeBarWidth) / 2;
			float y = getY() + Game.zFac * getZ() + bump.y + bump.height + 10;
			
			renderHpBar(batch, x, y, lifeBarWidth, hp / (float) maxHp);
		}
		
		particles.draw((SpriteBatch) batch, delta);
	}
	
	/**
	 * Changing return value has no effect
	 * 
	 * @return
	 */
	public Rectangle getAbsoluteBump()
	{
		bmp.set(bump);
		bmp.x += getX();
		bmp.y += getY() + Game.zFac * getZ();
		
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
		obmp.y += tr.y + Game.zFac * tr.z;
		return getAbsoluteBump().overlaps(obmp);
	}
	
	public void onRemoval()
	{}
	
	// -- statics -- //
	
	static final String[] regs = { "BarBase", "Bar-ff3232" };
	
	public static void renderHpBar(Batch batch, float x, float y, float width, float percentage)
	{
		if (percentage == 0) return;
		for (int i = 0; i < regs.length; i++)
		{
			float percent = i == 0 ? 1 : percentage;
			AtlasRegion ar = Burst.img.findRegion(regs[i]);
			ar.setRegionWidth(6);
			batch.draw(ar, x, y);
			int rx = ar.getRegionX();
			ar.setRegionX(rx + 6);
			ar.setRegionWidth(1);
			batch.draw(ar, x + 6, y, (width - 12) * percent, 23);
			ar.setRegionX(rx + 7);
			ar.setRegionWidth(6);
			batch.draw(ar, x + (width - 12) * percent + 6, y);
			ar.setRegionX(rx);
			ar.setRegionWidth(13);
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
