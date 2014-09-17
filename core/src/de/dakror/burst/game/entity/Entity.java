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
	Sprite sprite;
	String name;
	
	int hp, maxHp;
	final Vector3 pos;
	
	public Entity(float x, float y, float z)
	{
		pos = new Vector3(x, y, z);
		
		maxHp = hp = 10;
	}
	
	public boolean isDead()
	{
		return hp <= 0;
	}
	
	public Sprite getSprite()
	{
		return sprite;
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
		if (isDead() || sprite == null) return;
		
		sprite.setX(pos.x);
		sprite.setY(pos.y + 0.5f * pos.z);
		sprite.draw(batch);
	}
}
