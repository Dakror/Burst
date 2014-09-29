package de.dakror.burst.game.skill;

import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public abstract class TargetedSkill extends Skill
{
	protected Entity target;
	
	public TargetedSkill(Entity source, Entity target)
	{
		super(source);
		if (target != null) setTarget(target);
	}
	
	public Entity getTarget()
	{
		return target;
	}
	
	public void setTarget(Entity e)
	{
		target = e;
	}
}
