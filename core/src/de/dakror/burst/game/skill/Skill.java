package de.dakror.burst.game.skill;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static de.dakror.burst.game.skill.actions.Actions.*;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.creature.enemy.Enemy;
import de.dakror.burst.game.entity.projectile.projectiles.Shuriken;

/**
 * @author Dakror
 */
public enum Skill implements Provider
{
	Shadow_Jump("You turn into fading shadows to jump behind your selected target only to reappear and deal [#6bef74]%3*ad%[] damage.", "shadowjump", SkillType.Targeted, true, true, 11.0f, 325.0f)
	{
		@Override
		public SequenceAction getSequence(Creature source, Creature target)
		{
			float deltaX = Math.min(target.getBump().width + source.getAttackRange(), Math.abs(source.getX() - target.getX())) * (source.getX() > target.getX() ? 1 : -1);
			
			Vector2 sub = source.getPos().sub(target.getPos());
			sub.y *= deltaX / sub.x;
			sub.x = deltaX;
			
			Vector2 backSide = target.getPos().sub(sub);
			
			return sequence(visible(false), particle("shadow.p", 75, 75), moveTo(backSide.x, backSide.y), delay(0.3f), particle("shadow.p", 75, 75), delay(0.2f), visible(true), attack(3.0f, target));
		}
		
		@Override
		public boolean canCastOn(Creature target)
		{
			return target instanceof Enemy;
		}
	},
	Shuriken_Throw("You throw a sharp and rotating shuriken towards a target location. The projectile passes through every Enemy on the way to deal [#6bef74]8[] damage.", "shuriken", SkillType.Skillshot, false, false, 3.2f, 300.0f)
	{
		@Override
		public SequenceAction getSequence(Creature source, Creature target)
		{
			Vector2 dir = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()).sub(source.getX() + source.getWidth() / 2, source.getY() + source.getHeight() / 2).nor();
			
			return sequence(entity(new Shuriken(source, dir.x, dir.y)));
		}
		
		@Override
		public boolean canCastOn(Creature target)
		{
			return true;
		}
	},
	
	;
	
	String description, icon;
	
	boolean stopMotion, targeted;
	
	float cooldown, range;
	
	SkillType type;
	
	private Skill(String description, String icon, SkillType type, boolean stopMotion, boolean targeted, float cooldown, float range)
	{
		this.description = description;
		this.icon = icon;
		this.type = type;
		this.stopMotion = stopMotion;
		this.targeted = targeted;
		this.cooldown = cooldown;
		this.range = range;
	}
	
	public float getCooldown()
	{
		return cooldown;
	}
	
	public float getRange()
	{
		return range;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getParsedDescription(Creature source)
	{
		Array<String> ps = new Array<String>(new String[] { "ad", "as", "ra", "sp", "hp", "hpm" });
		Array<Float> ds = new Array<Float>(new Float[] { (float) source.getAttackDamage(), source.getAttackTime(), source.getAttackRange(), source.getSpeed(), (float) source.getHp(), (float) source.getMaxHp() });
		
		Pattern p = Pattern.compile("(%)(\\S{1,})(\\+|\\*)(ad|as|ra|sp|hp|hpm)(%)");
		Matcher m = p.matcher(description);
		StringBuffer sb = new StringBuffer();
		
		while (m.find())
		{
			float number = (float) Double.parseDouble(m.group(2));
			
			String g = m.group(4);
			float value = ds.get(ps.indexOf(g, false));
			
			if (m.group(3).equals("*")) number *= value;
			else number += value;
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			m.appendReplacement(sb, df.format(number) + "");
		}
		
		m.appendTail(sb);
		
		return sb.toString();
	}
	
	public String getIcon()
	{
		return icon;
	}
	
	public SkillType getType()
	{
		return type;
	}
	
	public boolean isStopMotion()
	{
		return stopMotion;
	}
	
	public boolean isTargeted()
	{
		return targeted;
	}
}
