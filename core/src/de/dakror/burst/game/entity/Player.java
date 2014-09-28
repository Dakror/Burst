package de.dakror.burst.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;

/**
 * @author Dakror
 */
public class Player extends Entity
{
	Vector3 tmp = new Vector3();
	
	public Player(float x, float y, float z)
	{
		super(x, y, z);
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
		
		float deltaX = 0, deltaY = 0, deltaZ = 0;
		if (Gdx.input.isKeyPressed(Keys.W) && getZ() <= (Gdx.graphics.getHeight() - speed * delta) * Game.zFac) deltaZ += speed * delta * Game.zFac;
		if (Gdx.input.isKeyPressed(Keys.A) && getX() >= speed * delta) deltaX -= speed * delta;
		if (Gdx.input.isKeyPressed(Keys.S) && getZ() >= speed * delta * Game.zFac) deltaZ -= speed * delta * Game.zFac;
		if (Gdx.input.isKeyPressed(Keys.D) && getX() + spriteFg.getWidth() <= Gdx.graphics.getWidth() - speed * delta) deltaX += speed * delta;
		
		for (Actor e : Game.instance.getStage().getActors())
		{
			if (e instanceof Entity && !((Entity) e).isDead() && e != this)
			{
				if (intersects((Entity) e, tmp.set(-deltaX, 0, 0))) deltaX = 0;
				if (intersects((Entity) e, tmp.set(0, -deltaY, 0))) deltaY = 0;
				if (intersects((Entity) e, tmp.set(0, 0, -deltaZ))) deltaZ = 0;
				
				if (deltaX == 0 && deltaY == 0 && deltaZ == 0) break;
			}
		}
		
		moveBy(deltaX, deltaY, deltaZ);
	}
	
	@Override
	public void dealDamage(int dmg)
	{
		super.dealDamage(dmg);
		if (dmg > 0 && hp >= 0) Game.instance.showBloodFlash();
	}
}
