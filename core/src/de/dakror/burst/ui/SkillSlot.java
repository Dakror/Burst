package de.dakror.burst.ui;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.dakror.burst.Burst;
import de.dakror.burst.game.skill.Skill;

/**
 * @author Dakror
 */
public class SkillSlot extends Button
{
	Skill skill;
	
	public SkillSlot(Skill skill)
	{
		this.skill = skill;
		
		ButtonStyle style = new ButtonStyle();
		style.up = new TextureRegionDrawable(Burst.img.findRegion("slot"));
		style.down = new TextureRegionDrawable(Burst.img.findRegion("slot"));
		style.over = new TextureRegionDrawable(Burst.img.findRegion("slot"));
		
		setStyle(style);
		
		row();
		
		TextureRegion r = Burst.img.findRegion(skill.getIcon());
		r.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		add(new Image(r)).size(48).expand();
	}
}
