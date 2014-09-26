package de.dakror.burst.util.asset;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class FileNode
{
	public FileNode parent;
	public FileHandle file;
	public Array<FileNode> children;
	public boolean directory;
	
	public FileNode(FileNode parent, FileHandle file, boolean directory)
	{
		this.parent = parent;
		this.file = file;
		this.directory = directory;
		
		children = new Array<FileNode>();
	}
	
	@Override
	public String toString()
	{
		return (parent != null && parent.file != null ? parent.file.name() : "") + ", " + (file != null ? file.name() : "") + ", " + children;
	}
}
