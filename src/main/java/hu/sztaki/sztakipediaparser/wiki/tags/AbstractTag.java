/*
 * Sztakipedia parser - http://http://code.google.com/p/sztakipedia-parser
 *
 * Copyright (C) 2011 MTA SZTAKI 
 * Copyright (C) 2011 Tibor Olah
 *
 * Sztakipedia parser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * Sztakipedia parser is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sztakipedia parser; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package hu.sztaki.sztakipediaparser.wiki.tags;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An abstract class, it is the parent class of all classes that represent a
 * node in the abstract syntax tree.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public abstract class AbstractTag {
	/**************/
	/*** Fields ***/
	/**************/

	/**
	 * Children nodes.
	 */
	protected ArrayList<AbstractTag> children = new ArrayList<AbstractTag>();

	/**
	 * Parent node.
	 */
	protected AbstractTag parent;

	/**
	 * HTML attributes like width="250px".
	 */
	protected HashMap<String, String> attributes = new HashMap<String, String>();

	/**
	 * List of css classes of the tag.
	 */
	protected ArrayList<String> classes = new ArrayList<String>();

	/**
	 * Store raw wikitext just in case.
	 */
	protected String wikitext = "";

	/**
	 * Position of the beginning of the tag in the original wikitext.
	 */
	protected Integer begin;

	/**
	 * Position of the end of the tag in the original wikitext.
	 */
	protected Integer end;

	/********************/
	/*** Constructors ***/
	/********************/

	/**
	 * Constructor for a root node. Its parent is null.
	 */
	public AbstractTag() {
	}

	/**
	 * Constructor for a non-root node.
	 * 
	 * @param parent
	 */
	public AbstractTag(AbstractTag parent) {
		this.parent = parent;
	}

	/***************/
	/*** Methods ***/
	/***************/

	/**
	 * Adds a single child to this node's children.
	 * 
	 * @param c
	 *            New child.
	 */
	public void addChild(AbstractTag c) {
		children.add(c);
	}

	/**
	 * Adds the child tag to at the specified index.
	 * 
	 * @param c
	 *            Child tag.
	 * @param index
	 */
	public void addChild(AbstractTag c, int index) {
		children.add(index, c);
	}

	/**
	 * Removes a child.
	 * 
	 * @param c
	 *            Child to remove.
	 */
	public void removeChild(AbstractTag c) {
		children.remove(c);
	}

	/**
	 * Returns the index of the specified child or -1 if the child is not found.
	 * 
	 * @param c
	 *            Child tag.
	 * @return Index or -1 if c is not found.
	 */
	public int getChildIndex(AbstractTag c) {
		return children.indexOf(c);
	}

	/**
	 * Adds a new attribute to the node. Like width="250".
	 * 
	 * @param key
	 *            Name of attribute.
	 * @param value
	 *            Value of attribute.
	 */
	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}

	/**
	 * Adds a new css class to the tag.
	 * 
	 * @param c
	 *            Single class definition.
	 */
	public void addClass(String c) {
		if (!classes.contains(c)) {
			classes.add(c);
		}
	}

	/**
	 * Adds several css classes to the tag.
	 * 
	 * @param c
	 *            ArrayList of class definitions.
	 */
	public void addClasses(ArrayList<String> c) {
		for (String ci : c) {
			if (!classes.contains(ci)) {
				classes.add(ci);
			}
		}
	}

	/**
	 * Render the appropriate html code string.
	 * 
	 * @return
	 */
	public abstract void render(StringBuilder b);

	/**
	 * Render the opening part of the HTML tag, like <div>
	 * 
	 * @param b
	 */
	protected abstract void openTag(StringBuilder b);

	/**
	 * Render the closing part of the HTML tag, like </div>
	 * 
	 * @param b
	 */
	protected abstract void closeTag(StringBuilder b);

	/**
	 * Render the HTML of the tag's attributes.
	 * 
	 * @param b
	 */
	protected void renderAttributes(StringBuilder b) {
		if (!attributes.isEmpty()) {
			for (String name : attributes.keySet()) {
				b.append(" " + name + "=\"" + attributes.get(name) + "\"");
			}
		}
	}

	/**
	 * Render the HTML of the tag's CSS classes.
	 * 
	 * @param b
	 */
	protected void renderCssClasses(StringBuilder b) {
		if (!classes.isEmpty()) {
			b.append(" class=\"");
			for (String name : classes) {
				if (classes.indexOf(name) == classes.size() - 1) {
					b.append(name);
				} else {
					b.append(name + " ");
				}
			}
			b.append("\"");
		}
	}

	/**
	 * Render all children nodes of this node.
	 * 
	 * @param b
	 */
	protected void renderChildren(StringBuilder b) {
		for (AbstractTag c : children) {
			c.render(b);
		}
	}

	/**
	 * Adds an attribute that specifies the begin and end character positions of
	 * the construct in the original wikitext.
	 */
	protected void addBeginEndAttr() {
		if (begin != null) {
			attributes.put("begin", String.valueOf(begin));
		}

		if (end != null) {
			attributes.put("end", String.valueOf(end));
		}
	}

	/***************************/
	/*** Getters and Setters ***/
	/***************************/

	public ArrayList<AbstractTag> getChildren() {
		return children;
	}

	public AbstractTag getParent() {
		return parent;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public ArrayList<String> getClasses() {
		return classes;
	}

	public String getWikitext() {
		return wikitext;
	}

	public void setWikitext(String wikitext) {
		this.wikitext = wikitext;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getBegin() {
		return begin;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getEnd() {
		return end;
	}
}
