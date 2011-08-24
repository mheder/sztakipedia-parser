/**
 * 
 */
package hu.sztaki.sztakipediaparser.wiki.visitor.html;

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
import hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor;

/**
 * Visitor to convert the wiki AST to plain text, containing only the titles, and the text of the article.
 *  
 * @note
 * No template expansion takes place.
 * 
 * @author Tamas Farkas
 *
 */
public class PlainTextContentWriter implements TagVisitor {

	final StringBuilder out;
	
	public PlainTextContentWriter() {
		out = new StringBuilder();
	}
	
	/**
	 * 
	 * @return the string representing the articles text
	 */
	public String getContent(){
		return out.toString();
	}
	
	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.Visitor#dispatchVisit(hu.sztaki.sztakipediaparser.wiki.visitor.Visitable)
	 */
	public void dispatchVisit(Tag visitable) {
		visitable.accept(this);

	}
	
	private void visitChildren(Tag tag) {
		if (tag.getChildren() != null)
			for (Tag t : tag.getChildren())
				dispatchVisit(t);
	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.AnchorTag)
	 */
	public void visit(AnchorTag tag) {
		// only the text
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.BodyTag)
	 */
	public void visit(BodyTag tag) {
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.BoldTag)
	 */
	public void visit(BoldTag tag) {
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.DivTag)
	 */
	public void visit(DivTag tag) {
		out.append("\n");
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.HeadingTag)
	 */
	public void visit(HeadingTag tag) {
		out.append("\n");
		visitChildren(tag);
		out.append("\n");

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.HRTag)
	 */
	public void visit(HRTag tag) {
		out.append("\n");
	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.ImageTag)
	 */
	public void visit(ImageTag tag) {
		out.append("\n");
		visitChildren(tag);
		out.append("\n");

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.IndentTag)
	 */
	public void visit(IndentTag tag) {
		out.append("\n");
	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.ItalicTag)
	 */
	public void visit(ItalicTag tag) {
		visitChildren(tag);
	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.ParagraphTag)
	 */
	public void visit(ParagraphTag tag) {
		out.append("\n");
		visitChildren(tag);
	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.RawWikiTag)
	 */
	public void visit(RawWikiTag tag) {
		out.append(" ");
		//do nothing, its not needed
	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.ReferenceTag)
	 */
	public void visit(ReferenceTag tag) {
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TextTag)
	 */
	public void visit(TextTag tag) {
		// Append content to the output
		out.append(tag.getContent());
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TableCaptionTag)
	 */
	public void visit(TableCaptionTag tag) {
		out.append("\n");
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TableCellTag)
	 */
	public void visit(TableCellTag tag) {
		out.append("\n");
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TableColHeadingTag)
	 */
	public void visit(TableColHeadingTag tag) {
		//out.append("\n");
		//visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TableRowHeadingTag)
	 */
	public void visit(TableRowHeadingTag tag) {
		//out.append("\n");
		//visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TableRowTag)
	 */
	public void visit(TableRowTag tag) {
		//out.append("\n");
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TableTag)
	 */
	public void visit(TableTag tag) {
		out.append("\n");
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TemplateParam)
	 */
	public void visit(TemplateParam tag) {
		//do nothing

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.TemplateTag)
	 */
	public void visit(TemplateTag tag) {
		//do nothing

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.UnorderedListItemTag)
	 */
	public void visit(UnorderedListItemTag tag) {
		out.append("\n");
		visitChildren(tag);

	}

	/* (non-Javadoc)
	 * @see hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor#visit(hu.sztaki.sztakipediaparser.wiki.tags.UnorderedListTag)
	 */
	public void visit(UnorderedListTag tag) {
		out.append("\n");
		visitChildren(tag);

	}

}
