/*
 * Sztakipedia parser - http://http://code.google.com/p/sztakipedia-parser
 *
 * Copyright (C) 2011 MTA SZTAKI 
 * Copyright (C) 2011 Illes Solt 
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
package hu.sztaki.sztakipediaparser.wiki.visitor;

import hu.sztaki.sztakipediaparser.wiki.tags.AnchorTag;
import hu.sztaki.sztakipediaparser.wiki.tags.BodyTag;
import hu.sztaki.sztakipediaparser.wiki.tags.BoldTag;
import hu.sztaki.sztakipediaparser.wiki.tags.DivTag;
import hu.sztaki.sztakipediaparser.wiki.tags.HRTag;
import hu.sztaki.sztakipediaparser.wiki.tags.HeadingTag;
import hu.sztaki.sztakipediaparser.wiki.tags.ImageTag;
import hu.sztaki.sztakipediaparser.wiki.tags.IndentTag;
import hu.sztaki.sztakipediaparser.wiki.tags.ItalicTag;
import hu.sztaki.sztakipediaparser.wiki.tags.ParagraphTag;
import hu.sztaki.sztakipediaparser.wiki.tags.RawWikiTag;
import hu.sztaki.sztakipediaparser.wiki.tags.ReferenceTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TableCaptionTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TableCellTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TableColHeadingTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TableRowHeadingTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TableRowTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TableTag;
import hu.sztaki.sztakipediaparser.wiki.tags.Tag;
import hu.sztaki.sztakipediaparser.wiki.tags.TemplateParam;
import hu.sztaki.sztakipediaparser.wiki.tags.TemplateTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TextTag;
import hu.sztaki.sztakipediaparser.wiki.tags.UnorderedListItemTag;
import hu.sztaki.sztakipediaparser.wiki.tags.UnorderedListTag;

/**
 * 
 * @author Illes Solt
 *
 */
public abstract class AbstractTagVisitor implements TagVisitor {
	
	abstract void visit(Tag tag);

	public void visit(AnchorTag tag) {
		visit((Tag)tag);
	}

	public void visit(BodyTag tag) {
		visit((Tag)tag);
	}

	public void visit(BoldTag tag) {
		visit((Tag)tag);
	}

	public void visit(DivTag tag) {
		visit((Tag)tag);
	}

	public void visit(HeadingTag tag) {
		visit((Tag)tag);
	}

	public void visit(HRTag tag) {
		visit((Tag)tag);
	}

	public void visit(ImageTag tag) {
		visit((Tag)tag);
	}

	public void visit(IndentTag tag) {
		visit((Tag)tag);
	}

	public void visit(ItalicTag tag) {
		visit((Tag)tag);
	}

	public void visit(ParagraphTag tag) {
		visit((Tag)tag);
	}

	public void visit(RawWikiTag tag) {
		visit((Tag)tag);
	}

	public void visit(ReferenceTag tag) {
		visit((Tag)tag);
	}

	public void visit(TextTag tag) {
		visit((Tag)tag);
	}

	public void visit(TableCaptionTag tag) {
		visit((Tag)tag);
	}

	public void visit(TableCellTag tag) {
		visit((Tag)tag);
	}

	public void visit(TableColHeadingTag tag) {
		visit((Tag)tag);
	}

	public void visit(TableRowHeadingTag tag) {
		visit((Tag)tag);
	}

	public void visit(TableRowTag tag) {
		visit((Tag)tag);
	}

	public void visit(TableTag tag) {
		visit((Tag)tag);
	}

	public void visit(TemplateParam tag) {
		visit((Tag)tag);
	}

	public void visit(TemplateTag tag) {
		visit((Tag)tag);
	}

	public void visit(UnorderedListItemTag tag) {
		visit((Tag)tag);
	}

	public void visit(UnorderedListTag tag) {
		visit((Tag)tag);
	}

}
