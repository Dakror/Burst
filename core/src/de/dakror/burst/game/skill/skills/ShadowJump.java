package de.dakror.burst.game.skill.skills;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static de.dakror.burst.game.skill.actions.Actions.*;

import com.badlogic.gdx.math.Vector2;

import de.dakror.burst.game.entity.Entity;
import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.creature.enemy.Enemy;
import de.dakror.burst.game.skill.TargetedSkill;

/**
 * @author Dakror
 */
public class ShadowJump extends TargetedSkill
{
	public ShadowJump(Creature source, Creature target)
	{
		super(source, target);
	}
	
	@Override
	public void setTarget(Creature c)
	{
		super.setTarget(c);
		
		float deltaX = Math.min(target.getBump().width + source.getAttackRange(), Math.abs(source.getX() - target.getX())) * (source.getX() > target.getX() ? 1 : -1);
		
		Vector2 sub = source.getPos().sub(target.getPos());
		sub.y *= deltaX / sub.x;
		sub.x = deltaX;
		
		Vector2 backSide = target.getPos().sub(sub);
		
		sequence = sequence(visible(false), particle("shadow.p", 75, 75), moveTo(backSide.x, backSide.y), delay(0.3f), particle("shadow.p", 75, 75), delay(0.2f), visible(true), attack(3.0f, c));
	}
	
	@Override
	public boolean canBeCastOn(Entity e)
	{
		return e instanceof Enemy;
	}
}
