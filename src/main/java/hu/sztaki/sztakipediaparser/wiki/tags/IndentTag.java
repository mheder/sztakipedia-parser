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
 * Represents indentation in the AST.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class IndentTag extends AbstractTag {
	private int level = 1;
	private static int LEVEL1 = 3;
	private static int LEVEL2 = 6;
	private static int LEVEL3 = 9;

	public IndentTag() {
		super();
	}

	public IndentTag(Tag parent) {
		super(parent);
	}

	@Override
	public void render(StringBuilder b) {
		openTag(b);
	}

	@Override
	public void openTag(StringBuilder b) {
		b.append("\n<br/>");
		switch (level) {
		case 1:
			for (int i = 0; i < LEVEL1; i++) {
				b.append("&nbsp;");
			}
			break;
		case 2:
			for (int i = 0; i < LEVEL2; i++) {
				b.append("&nbsp;");
			}
			break;
		case 3:
			for (int i = 0; i < LEVEL3; i++) {
				b.append("&nbsp;");
			}
			break;
		default:
			for (int i = 0; i < LEVEL1; i++) {
				b.append("&nbsp;");
			}
			break;
		}
	}

	@Override
	public void closeTag(StringBuilder b) {
	}

	public int getLevel() {
		return level;
	}

	public void setLeveL(int level) {
		this.level = level;
	}
	
    public void accept(TagVisitor visitor) {
        visitor.visit(this);
    }	

}
