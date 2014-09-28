package de.dakror.burst.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.game.skill.skills.ShadowJump;

/**
 * @author Dakror
 */
public class Player extends Entity implements InputProcessor
{
	Vector2 tmp = new Vector2();
	
	Entity target = null;
	
	public Player(float x, float y)
	{
		super(x, y);
		maxHp = hp = 20;
		name = "Player";
		spriteFg = Burst.img.createSprite("player_fg");
		spriteBg = Burst.img.createSprite("player_bg");
		speed = 180;
		showHpBar = false;
		
		bump.set(51, 35, 48, 80);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		float deltaX = 0, deltaY = 0;
		if (Gdx.input.isKeyPressed(Keys.W) && getY() <= Gdx.graphics.getHeight() - speed * delta) deltaY += speed * delta;
		if (Gdx.input.isKeyPressed(Keys.A) && getX() >= speed * delta) deltaX -= speed * delta;
		if (Gdx.input.isKeyPressed(Keys.S) && getY() >= speed * delta) deltaY -= speed * delta;
		if (Gdx.input.isKeyPressed(Keys.D) && getX() + spriteFg.getWidth() <= Gdx.graphics.getWidth() - speed * delta) deltaX += speed * delta;
		
		for (Actor e : Game.instance.getStage().getActors())
		{
			if (e instanceof Entity && !((Entity) e).isDead() && e != this)
			{
				if (((Entity) e).hovered) target = (Entity) e;
				
				if (intersects((Entity) e, tmp.set(-deltaX, 0))) deltaX = 0;
				if (intersects((Entity) e, tmp.set(0, -deltaY))) deltaY = 0;
				
				if (deltaX == 0 && deltaY == 0) break;
			}
		}
		
		moveBy(deltaX, deltaY);
	}
	
	@Override
	public void dealDamage(int dmg)
	{
		super.dealDamage(dmg);
		if (dmg > 0 && hp >= 0) Game.instance.showBloodFlash();
	}
	
	@Override
	public boolean keyDown(int keycode)
	{
		if (activeSkill == null)
		{
			if (keycode == Keys.T && target != null)
			{
				setSkill(new ShadowJump(this, target));
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
