package de.dakror.burst.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.dakror.burst.Burst;
import de.dakror.burst.game.skill.Skill;
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
	protected float speed, attackTime, attackRange;
	
	protected Rectangle bump;
	
	final Rectangle bmp = new Rectangle();
	final Vector2 tmp = new Vector2();
	
	final Vector2 attack = new Vector2();
	float attackProgress;
	boolean attacked;
	Entity target;
	
	protected Skill activeSkill;
	protected float pulseTime = 0.5f; // * 1 second
	protected float delta;
	protected int glowSize = 20;
	protected boolean showHpBar;
	protected boolean hovered;
	
	public Entity(float x, float y)
	{
		setPosition(x, y);
		
		level = 0;
		maxHp = hp = 10;
		showHpBar = true;
		bump = new Rectangle();
		
		attackDamage = 1;
		attackTime = 0.75f;
		attackRange = 20;
		
		particles = new MultiParticleEffectPool();
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		this.delta += delta;
		
		if (getActions().size == 0) activeSkill = null;
		
		hovered = getAbsoluteBump().contains(Gdx.input.getX(), Gdx.input.getY());
		
		if (attack.len() > 0)
		{
			attackProgress += delta;
			
			if (attackProgress / attackTime >= 0.3f && !attacked)
			{
				target.dealDamage(attackDamage);
				attacked = true;
			}
			
			if (attackProgress / attackTime > 1)
			{
				attack.setZero();
				attackProgress = 0;
				attacked = false;
			}
		}
		
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
	
	public Vector2 getPos()
	{
		return tmp.set(getX(), getY());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		if (spriteFg == null || !isVisible()) return;
		
		if (bump.width == 0)
		{
			bump.width = spriteFg.getWidth();
			bump.height = spriteFg.getHeight();
		}
		
		float x = getX();
		float y = getY();
		
		if (attack.len() != 0)
		{
			float percentage = attackProgress / attackTime;
			float limit = percentage < 0.5f ? percentage * -5 : (float) Math.sin(attackProgress * -Math.PI / attackTime);
			tmp.set(attack).limit(Math.max(-attack.len() + 0.00001f, limit * attackRange));
			x += tmp.x;
			y += tmp.y;
		}
		
		if (spriteBg != null)
		{
			float fac = (float) Math.sin(delta * Math.PI / pulseTime);
			float fac2 = (float) Math.cos(delta * Math.PI / pulseTime);
			float glowAdd = fac * glowSize;
			float w = spriteBg.getWidth(), h = spriteBg.getHeight();
			
			spriteBg.setX(x - glowAdd / 2);
			spriteBg.setY(y - glowAdd / 2);
			spriteBg.setSize(w + glowAdd, h + glowAdd);
			spriteBg.draw(batch, (fac2 + 1) / 2f);
			spriteBg.setSize(w, h);
		}
		
		spriteFg.setX(x);
		spriteFg.setY(y);
		spriteFg.draw(batch);
		
		if (maxHp > 0 && showHpBar)
		{
			float x1 = x + bump.x + (bump.width - lifeBarWidth) / 2;
			float y1 = y + bump.y + bump.height + 10;
			
			renderHpBar(batch, x1, y1, lifeBarWidth, hp / (float) maxHp);
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
		bmp.y += getY();
		
		return bmp;
	}
	
	public Rectangle getBump()
	{
		return bump;
	}
	
	/**
	 * Changing return value has no effect
	 * 
	 * @return
	 */
	public Rectangle getAbsoluteAttackSpace()
	{
		bmp.set(bump);
		bmp.x += getX() - attackRange;
		bmp.y += getY() - attackRange;
		bmp.width += 2 * attackRange;
		bmp.height += 2 * attackRange;
		
		return bmp;
	}
	
	public boolean intersects(Entity o)
	{
		return getAbsoluteBump().overlaps(o.getAbsoluteBump());
	}
	
	public boolean intersects(Entity o, Vector2 tr)
	{
		Rectangle obmp = o.getAbsoluteBump();
		obmp.x += tr.x;
		obmp.y += tr.y;
		return getAbsoluteBump().overlaps(obmp);
	}
	
	public MultiParticleEffectPool getParticles()
	{
		return particles;
	}
	
	/**
	 * @param o
	 * @param tr
	 * @return true if o is in this entity's attack range
	 */
	public boolean isInAttackRange(Entity o, Vector2 tr)
	{
		Rectangle obmp = o.getAbsoluteBump();
		obmp.x += tr.x;
		obmp.y += tr.y;
		return getAbsoluteAttackSpace().overlaps(obmp);
	}
	
	public void onRemoval()
	{}
	
	protected void setSkill(Skill skill)
	{
		activeSkill = skill;
		
		addAction(activeSkill.getSequence());
	}
	
	public float getAttackRange()
	{
		return attackRange;
	}
	
	public void attack(Entity e)
	{
		target = e;
		attack.set(getPos().sub(e.getPos()).limit(attackRange));
		attackProgress = 0;
		attacked = false;
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
}
