package de.dakror.burst.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.dakror.burst.Burst;
import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.creature.Creature;
import de.dakror.burst.game.skill.Skill;
import de.dakror.burst.game.skill.SkillType;
import de.dakror.burst.ui.SkillSlot;
import de.dakror.burst.util.D;

/**
 * @author Dakror
 */
public class HudLayer extends Layer
{
	float bloodFlashAlpha = 0;
	
	BitmapFont killDisplay;
	
	Table skillGroup;
	
	final Vector2 tmp = new Vector2();
	
	@Override
	public void show()
	{
		stage = new Stage(new ScreenViewport());
		
		killDisplay = Burst.assets.get("font/tele.fnt", BitmapFont.class);
		skillGroup = new Table();
		
		skillGroup.row().center();
		for (int i = 0; i < Math.min(6, Skill.values().length); i++)
		{
			skillGroup.add(new SkillSlot(Skill.values()[i])).size(64 * (D.android() ? 2 : 1)).spaceLeft(16).padBottom(D.android() ? 32 : 0);
		}
		
		skillGroup.setPosition((Gdx.graphics.getWidth() - skillGroup.getWidth()) / 2, 70);
		stage.addActor(skillGroup);
		
		initDone = true;
	}
	
	@Override
	public void update(float delta)
	{
		stage.act(delta);
	}
	
	@Override
	public void render(float delta)
	{
		stage.draw();
		stage.getBatch().begin();
		
		if (Game.player.getSelectedSkill() != null && Game.player.getCooldown(Game.player.getSelectedSkill().ordinal()) == 0)
		{
			stage.getBatch().setColor(1, 1, 1, 0.75f);
			if (Game.player.getSelectedSkill().getType() == SkillType.Targeted || Game.player.getSelectedSkill().getType() == SkillType.Aura)
			{
				float x = Game.player.getX() + Game.player.getWidth() / 2 - Game.player.getSelectedSkill().getRange();
				float y = Game.player.getY() + Game.player.getHeight() / 2 - Game.player.getSelectedSkill().getRange();
				float size = Game.player.getSelectedSkill().getRange() * 2;
				
				stage.getBatch().draw(Burst.img.findRegion("area"), x, y, size, size);
			}
			else if (Game.player.getSelectedSkill().getType() == SkillType.Skillshot)
			{
				float px = Game.player.getX() + Game.player.getWidth() / 2;
				float py = Game.player.getY() + Game.player.getHeight() / 2;
				float x = px - Game.player.getSelectedSkill().getRange();
				float y = py - Game.player.getSelectedSkill().getRange();
				
				tmp.set(x, y);
				
				float hbRadius = Game.player.getSelectedSkill().getDefaultHitBoxRadius();
				AtlasRegion r = Burst.img.findRegion("arrow");
				
				stage.getBatch().draw(r, px, py, r.getRegionWidth(), hbRadius * 2);
				
				// float angle = (float) Math.toDegrees(Math.atan2(y, x));
				// float range = Game.player.getSelectedSkill().getRange();
				//
				// float hbRadius = Game.player.getSelectedSkill().getDefaultHitBoxRadius();
				//
				//
				//
				// float imgMalus = 76f / r.getRegionHeight();
				//
				// stage.getBatch().draw(r, x, y, (r.getRegionWidth() / r.getRegionHeight()) * (hbRadius * (2 + imgMalus)), hbRadius * (2 + imgMalus));
			}
			stage.getBatch().setColor(1, 1, 1, 1);
		}
		
		int width = 400;
		Creature.renderHpBar(stage.getBatch(), (Gdx.graphics.getWidth() - width) / 2, 5, width, Game.player.getHpPercentage());
		
		TextBounds tb = killDisplay.getBounds(Game.instance.getKills() + "");
		killDisplay.draw(stage.getBatch(), Game.instance.getKills() + "", (Gdx.graphics.getWidth() - tb.width) / 2, Gdx.graphics.getHeight() - tb.height / 2);
		
		if (bloodFlashAlpha > 0)
		{
			stage.getBatch().setColor(1, 1, 1, bloodFlashAlpha);
			stage.getBatch().draw(Burst.assets.get("img/blood.png", Texture.class), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			stage.getBatch().setColor(1, 1, 1, 1);
			bloodFlashAlpha -= delta;
		}
		
		stage.getBatch().end();
	}
	
	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
		skillGroup.setPosition((Gdx.graphics.getWidth() - skillGroup.getWidth()) / 2, 70);
	}
	
	public void showBloodFlash()
	{
		bloodFlashAlpha = 1f;
	}
}
