package de.dakror.burst.game.skill.skills;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static de.dakror.burst.game.skill.actions.Actions.*;
import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.projectile.projectiles.Shuriken;
import de.dakror.burst.game.skill.Skill;

/**
 * @author Dakror
 */
public class ShurikenThrow extends Skill
{
	public ShurikenThrow(Creature source)
	{
		super(source);
		
		sequence = sequence(parallel(entity(new Shuriken(source, 1, 0)), entity(new Shuriken(source, 0, 1)), entity(new Shuriken(source, -1, 0)), entity(new Shuriken(source, 0, -1))));
	}
	
	@Override
	public boolean canBeCastOn(Entity e)
	{
		return true;
	}
}
