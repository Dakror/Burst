package de.dakror.burst.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.util.MultiParticleEffectPool;
import de.dakror.burst.util.interf.Drawable;
import de.dakror.burst.util.interf.Tickable;

/**
 * @author Dakror
 */
public abstract class Entity implements Drawable, Tickable
{
	public static final float lifeBarWidth = 100;
	
	protected Sprite spriteFg, spriteBg;
	protected String name;
	
	protected MultiParticleEffectPool particles;
	
	protected int hp, maxHp, level, attackDamage, attackSpeed;
	protected float speed;
	protected final Vector3 pos;
	
	protected Rectangle bump;
	
	final Rectangle bmp = new Rectangle();
	
	protected float pulseTime = 0.5f; // a second
	protected float delta;
	protected int glowSize = 20;
	protected boolean showHpBar;
	
	public Entity(float x, float y, float z)
	{
		pos = new Vector3(x, y, z);
		
		level = 0;
		maxHp = hp = 10;
		showHpBar = true;
		bump = new Rectangle();
		
		attackDamage = 1;
		attackSpeed = 30;
		
		particles = new MultiParticleEffectPool();
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
	
	public String getName()
	{
		return name;
	}
	
	public Vector3 getPos()
	{
		return pos;
	}
	
	@Override
	public void render(SpriteBatch batch, float delta)
	{
		if (spriteFg == null) return;
		
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
			spriteBg.setY(pos.y + Game.zFac * pos.z - glowAdd / 2);
			spriteBg.setSize(w + glowAdd, h + glowAdd);
			spriteBg.draw(batch, (fac2 + 1) / 2f);
			spriteBg.setSize(w, h);
			
			if (this.delta > pulseTime) this.delta = -pulseTime;
		}
		
		spriteFg.setX(pos.x);
		spriteFg.setY(pos.y + Game.zFac * pos.z);
		spriteFg.draw(batch);
		
		if (maxHp > 0 && showHpBar)
		{
			float x = pos.x + bump.x + (bump.width - lifeBarWidth) / 2;
			float y = pos.y + Game.zFac * pos.z + bump.y + bump.height + 10;
			
			renderHpBar(batch, x, y, lifeBarWidth, hp / (float) maxHp);
		}
		
		particles.draw(batch, delta);
	}
	
	public void debug(Batch batch, float delta2)
	{
		if (Burst.debug)
		{
			Burst.shapeRenderer.identity();
			Burst.shapeRenderer.begin(ShapeType.Line);
			Burst.shapeRenderer.setColor(Color.WHITE);
			Burst.shapeRenderer.rect(pos.x + bump.x, pos.y + Game.zFac * pos.z + bump.y, bump.width, bump.height);
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
		bmp.y += pos.y + Game.zFac * pos.z;
		
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
