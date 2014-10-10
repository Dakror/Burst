package de.dakror.burst.game.skill;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public interface Provider
{
	/**
	 * @param target will be null when not a targeted skill
	 */
	public SequenceAction getSequence(Creature source, Creature target);
	
	public boolean canCastOn(Creature target);
}
