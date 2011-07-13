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
 * Represents an unordered list in the AST.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class UnorderedListTag extends AbstractTag {
	/**
	 * Wrap this list in <li>...</li> pairs.
	 */
	private boolean wrap = false;

	public UnorderedListTag() {
		super();
	}

	public UnorderedListTag(AbstractTag parent) {
		super(parent);
	}

	@Override
	public void render(StringBuilder b) {
		// TODO Set attributes and css classes of <li> tag.
		if (wrap) {
			b.append("<li>");
		}

		openTag(b);
		renderChildren(b);
		closeTag(b);

		if (wrap) {
			b.append("</li>");
		}
	}

	@Override
	protected void openTag(StringBuilder b) {

		b.append("<ul");
		renderAttributes(b);
		renderCssClasses(b);
		b.append(">");
	}

	@Override
	protected void closeTag(StringBuilder b) {
		b.append("</ul>");
	}

	public boolean getWrap() {
		return wrap;
	}

	public void setWrap(boolean wrap) {
		this.wrap = wrap;
	}
}