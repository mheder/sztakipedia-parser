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


import hu.sztaki.sztakipediaparser.wiki.tags.*;

/**
 * Interface for travesals of the Tag hierarchy.
 *  
 * @author Illes Solt
 *
 */
public interface TagVisitor extends Visitor<TagVisitor, Tag> {
	void visit(AnchorTag tag);
	void visit(BodyTag tag);
	void visit(BoldTag tag);
	void visit(DivTag tag);
	void visit(HeadingTag tag);
	void visit(HRTag tag);
	void visit(ImageTag tag);
	void visit(IndentTag tag);
	void visit(ItalicTag tag);
	void visit(ParagraphTag tag);
	void visit(RawWikiTag tag);
	void visit(ReferenceTag tag);
	void visit(TextTag tag);
	void visit(TableCaptionTag tag);
	void visit(TableCellTag tag);
	void visit(TableColHeadingTag tag);
	void visit(TableRowHeadingTag tag);
	void visit(TableRowTag tag);
	void visit(TableTag tag);
	void visit(TemplateParam tag);
	void visit(TemplateTag tag);
	void visit(UnorderedListItemTag tag);
	void visit(UnorderedListTag tag);
}
