package de.dakror.burst.game.skill.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public class AttackAction extends Action
{
	float ampl;
	int damage;
	Entity target;
	boolean queued;
	
	public AttackAction(Entity target)
	{
		this(0, target);
	}
	
	public AttackAction(int damage, Entity target)
	{
		this.damage = damage;
		this.target = target;
		
		queued = false;
	}
	
	public AttackAction(float ampl, Entity target)
	{
		this.ampl = ampl;
		this.target = target;
		
		queued = false;
	}
	
	@Override
	public boolean act(float delta)
	{
		if (!queued)
		{
			if (damage > 0) ((Entity) actor).attack(target, damage);
			else if (ampl > 1) ((Entity) actor).attack(target, ampl);
			else ((Entity) actor).attack(target);
			queued = true;
		}
		return ((Entity) actor).attackDone;
	}
	
	public static AttackAction attack(Entity target)
	{
		return new AttackAction(target);
	}
	
	public static AttackAction attack(int damage, Entity target)
	{
		return new AttackAction(damage, target);
	}
	
	public static AttackAction attack(float ampl, Entity target)
	{
		return new AttackAction(ampl, target);
	}
}
