package de.dakror.burst.client;

import java.io.BufferedReader;
import java.io.FileFilter;
import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

import de.dakror.burst.util.asset.FileNode;
import de.dakror.burst.util.asset.InternalAssetManager;

/**
 * @author Dakror
 */
public class HtmlInternalAssetManager extends InternalAssetManager
{
	@Override
	public void init()
	{
		try
		{
			Reader reader = null;
			
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
			
			Gdx.app.log("HtmlInternalAssetManager.init", files + " files loaded.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isRunningFromJarFile()
	{
		return true;
	}
	
	@Override
	public void scheduleDirectory(AssetManager assets, String path, Class<?> type, FileFilter fileFilter, boolean recursive)
	{}
}
