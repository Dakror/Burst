package de.dakror.burst.game.skill.skills;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static de.dakror.burst.game.skill.actions.Actions.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

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
		
		Vector2 dir = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()).sub(source.getX() + source.getWidth() / 2, source.getY() + source.getHeight() / 2).nor();
		
		sequence = sequence(entity(new Shuriken(source, dir.x, dir.y)));
	}
	
	@Override
	public boolean canBeCastOn(Entity e)
	{
		return true;
	}
}
