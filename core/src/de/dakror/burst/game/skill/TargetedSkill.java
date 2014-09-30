package de.dakror.burst.game.skill;

import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public abstract class TargetedSkill extends Skill
{
	protected Creature target;
	
	public TargetedSkill(Creature source, Creature target)
	{
		super(source);
		if (target != null) setTarget(target);
	}
	
	public Entity getTarget()
	{
		return target;
	}
	
	public void setTarget(Creature c)
	{
		target = c;
	}
}
