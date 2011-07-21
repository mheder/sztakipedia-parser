/*
 * Sztakipedia parser - http://http://code.google.com/p/sztakipedia-parser
 *
 * Copyright (C) 2011 MTA SZTAKI 
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
package hu.sztaki.sztakipediaparser.wiki.visitor.html;

import hu.sztaki.sztakipediaparser.wiki.tags.*;
import hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor;

public class HTMLVisitor implements TagVisitor {
	
	final StringBuilder out;
	
	public HTMLVisitor() {
		out = new StringBuilder();
	}
	
	String getHTML() {
		return out.toString();
	}
	
	private void visitChildren(Tag tag) {
		if (tag.getChildren() != null)
			for (Tag t : tag.getChildren())
				t.accept(this);
	} 
	

	public void visit(AnchorTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(BodyTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(BoldTag tag) {
		out.append("<b>");
		visitChildren(tag);
		out.append("</b>");
	}

	public void visit(DivTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(HeadingTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(HRTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(ImageTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(IndentTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(ItalicTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(ParagraphTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(RawWikiTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(ReferenceTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TextTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TableCaptionTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TableCellTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TableColHeadingTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TableRowHeadingTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TableRowTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TableTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TemplateParam tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(TemplateTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(UnorderedListItemTag tag) {
		// TODO Auto-generated method stub
		
	}

	public void visit(UnorderedListTag tag) {
		// TODO Auto-generated method stub
		
	}

}
