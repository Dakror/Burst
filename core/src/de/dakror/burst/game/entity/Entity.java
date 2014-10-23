package de.dakror.burst.game.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author Dakror
 */
public abstract class Entity extends Actor
{
	protected Sprite spriteFg, spriteBg;
	
	protected float pulseTime = 0.5f; // * 1 second
	protected float delta;
	protected int glowSize = 20;
	
	protected Rectangle bump;
	
	protected final Rectangle bmp = new Rectangle();
	protected final Vector2 tmp = new Vector2();
	
	protected boolean dead;
	
	public Entity(float x, float y)
	{
		setPosition(x, y);
		bump = new Rectangle();
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		this.delta += delta;
		
		if (spriteFg != null) setSize(spriteFg.getWidth(), spriteFg.getHeight());
		
		if (dead)
		{
			onRemoval();
			remove();
		}
	}
	
	public Sprite getSpriteFg()
	{
		return spriteFg;
	}
	
	public Sprite getSpriteBg()
	{
		return spriteBg;
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
	
	public final boolean isDead()
	{
		return dead;
	}
	
	public void setDead(boolean dead)
	{
		this.dead = dead;
	}
	
	public void onRemoval()
	{}
}
