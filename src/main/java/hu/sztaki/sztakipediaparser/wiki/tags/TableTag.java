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
 * Represents a table in the AST.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class TableTag extends AbstractTag {
	private String params;
	private TableCaptionTag caption;

	public TableTag() {
		super();
	}

	public TableTag(AbstractTag parent) {
		super(parent);
	}

	public TableTag(Tag parent, String params) {
		super(parent);
		this.params = params;
	}

	@Override
	public void render(StringBuilder b) {
		openTag(b);
		if (caption != null) {
			caption.render(b);
		}

		b.append("<tbody>");
		renderChildren(b);
		b.append("</tbody>");

		closeTag(b);
	}

	@Override
	public void openTag(StringBuilder b) {
		b.append("<table");
		renderCssClasses(b);
		renderAttributes(b);
		if (params != null && !params.isEmpty()) {
			b.append(" " + params);
		}
		b.append(">");
	}

	@Override
	public void closeTag(StringBuilder b) {
		b.append("</table>");
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setCaption(TableCaptionTag caption) {
		this.caption = caption;
	}
	
    public void accept(TagVisitor visitor) {
        visitor.visit(this);
    }
	
}
