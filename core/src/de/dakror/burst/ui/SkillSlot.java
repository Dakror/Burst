package de.dakror.burst.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
	
	final Color c = new Color(1, 1, 1, 1);
	float delta;
	
	static NinePatch bg_fg, bg_bg;
	
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
		
		if (bg_fg == null)
		{
			int s = 40;
			bg_fg = new NinePatch(Burst.img.findRegion("glowBg_fg"), s, s, s, s);
			bg_bg = new NinePatch(Burst.img.findRegion("glowBg_bg"), s, s, s, s);
		}
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		if (isOver()) this.delta += delta;
		else this.delta = 0;
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
			
			float x = getX() - (tb.width - getWidth()) / 4;
			float y = getY() + getHeight() + tb.height + 10;
			
			bg_bg.setColor(c.set(1, 1, 1, (float) Math.sin(delta * Math.PI) * 0.4f + 0.6f));
			bg_bg.draw(batch, x - 40, y - tb.height - 40, width + 80, tb.height + 80);
			bg_fg.draw(batch, x - 40, y - tb.height - 40, width + 80, tb.height + 80);
			font.drawWrapped(batch, str, x, y, width);
		}
	}
}
