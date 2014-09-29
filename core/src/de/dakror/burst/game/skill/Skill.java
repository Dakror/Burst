package de.dakror.burst.game.skill;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public abstract class Skill
{
	protected SequenceAction sequence;
	protected Entity source;
	
	public Skill(Entity source)
	{
		this.source = source;
	}
	
	public SequenceAction getSequence()
	{
		return sequence;
	}
	
	public abstract boolean canBeCastOn(Entity e);
}
