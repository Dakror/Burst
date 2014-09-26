package de.dakror.burst.desktop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

import de.dakror.burst.util.asset.FileNode;
import de.dakror.burst.util.asset.InternalAssetManager;

/**
 * @author Dakror
 */
public class DesktopInternalAssetManager extends InternalAssetManager
{
	@Override
	public void init()
	{
		try
		{
			Reader reader = null;
			
			if (!isRunningFromJarFile())
			{
				File parent = new File(DesktopInternalAssetManager.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParentFile();
				
				File dst = new File(parent, "core/assets");
				if (!dst.exists()) dst = new File(parent, "android/assets");
				if (!dst.exists()) throw new FileNotFoundException("Could not locate assets folder");
				
				StringBuffer sb = new StringBuffer();
				writeDirectory(sb, dst, dst.getPath().length() + 1);
				FileWriter fw = new FileWriter(new File(dst, "FILES.txt"));
				String s = sb.toString();
				fw.write(s);
				fw.close();
				
				reader = new StringReader(s);
			}
			
			int files = 0;
			
			FileHandle fh = Gdx.files.internal("FILES.txt");
			BufferedReader br = new BufferedReader(reader == null ? fh.reader() : reader);
			String line = "";
			
			root = new FileNode(null, null, true);
			
			FileNode activeNode = root;
			int slashes = -1;
			
			while ((line = br.readLine()) != null)
			{
				boolean dir = line.startsWith("d");
				String path = line.substring(2);
				
				int sl = path.split("/").length - 1;
				if (slashes == -1) slashes = sl;
				
				while (slashes > sl)
				{
					activeNode = activeNode.parent;
					slashes--;
				}
				
				if (slashes == sl)
				{
					if (dir)
					{
						FileNode node = new FileNode(activeNode, Gdx.files.internal(path), true);
						activeNode.children.add(node);
						activeNode = node;
						slashes++;
					}
					else
					{
						FileNode node = new FileNode(activeNode, Gdx.files.internal(path), false);
						activeNode.children.add(node);
						files++;
					}
				}
			}
			
			Gdx.app.log("DesktopInternalAssetManager.init", files + " files loaded.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isRunningFromJarFile()
	{
		try
		{
			return new File(DesktopInternalAssetManager.class.getProtectionDomain().getCodeSource().getLocation().toURI()).isFile();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public void scheduleDirectory(AssetManager assets, String path, Class<?> type, FileFilter fileFilter, boolean recursive)
	{
		for (FileNode fn : listFiles(path, recursive))
		{
			if (fileFilter.accept(fn.file.file())) assets.load(fn.file.path(), type);
		}
	}
}
