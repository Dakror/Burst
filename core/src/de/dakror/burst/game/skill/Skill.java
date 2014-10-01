package de.dakror.burst.game.skill;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public abstract class Skill
{
	protected SequenceAction sequence;
	protected Creature source;
	
	public Skill(Creature source)
	{
		this.source = source;
	}
	
	public SequenceAction getSequence()
	{
		return sequence;
	}
	
	/**
	 * @param e could be null
	 */
	public abstract boolean canBeCastOn(Entity e);
}
