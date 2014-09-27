package de.dakror.burst.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.layer.HudLayer;

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
		speed = 3;
		showHpBar = false;
		
		bump.set(51, 35, 48, 80);
	}
	
	@Override
	public void update(float delta)
	{
		float deltaX = 0, deltaY = 0, deltaZ = 0;
		if (Gdx.input.isKeyPressed(Keys.W) && pos.z <= (Gdx.graphics.getHeight() - speed) * Game.zFac) deltaZ += speed * Game.zFac;
		if (Gdx.input.isKeyPressed(Keys.A) && pos.x >= speed) deltaX -= speed;
		if (Gdx.input.isKeyPressed(Keys.S) && pos.z >= speed * Game.zFac) deltaZ -= speed * Game.zFac;
		if (Gdx.input.isKeyPressed(Keys.D) && pos.x + spriteFg.getWidth() <= Gdx.graphics.getWidth() - speed) deltaX += speed;
		
		for (Entity e : Game.instance.entities)
		{
			if (!e.isDead() && e != this)
			{
				if (intersects(e, tmp.set(-deltaX, 0, 0))) deltaX = 0;
				if (intersects(e, tmp.set(0, -deltaY, 0))) deltaY = 0;
				if (intersects(e, tmp.set(0, 0, -deltaZ))) deltaZ = 0;
				
				if (deltaX == 0 && deltaY == 0 && deltaZ == 0) break;
			}
		}
		
		pos.add(deltaX, deltaY, deltaZ);
	}
	
	@Override
	public void dealDamage(int dmg)
	{
		super.dealDamage(dmg);
		if (dmg > 0 && hp >= 0) HudLayer.instance.showBloodFlash();
	}
}
