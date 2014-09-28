package de.dakror.burst.game.entity.enemy;

import com.badlogic.gdx.math.Vector2;

import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public class Enemy extends Entity
{
	final Vector2 tmp = new Vector2();
	
	protected long touchStart;
	
	public Enemy(float x, float y)
	{
		super(x, y);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if (Game.player.isVisible() && false)
		{
			tmp.set(Game.player.getPos()).sub(getPos());
			
			if (tmp.len() > speed) tmp.limit(speed);
			
			tmp.scl(delta);
			
			if (Game.player.isInAttackRange(this, tmp) && !Game.player.isDead())
			{
				if (touchStart == 0) touchStart = System.currentTimeMillis();
				onPlayerTouch(delta);
			}
			else
			{
				moveBy(tmp.x, tmp.y);
				touchStart = 0;
			}
		}
	}
	
	public void onPlayerTouch(float delta)
	{
		if (Math.round((System.currentTimeMillis() - touchStart) / 1000f) >= attackTime)
		{
			Game.player.dealDamage(attackDamage);
			touchStart = System.currentTimeMillis();
		}
	}
}
