package de.dakror.burst.game.skill.actions;

import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class Actions
{
	public static EntityAction entity(Entity entity)
	{
		return new EntityAction(entity);
	}
	
	public static AttackAction attack(Creature target)
	{
		return new AttackAction(target);
	}
	
	public static AttackAction attack(Creature target, float time)
	{
		return new AttackAction(target).setTime(time);
	}
	
	public static AttackAction attack(int damage, Creature target)
	{
		return new AttackAction(damage, target);
	}
	
	public static AttackAction attack(int damage, Creature target, float time)
	{
		return new AttackAction(damage, target).setTime(time);
	}
	
	public static AttackAction attack(float ampl, Creature target)
	{
		return new AttackAction(ampl, target);
	}
	
	public static AttackAction attack(float ampl, Creature target, float time)
	{
		return new AttackAction(ampl, target).setTime(time);
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
