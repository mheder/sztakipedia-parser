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
 * Represents raw wiki content in the AST. A raw wiki tag added to the AST upon
 * some parser error. The content is snippet of the wikitext that could not be
 * parsed.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class RawWikiTag extends AbstractTag {
	public RawWikiTag() {
		super();
	}

	public RawWikiTag(Tag parent) {
		super(parent);
	}

	@Override
	public void render(StringBuilder b) {
		addBeginEndAttr();
		openTag(b);
		b.append(wikitext);
		closeTag(b);
	}

	@Override
	public void openTag(StringBuilder b) {
		b.append("<span class=\"raw-wikitext\"");
		renderAttributes(b);
		b.append(">");
	}

	@Override
	public void closeTag(StringBuilder b) {
		b.append("</span>");
	}
	
    public void accept(TagVisitor visitor) {
        visitor.visit(this);
    }	
	
}
