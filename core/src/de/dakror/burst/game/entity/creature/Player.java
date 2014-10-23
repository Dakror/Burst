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
import de.dakror.burst.game.skill.SkillType;
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
	
	Skill selectedSkill;
	
	boolean autoAttackRequestedDirectionSet;
	boolean autoAttackRequested;
	
	Creature autoAttackRequestedTarget;
	
	public Creature selectedTarget;
	
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
			else if (!autoAttackRequestedDirectionSet)
			{
				dest.set(autoAttackRequestedTarget.getPos().add(autoAttackRequestedTarget.getWidth() / 2, autoAttackRequestedTarget.getHeight() / 2));
				autoAttackRequestedDirectionSet = true;
			}
		}
		
		if (attackDone && autoAttackRequestedTarget != null && !autoAttackRequested) autoAttackRequestedTarget = null;
		
		boolean w = Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP);
		boolean a = Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT);
		boolean s = Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN);
		boolean d = Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT);
		
		if (dest.len() > 0 || w || a || s || d)
		{
			if (destProgress < destAnimTime) destProgress += delta;
			
			tmp.set(dest).sub(getPos()).sub(getWidth() / 2, getHeight() - (bump.y + bump.height));
			
			if (w || a || s || d)
			{
				dest.setZero();
				tmp.setZero();
				
				if (w) tmp.y += speed;
				if (a) tmp.x -= speed;
				if (s) tmp.y -= speed;
				if (d) tmp.x += speed;
			}
			
			
			
			if (tmp.len() > speed * delta) tmp.limit(speed * delta);
			else tmp.scl(delta);
			
			float deltaX = tmp.x, deltaY = tmp.y;
			
			for (Actor e : Game.instance.getStage().getActors())
			{
				if (e instanceof Creature && !((Entity) e).isDead() && e != this)
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
		
		autoAttackRequestedDirectionSet = false;
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
	public void dealDamage(int dmg, float angleDegrees, Entity source)
	{
		super.dealDamage(dmg, angleDegrees, source);
		if (dmg > 0 && hp >= 0) Game.hud.showBloodFlash();
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
			if (keycode == Keys.NUM_1)
			{
				selectedSkill = Skill.Shadow_Jump;
			}
			if (keycode == Keys.NUM_2)
			{
				selectedSkill = Skill.Shuriken_Throw;
			}
		}
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		if (selectedSkill != null)
		{
			activateSelectedSkill(selectedTarget);
			return true;
		}
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
			
			autoAttackRequestedDirectionSet = true;
			autoAttackRequested = false;
			autoAttackRequestedTarget = null;
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
		return true;
	}
	
	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
	
	public void activateSelectedSkill(Creature target)
	{
		if (selectedSkill != null)
		{
			if (selectedSkill.canCastOn(target) && isInCastRange(target))
			{
				if (selectedSkill.isStopMotion()) dest.setZero();
				
				setSkill(selectedSkill, target);
			}
			selectedSkill = null;
		}
	}
	
	public boolean isInCastRange(Creature target)
	{
		if (selectedSkill.getType() != SkillType.Targeted) return true;
		
		if (target == null) return false;
		
		float range = tmp.set(target.getX() + target.getWidth() / 2, target.getY() + target.getHeight() / 2).sub(getX() + getWidth() / 2, getY() + getHeight() / 2).len();
		return range <= selectedSkill.getRange();
	}
}
