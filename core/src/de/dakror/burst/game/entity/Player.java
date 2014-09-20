package de.dakror.burst.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import de.dakror.burst.Burst;

/**
 * @author Dakror
 */
public class Player extends Entity
{
	public Player(float x, float y, float z)
	{
		super(x, y, z);
		maxHp = hp = 20;
		name = "Player";
		spriteFg = Burst.img.createSprite("player_fg");
		spriteBg = Burst.img.createSprite("player_bg");
		speed = 3;
		
		bump.set(51, 35, 48, 80);
	}
	
	@Override
	public void tick(int tick)
	{
		if (Gdx.input.isKeyPressed(Keys.W) && pos.z <= (Gdx.graphics.getHeight() - speed) * zFac) pos.z += speed * zFac;
		if (Gdx.input.isKeyPressed(Keys.A) && pos.x >= speed) pos.x -= speed;
		if (Gdx.input.isKeyPressed(Keys.S) && pos.z >= speed * zFac) pos.z -= speed * zFac;
		if (Gdx.input.isKeyPressed(Keys.D) && pos.x + spriteFg.getWidth() <= Gdx.graphics.getWidth() - speed) pos.x += speed;
	}
}
