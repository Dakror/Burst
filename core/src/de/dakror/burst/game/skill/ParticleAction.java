package de.dakror.burst.game.skill;

import com.badlogic.gdx.scenes.scene2d.Action;

import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;

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
		if (!actor.isVisible()) Game.particles.add(name, x + actor.getX(), y + actor.getY(), duration);
		else ((Entity) actor).getParticles().add(name, x + actor.getX(), y + actor.getY(), duration);
		return true;
	}
	
	public static ParticleAction particle(String name, float x, float y)
	{
		return particle(name, x, y, 0);
	}
	
	public static ParticleAction particle(String name, float x, float y, int duration)
	{
		return new ParticleAction(name, x, y);
	}
}
