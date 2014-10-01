package de.dakror.burst.game.entity.creature;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.skill.Skill;
import de.dakror.burst.util.MultiParticleEffectPool;

/**
 * @author Dakror
 */
public abstract class Creature extends Entity
{
	public static final float lifeBarWidth = 100;
	
	protected MultiParticleEffectPool particles;
	
	protected int hp, maxHp, level, attackDamage;
	protected float speed, attackTime, attackRange;
	
	final Vector2 attack = new Vector2();
	float attackProgress;
	int nextAttackDamage;
	float nextAttackAmpl;
	boolean attacked;
	public boolean attackDone;
	Creature target;
	
	protected Skill activeSkill;
	protected boolean showHpBar;
	
	public Creature(float x, float y)
	{
		super(x, y);
		level = 0;
		maxHp = hp = 10;
		showHpBar = true;
		
		attackDamage = 1;
		attackTime = 0.75f;
		attackRange = 20;
		
		particles = new MultiParticleEffectPool();
		
		addListener(new InputListener()
		{
			@Override
			public boolean mouseMoved(InputEvent event, float x, float y)
			{
				if (Burst.smartCast && bump.contains(x, getHeight() - y)) Game.player.activateSelectedSkill(Creature.this);
				return false;
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				if (bump.contains(x, y))
				{
					if (Game.player.getSelectedSkill() != null) Game.player.activateSelectedSkill(Creature.this);
					else if (Game.player != Creature.this) Game.player.requestAutoAttack(Creature.this);
				}
				return false;
			}
		});
	}
	
	@Override
	public void act(float delta)
	{
		dead = hp <= 0;
		
		super.act(delta);
		
		if (getActions().size == 0) activeSkill = null;
		
		if (attack.len() > 0)
		{
			attackProgress += delta;
			
			if (attackProgress / attackTime >= 0.3f && !attacked)
			{
				target.dealDamage(nextAttackDamage > 0 ? nextAttackDamage : Math.round(nextAttackAmpl * attackDamage));
				attacked = true;
			}
			
			if (attackProgress / attackTime > 1)
			{
				attack.setZero();
				attackProgress = 0;
				attacked = false;
				nextAttackDamage = 0;
				nextAttackAmpl = 0;
				attackDone = true;
			}
		}
	}
	
	public float getHpPercentage()
	{
		return hp / (float) maxHp;
	}
	
	public void dealDamage(int dmg)
	{
		hp = Math.max(0, hp - dmg);
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
			float w = getWidth(), h = getHeight();
			
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
	public Rectangle getAbsoluteAttackSpace()
	{
		bmp.set(bump);
		bmp.x += getX() - attackRange;
		bmp.y += getY() - attackRange;
		bmp.width += 2 * attackRange;
		bmp.height += 2 * attackRange;
		
		return bmp;
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
	
	@Override
	public void onRemoval()
	{
		Game.particles.add("death.p", getX() + 75, getY() + 75);
	}
	
	protected void setSkill(Skill skill)
	{
		activeSkill = skill;
		
		addAction(activeSkill.getSequence());
	}
	
	public float getAttackRange()
	{
		return attackRange;
	}
	
	public void attack(Creature e)
	{
		attack(e, attackDamage);
	}
	
	public void attack(Creature e, int damage)
	{
		attackDone = false;
		target = e;
		attack.set(getPos().sub(e.getPos()).limit(attackRange));
		attackProgress = 0;
		attacked = false;
		
		nextAttackDamage = damage;
	}
	
	public void attack(Creature e, float ampl)
	{
		attackDone = false;
		target = e;
		attack.set(getPos().sub(e.getPos()).limit(attackRange));
		attackProgress = 0;
		attacked = false;
		
		nextAttackAmpl = ampl;
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
