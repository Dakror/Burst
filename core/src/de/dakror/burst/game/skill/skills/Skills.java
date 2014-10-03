package de.dakror.burst.game.skill.skills;

/**
 * @author Dakror
 */
public enum Skills
{
	/* Name has to be the same as the respective class name associated with the enum entry */
	
	ShadowJump("You turn into fading shadows to jump behind your selected target only to reappear and deal %3;ad% damage.", "shadowjump"),
	
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
	
	public String getIcon()
	{
		return icon;
	}
}
