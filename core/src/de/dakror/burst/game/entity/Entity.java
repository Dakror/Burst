package de.dakror.burst.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import de.dakror.burst.util.interf.Drawable;
import de.dakror.burst.util.interf.Tickable;

/**
 * @author Dakror
 */
public abstract class Entity implements Drawable, Tickable
{
	public static final float zFac = (float) Math.sqrt(2) / 2;
	
	Sprite spriteFg, spriteBg;
	String name;
	
	int hp, maxHp, level;
	float speed;
	final Vector3 pos;
	
	public Entity(float x, float y, float z)
	{
		pos = new Vector3(x, y, z);
		
		level = 0;
		maxHp = hp = 10;
	}
	
	public boolean isDead()
	{
		return hp <= 0;
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
	public void render(Batch batch, float delta)
	{
		if (isDead() || spriteFg == null) return;
		
		if (spriteBg != null)
		{
			spriteBg.setX(pos.x);
			spriteBg.setY(pos.y + zFac * pos.z);
			spriteBg.draw(batch);
		}
		
		spriteFg.setX(pos.x);
		spriteFg.setY(pos.y + zFac * pos.z);
		spriteFg.draw(batch);
	}
}
