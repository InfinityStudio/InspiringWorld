package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author ci010
 */
@Beta
public class PackageModIdMap
{
	private int size;
	private List<Entry> rootList;

	public PackageModIdMap()
	{
		size = 0;
//		PriorityQueue<Entry> queue = new PriorityQueue<Entry>(Loader.instance().getActiveModList().size(), cmp);
		rootList = Lists.newLinkedList();
	}

	public int size()
	{
		return size;
	}

	public boolean isEmpty()
	{
		return size == 0;
	}

	public String getModid(String pkg)
	{
		if (pkg == null)
			return null;
		if (size == 0)
			return null;
		Entry entry = getRootEntry(pkg);
		while (entry != null)
		{
			int r = pkg.compareTo(entry.pkg);
			if (r < 0)
				if (entry.left != null)
					entry = entry.left;
				else
					return entry.modid;
			else if (r > 0)
				if (entry.right != null)
					entry = entry.right;
				else
					return entry.modid;
			else
				return entry.modid;
		}
		return null;
	}

	private Entry getRootEntry(String pkg)
	{
		Entry entry = null;
		int idx = pkg.indexOf(".");
		String root = idx > 0 ? pkg.substring(0, idx) : pkg;
		for (Entry e : rootList)
		{
			idx = e.pkg.indexOf(".");
			if (idx > 0 && e.pkg.substring(0, idx).equals(root))
				entry = e;
			else if (e.pkg.equals(root))
				entry = e;
		}
		return entry;
	}

	public void put(String pkg, String modid)
	{
		Entry entry = getRootEntry(pkg);
		if (entry == null)
		{
			rootList.add(newEntry(pkg, modid));
			Collections.sort(rootList, cmp);
		}
		else
			while (entry.left != null || entry.right != null)
			{
				int r = pkg.compareTo(entry.pkg);
				if (r < 0)
					if (entry.left != null)
						entry = entry.left;
					else
						entry.left = newEntry(pkg, modid);
				else if (r > 0)
					if (entry.right != null)
						entry = entry.right;
					else
						entry.right = newEntry(pkg, modid);
				else
					throw new IllegalArgumentException("Duplicated pkg! This should not happen!");
			}
	}

	private Entry newEntry(String key, String value)
	{
		Entry entry = new Entry();
		entry.pkg = key;
		entry.modid = value;
		size++;
		return entry;
	}

	private static Comparator<Entry> cmp = new Comparator<Entry>()
	{
		@Override
		public int compare(Entry o1, Entry o2)
		{
			return o1.pkg.compareTo(o2.pkg);
		}
	};

	public void clear()
	{
		this.rootList = Lists.newLinkedList();
	}

	private class Entry
	{
		String pkg;
		String modid;
		Entry left, right;
	}
}
