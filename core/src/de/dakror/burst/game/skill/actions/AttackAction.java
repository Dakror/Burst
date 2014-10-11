package de.dakror.burst.game.skill.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public class AttackAction extends Action
{
	float ampl;
	float time;
	int damage;
	Creature target;
	boolean queued;
	
	public AttackAction(Creature target)
	{
		this(0, target);
	}
	
	public AttackAction(int damage, Creature target)
	{
		this.damage = damage;
		this.target = target;
		
		queued = false;
	}
	
	public AttackAction(float ampl, Creature target)
	{
		this.ampl = ampl;
		this.target = target;
		
		queued = false;
	}
	
	public AttackAction setTime(float time)
	{
		this.time = time;
		return this;
	}
	
	@Override
	public boolean act(float delta)
	{
		if (!queued)
		{
			if (damage > 0)
			{
				if (time > 0) ((Creature) actor).attack(target, damage, time);
				else ((Creature) actor).attack(target, damage);
			}
			else if (ampl > 1)
			{
				if (time > 0) ((Creature) actor).attack(target, ampl, time);
				else ((Creature) actor).attack(target, ampl);
			}
			else ((Creature) actor).attack(target);
			queued = true;
		}
		return ((Creature) actor).attackDone;
	}
}
