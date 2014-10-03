package de.dakror.burst.game.skill.skills;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.utils.Array;

import de.dakror.burst.game.entity.creature.Creature;

/**
 * @author Dakror
 */
public enum Skills
{
	/* Name has to be the same as the respective class name associated with the enum entry */
	
	ShadowJump("You turn into fading shadows to jump behind your selected target only to reappear and deal [#6bef74]%3*ad%[] damage.", "shadowjump"),
	
	;
	
	String description, icon;
	
	private Skills(String description, String icon)
	{
		this.description = description;
		this.icon = icon;
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
}
