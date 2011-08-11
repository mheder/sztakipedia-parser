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

import hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor;
import hu.sztaki.sztakipediaparser.wiki.visitor.Visitable;

import java.util.List;
import java.util.Map;

/**
 * An abstract class, it is the parent class of all classes that represent a
 * node in the abstract syntax tree.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public interface Tag  extends TreeNode<Tag>, Visitable<TagVisitor, Tag> {

	/***************/
	/*** Methods ***/
	/***************/

	/**
	 * Adds a new attribute to the node. Like width="250".
	 * 
	 * @param key
	 *            Name of attribute.
	 * @param value
	 *            Value of attribute.
	 */
	public void addAttribute(String key, String value);

	/**
	 * Adds a new CSS class to the tag.
	 * 
	 * @param c
	 *            Single class definition.
	 */
	public void addClass(String c);

	/**
	 * Adds several CSS classes to the tag.
	 * 
	 * @param c
	 *            ArrayList of class definitions.
	 */
	public void addClasses(List<String> c);

	/**
	 * Render the HTML of the tag's attributes.
	 * 
	 * @param b
	 * @deprecated Move rendering code to separate class according to
	 *             <em>Visitor</em> pattern
	 */
	@Deprecated
	void renderAttributes(StringBuilder b);

	/**
	 * Render the HTML of the tag's CSS classes.
	 * 
	 * @param b
	 */
	@Deprecated
	void renderCssClasses(StringBuilder b);

	/**
	 * Adds an attribute that specifies the begin and end character positions of
	 * the construct in the original wikitext.
	 */
	void addBeginEndAttr();

	public Map<String, String> getAttributes();

	public List<String> getClasses();

	public String getWikitext();

	public void setWikitext(String wikitext);

	public void setBegin(int begin);

	public int getBegin();

	public void setEnd(int end);

	public int getEnd();

}
