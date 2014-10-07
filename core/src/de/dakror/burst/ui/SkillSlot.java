package de.dakror.burst.ui;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.game.skill.Skill;
import de.dakror.burst.util.D;

/**
 * @author Dakror
 */
public class SkillSlot extends Button
{
	Skill skill;
	BitmapFont font;
	
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
		
		font = new BitmapFont();
		font.setMarkupEnabled(true);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if (isOver() && !D.android())
		{
			String str = skill.getParsedDescription(Game.player);
			int width = 200;
			TextBounds tb = font.getWrappedBounds(str, width);
			font.drawWrapped(batch, str, getX() - (tb.width - getWidth()) / 4, getY() + getHeight() + tb.height + 10, width);
		}
	}
}
