package de.dakror.burst.game.skill.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import de.dakror.burst.game.Game;
import de.dakror.burst.game.entity.Entity;

/**
 * @author Dakror
 */
public class EntityAction extends Action {
	Entity entity;
	
	public EntityAction(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public boolean act(float delta) {
		Game.instance.spawnEntity(entity);
		return true;
	}
}
