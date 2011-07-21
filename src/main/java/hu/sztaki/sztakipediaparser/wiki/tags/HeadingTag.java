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

/**
 * Represents a section heading in the AST.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class HeadingTag extends AbstractTag {
	/**
	 * Level of heading. 1 = Section heading 2 = Subsection 3 = Sub-subsection
	 */
	private int level = 1;

	public HeadingTag() {
		super();
	}

	public HeadingTag(Tag parent) {
		super(parent);
	}

	@Override
	public void render(StringBuilder b) {
		switch (level) {
		case 1:
			addClass("section-heading");
			break;
		case 2:
			addClass("subsection-heading");
			break;
		case 3:
			addClass("sub-subsection-heading");
			break;
		default:
			addClass("sub-subsection-heading");
		}

		openTag(b);

		renderChildren(b);

		closeTag(b);
	}

	@Override
	public void openTag(StringBuilder b) {
		b.append("<div");

		renderCssClasses(b);
		renderAttributes(b);

		b.append(">");
	}

	@Override
	public void closeTag(StringBuilder b) {
		b.append("</div>");
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
    public void accept(TagVisitor visitor) {
        visitor.visit(this);
    }	

}
