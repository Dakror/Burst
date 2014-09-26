package de.dakror.burst.util.asset;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * This class creates a list of all internal assets files
 * to allow iterating through internal directories.<br>
 * To start the class call {@link #init()} in the startup of your program.
 * 
 * @author Dakror
 */
public abstract class InternalAssetManager
{
	public static InternalAssetManager instance;
	
	protected FileNode root;
	
	/**
	 * Initializes the InternalAssetManager.<br>
	 * Checks wether the program is currently running from a jar file.<br>
	 * If not, a list <i>FILES.txt</i> gets created containing all files and
	 * directories inside the assets folder.<br>
	 * Then all files and directories get loaded in a tree data structure.
	 */
	public abstract void init();
	
	/**
	 * @param path the internal directory
	 * @return a list of all FILES in the specified directory
	 */
	public FileNode[] listFiles(String path)
	{
		return listFiles(path, false);
	}
	
	/**
	 * @param path the internal directory
	 * @param recursive wether all files in all subfolders should be listed too
	 * @return a list of all FILES in the specified directory
	 */
	public FileNode[] listFiles(String path, boolean recursive)
	{
		return list(path, true, false, recursive);
	}
	
	/**
	 * @param path the internal directory
	 * @return a list of all DIRECTORIES in the specified directory
	 */
	public FileNode[] listDirectories(String path)
	{
		return listDirectories(path, false);
	}
	
	/**
	 * @param path the internal directory
	 * @param recursive wether all files in all subfolders should be listed too
	 * @return a list of all DIRECTORIES in the specified directory
	 */
	public FileNode[] listDirectories(String path, boolean recursive)
	{
		return list(path, false, true, recursive);
	}
	
	/**
	 * @param path the internal directory
	 * @return a list of EVERYTHING in the specified directory
	 */
	public FileNode[] list(String path)
	{
		return list(path, false);
	}
	
	/**
	 * @param path the internal directory
	 * @param recursive wether all files in all subfolders should be listed too
	 * @return a list of EVERYTHING in the specified directory
	 */
	public FileNode[] list(String path, boolean recursive)
	{
		return list(path, true, true, recursive);
	}
	
	/**
	 * Calls {@link AssetManager#load(String, Class)} for all files found in the
	 * directory.<br>
	 * If <code>recursive</code> is defined all files in all subfolders will
	 * get scheduled for loading as well.
	 * 
	 * @param assets the AssetManager to load the found assets
	 * @param path the directory to be loaded
	 * @param type the AssetManager Type e.g {@link Texture}
	 * @param recursive wether all files in all subfolders should be loaded too
	 */
	public void scheduleDirectory(AssetManager assets, String path, Class<?> type, boolean recursive)
	{
		scheduleDirectory(assets, path, type, new FileFilter()
		{
			@Override
			public boolean accept(File pathname)
			{
				return true;
			}
		}, recursive);
	}
	
	/**
	 * Calls {@link AssetManager#load(String, Class)} for all files found in the
	 * directory.<br>
	 * If <code>recursive</code> is defined all files in all subfolders will
	 * get scheduled for loading as well.
	 * 
	 * @param assets the AssetManager to load the found assets
	 * @param path the directory to be loaded
	 * @param type the AssetManager Type e.g {@link Texture}
	 * @param fileFilter the {@link FileFilter} to apply. Use e.g a {@link FileNameExtensionFilter}
	 * @param recursive wether all files in all subfolders should be loaded too
	 */
	public abstract void scheduleDirectory(AssetManager assets, String path, Class<?> type, FileFilter fileFilter, boolean recursive);
	
	/**
	 * @return wether the current program is located in a file tree or packed jar
	 *         archive
	 */
	public abstract boolean isRunningFromJarFile();
	
	protected FileNode[] list(String path, boolean f, boolean d, boolean recursive)
	{
		FileHandle dir = Gdx.files.internal(path);
		FileNode fn = traverseTo(dir, root);
		
		Array<FileNode> files = new Array<FileNode>();
		
		Array<FileNode> dirs = new Array<FileNode>();
		
		for (FileNode file : fn.children)
		{
			if (file.directory)
			{
				if (d) files.add(file);
				if (recursive) dirs.add(file);
			}
			if (!file.directory && f) files.add(file);
		}
		
		if (recursive)
		{
			for (FileNode file : dirs)
				files.addAll(list(file.file.path(), f, d, true));
		}
		
		return files.toArray(FileNode.class);
	}
	
	protected FileNode traverseTo(FileHandle searched, FileNode parent)
	{
		if (parent.file != null && searched.equals(parent.file)) return parent;
		for (FileNode fn : parent.children)
		{
			if (fn.directory && searched.path().substring(0, Math.min(fn.file.path().length(), searched.path().length())).equals(fn.file.path()))
			{
				return traverseTo(searched, fn);
			}
		}
		return null;
	}
	
	protected void writeDirectory(StringBuffer sb, File dir, int pathOffset) throws IOException
	{
		File[] files = dir.listFiles();
		Arrays.sort(files, new Comparator<File>()
		{
			@Override
			public int compare(File o1, File o2)
			{
				boolean d1 = o1.isDirectory();
				boolean d2 = o2.isDirectory();
				
				if (d2 && !d1) return -1;
				else if (d1 && !d2) return 1;
				else return o1.getName().compareTo(o2.getName());
			}
		});
		for (File f : files)
		{
			sb.append((f.isDirectory() ? "d " : "f ") + f.getPath().substring(pathOffset).replace("\\", "/") + "\r\n");
			if (f.isDirectory()) writeDirectory(sb, f, pathOffset);
		}
	}
}
