package de.dakror.burst.game.skill.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import de.dakror.burst.game.Game;

/**
 * @author Dakror
 */
public class ParticleAction extends Action
{
	String name;
	int duration;
	float x, y;
	
	public ParticleAction(String name, float x, float y)
	{
		this(name, x, y, 0);
	}
	
	public ParticleAction(String name, float x, float y, int duration)
	{
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean act(float delta)
	{
		Game.particles.add(name, x + actor.getX(), y + actor.getY(), duration);
		return true;
	}
}
