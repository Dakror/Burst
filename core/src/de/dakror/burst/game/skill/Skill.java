/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.burst.game.skill;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static de.dakror.burst.game.skill.actions.Actions.*;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.entity.creature.Player;
import de.dakror.burst.game.entity.creature.enemy.Enemy;
import de.dakror.burst.game.entity.projectile.projectiles.Shuriken;

/**
 * @author Dakror
 */
public enum Skill {
	Shadow_Jump("You turn into fading shadows to jump behind your selected [enemy]target[] only to reappear and deal [scale]%3*ad%[] [GRAY](3 * [][ad]%1*ad%[][GRAY])[] damage.", "shadowjump", SkillType.Targeted, true, 11.0f, 325.0f) {
		@Override
		public SequenceAction getSequence(Creature source, Creature target) {
			float deltaX = Math.min(target.getBump().width + source.getAttackRange(), Math.abs(source.getX() - target.getX())) * (source.getX() > target.getX() ? 1 : -1);
			
			Vector2 sub = source.getPos().sub(target.getPos());
			sub.y *= deltaX / sub.x;
			sub.x = deltaX;
			
			Vector2 backSide = target.getPos().sub(sub);
			
			return sequence(visible(false), particle("shadow.p", 75, 75), moveTo(backSide.x, backSide.y), delay(0.3f), particle("shadow.p", 75, 75), delay(0.2f), visible(true), attack(3.0f, target, 0.25f));
		}
		
		@Override
		public boolean canCastOn(Creature target) {
			return target instanceof Enemy && !target.isDead();
		}
	},
	Shuriken_Throw("You throw a sharp and rotating shuriken towards a target location.\nThe projectile passes through every [enemy]enemy[] on the way to deal [flat]8[] damage.", "shuriken", SkillType.Skillshot, false, 3.2f, 300.0f) {
		@Override
		public SequenceAction getSequence(Creature source, Creature target) {
			Vector2 dir = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()).sub(source.getX() + source.getWidth() / 2, source.getY() + source.getHeight() / 2).nor();
			
			return sequence(entity(new Shuriken(source, dir.x, dir.y)));
		}
		
		@Override
		public boolean canCastOn(Creature target) {
			return !(target instanceof Player);
		}
		
		@Override
		public float getDefaultHitBoxRadius() {
			return 16;
		}
	},
	
	;
	
	static {
		Colors.put("add", Color.valueOf("f88b2a"));
		Colors.put("scale", Color.valueOf("afdc15"));
		Colors.put("flat", Color.valueOf("24de84"));
		
		Colors.put("ad", Color.valueOf("d06212"));
		
		Colors.put("enemy", Color.valueOf("c14949"));
	}
	
	String description, icon;
	
	boolean stopMotion;
	
	float cooldown, range;
	
	SkillType type;
	
	private Skill(String description, String icon, SkillType type, boolean stopMotion, float cooldown, float range) {
		this.description = description;
		this.icon = icon;
		this.type = type;
		this.stopMotion = stopMotion;
		this.cooldown = cooldown;
		this.range = range;
	}
	
	public final float getCooldown() {
		return cooldown;
	}
	
	public final float getRange() {
		return range;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public final String getParsedDescription(Creature source) {
		Array<String> ps = new Array<String>(new String[] { "ad", "as", "ra", "sp", "hp", "hpm" });
		Array<Float> ds = new Array<Float>(new Float[] { (float) source.getAttackDamage(), source.getAttackTime(), source.getAttackRange(), source.getSpeed(), (float) source.getHp(), (float) source.getMaxHp() });
		
		Pattern p = Pattern.compile("%([0-9\\.]+)(\\*|\\+)(\\S+)%");
		Matcher m = p.matcher(description);
		StringBuffer sb = new StringBuffer();
		
		while (m.find()) {
			float number = (float) Double.parseDouble(m.group(1));
			
			float value = ds.get(ps.indexOf(m.group(3), false));
			
			if (m.group(2).equals("*")) number *= value;
			else number += value;
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			m.appendReplacement(sb, df.format(number) + "");
		}
		
		m.appendTail(sb);
		
		return sb.toString();
	}
	
	public final String getIcon() {
		return icon;
	}
	
	public final SkillType getType() {
		return type;
	}
	
	public final boolean isStopMotion() {
		return stopMotion;
	}
	
	// -- abstracts & overridables -- //
	
	/**
	 * @param target will be null when not a targeted skill
	 */
	public abstract SequenceAction getSequence(Creature source, Creature target);
	
	public abstract boolean canCastOn(Creature target);
	
	public float getDefaultHitBoxRadius() {
		return 0;
	}
}
