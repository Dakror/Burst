package de.dakror.burst.game.entity.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.skill.Skill;
import de.dakror.burst.game.skill.skills.ShadowJump;
import de.dakror.burst.util.D;

/**
 * @author Dakror
 */
public class Player extends Creature implements InputProcessor
{
	final Vector2 tmp = new Vector2();
	
	float destAnimTime = 0.35f;
	float destProgress;
	
	final Vector2 dest = new Vector2();
	
	public Skill selectedSkill;
	
	boolean autoAttackRequested;
	Creature autoAttackRequestedTarget;
	
	public Player(float x, float y)
	{
		super(x, y);
		maxHp = hp = 20;
		setName("Player");
		spriteFg = Burst.img.createSprite("player_fg");
		spriteBg = Burst.img.createSprite("player_bg");
		speed = 180;
		showHpBar = false;
		
		attackDamage = 2;
		
		bump.set(51, 35, 48, 80);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if (autoAttackRequested)
		{
			if (isInAttackRange(autoAttackRequestedTarget, Vector2.Zero))
			{
				attack(autoAttackRequestedTarget);
				autoAttackRequested = false;
			}
		}
		
		if (attackDone && autoAttackRequestedTarget != null && !autoAttackRequested) autoAttackRequestedTarget = null;
		
		if (dest.len() > 0)
		{
			if (destProgress < destAnimTime) destProgress += delta;
			
			tmp.set(dest).sub(getPos()).sub(getWidth() / 2, getHeight() - (bump.y + bump.height));
			if (tmp.len() > speed * delta) tmp.limit(speed * delta);
			else tmp.scl(delta);
			
			float deltaX = tmp.x, deltaY = tmp.y;
			
			for (Actor e : Game.instance.getStage().getActors())
			{
				if (e instanceof Entity && !((Entity) e).isDead() && e != this)
				{
					if (intersects((Entity) e, tmp.set(-deltaX, 0))) deltaX = 0;
					if (intersects((Entity) e, tmp.set(0, -deltaY))) deltaY = 0;
					
					if (deltaX == 0 && deltaY == 0) break;
				}
			}
			
			if (isVisible() && getActions().size == 0) moveBy(deltaX, deltaY);
		}
	}
	
	public boolean requestAutoAttack(Creature target)
	{
		if (autoAttackRequested || autoAttackRequestedTarget != null) return false;
		
		autoAttackRequested = true;
		autoAttackRequestedTarget = target;
		return true;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		
		if (destProgress < destAnimTime && dest.len() > 0)
		{
			float size = 64;
			batch.draw(Burst.img.findRegion("target", Math.abs(Math.round((destProgress % destAnimTime) / destAnimTime * 14) - 7)), dest.x - size / 2, dest.y - size / 2, size, size);
		}
	}
	
	@Override
	public void dealDamage(int dmg)
	{
		super.dealDamage(dmg);
		if (dmg > 0 && hp >= 0) Game.instance.showBloodFlash();
	}
	
	public Skill getSelectedSkill()
	{
		return selectedSkill;
	}
	
	public void setSelectedSkill(Skill skill)
	{
		selectedSkill = skill;
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		if (activeSkill == null)
		{
			if (keycode == Keys.Q)
			{
				selectedSkill = new ShadowJump(Player.this, null);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}
	
	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if (button == Buttons.RIGHT || D.android())
		{
			destProgress = 0;
			dest.set(screenX, Gdx.graphics.getHeight() - screenY);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
