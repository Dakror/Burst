package de.dakror.burst.game.skill;

import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public class TargetedSkill extends Skill
{
	protected Entity target;
	
	public TargetedSkill(Entity source, Entity target)
	{
		super(source);
		this.target = target;
	}
	
	public Entity getTarget()
	{
		return target;
	}
}
