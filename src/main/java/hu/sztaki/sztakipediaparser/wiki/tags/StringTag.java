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

/**
 * Represents any free text in the AST.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class StringTag extends AbstractTag {
	/**************/
	/*** Fields ***/
	/**************/
	private String content = "";

	/********************/
	/*** Constructors ***/
	/********************/

	public StringTag(String content) {
		super();
		this.content = content;
	}

	public StringTag(AbstractTag parent, String content) {
		super(parent);
		this.content = content;
	}

	/***************/
	/*** Methods ***/
	/***************/

	@Override
	public void render(StringBuilder b) {
		addBeginEndAttr();
		openTag(b);
		b.append(content);
		closeTag(b);
		renderChildren(b);
	}

	@Override
	protected void openTag(StringBuilder b) {
		if (!attributes.isEmpty()) {
			b.append("<span");
			renderAttributes(b);
			b.append(">");
		}
	}

	@Override
	protected void closeTag(StringBuilder b) {
		if (!attributes.isEmpty()) {
			b.append("</span>");
		}
	}

	/***************************/
	/*** Getters and Setters ***/
	/***************************/

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
