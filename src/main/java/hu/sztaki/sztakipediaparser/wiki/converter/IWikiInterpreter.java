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
package hu.sztaki.sztakipediaparser.wiki.converter;

import hu.sztaki.sztakipediaparser.wiki.tags.Tag;

import java.util.List;

/**
 * Interface for the wiki converter.
 * 
 * <p>
 * This interface defines all the methods that a wiki converter class has to
 * implement.
 * </p>
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public interface IWikiInterpreter {
	/**
	 * Appends an external link to the output buffer. See <a
	 * href="http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs">Links
	 * and URLs</a>
	 * 
	 * @param url
	 *            URL of the link. The validity of the url is NOT checked by the
	 *            parser.
	 * @param alias
	 *            Display name of the url.
	 * @param plainlink
	 *            Plainlink URL.
	 * @param wikitext
	 *            Original raw wikitext of the tag.
	 */
	public void addExternalLinkTag(String url, String alias, boolean plainlink,
			String wikitext);

	/**
	 * Appends an internal wiki link to the output buffer. e.g. [[Texas|Lone
	 * Star State]] Everey link is treated as internal if it begins with double
	 * brackets, so File:, Media: and special links are also internal. The
	 * method has to figure out which link is which from the url parameter and
	 * decide what to do with it.
	 * 
	 * @param url
	 *            It is not an URL but the name of the article to link to.
	 * @param params
	 *            Parameters of the internal link tag. Can be just an alias or
	 *            an array of image parameters.
	 * @param wikitext
	 *            The raw wikitext.
	 */
	public void addInternalLinkTag(String url, List<String> params,
			String wikitext);

	/**
	 * Adds a new StringTag to the tree.
	 * 
	 * @param content
	 *            Content of the tag.
	 */
	public void addStringTag(String content);

	/**
	 * Mark the start of a bold tag.
	 */
	public void startBoldTag(String wikitext);

	/**
	 * Mark the end of a bold tag.
	 */
	public void endBoldTag();

	/**
	 * Mark the start of an italic tag.
	 */
	public void startItalicTag(String wikitext);

	/**
	 * Mark the end of an italic tag.
	 */
	public void endItalicTag();

	/**
	 * Mark the start of a section heading, ie. <code>==Section Heading==</code>
	 * 
	 * @param wikitext
	 */
	public void startSectionHeading(String wikitext);

	/**
	 * Mark the end of a section heading, ie. <code>==Section Heading==</code>
	 */
	public void endSectionHeading();

	/**
	 * Mark the start of a subsection heading, ie.
	 * <code>===Subsection Heading===</code>
	 * 
	 * @param wikitext
	 */
	public void startSubSectionHeading(String wikitext);

	/**
	 * Mark the end of a subsection heading, ie. ===Subsection Heading===
	 */
	public void endSubSectionHeading();

	/**
	 * Mark the start of a sub-subsection heading, ie. ===Subsection Heading===
	 * 
	 * @param wikitext
	 */
	public void startSubSubSectionHeading(String wikitext);

	/**
	 * Mark the end of a sub-subsection heading, ie. ===Subsection Heading===
	 */
	public void endSubSubSectionHeading();

	/**
	 * Adds a horizontal ruler
	 * 
	 * @param wikitext
	 */
	public void addHorizontalRuler(String wikitext);

	/**
	 * Starts a new paragraph.
	 * 
	 * @param wikitext
	 */
	public void startParagraph(String wikitext);

	/**
	 * Ends the actual paragraph.
	 */
	public void endParagraph();

	/**
	 * Add a wikitemplate.
	 * 
	 * @param str
	 *            Whole, unparsed template string, everything between {{ and }}
	 *            operators.
	 * @param multiline
	 *            True if the original wikitext had newlines.
	 */
	public void addTemplate(String str, boolean multiline);

	/**
	 * Adds raw wikitext to the output.
	 * 
	 * @param wikitext
	 */
	public void addRawWikiTag(String wikitext);

	/**
	 * Adds indent to the output.
	 * 
	 * @param wikitext
	 * @param level
	 *            Level can be 1,2 or 3, it describes the depth of the indent.
	 */
	public void addIndentTag(String wikitext, int level);

	/**
	 * Starts an unordered list.
	 */
	public void startList(boolean wrap);

	/**
	 * Ends an unordered list.
	 */
	public void endList();

	/**
	 * Adds a list item with wiki markup string str.
	 * 
	 * @param str
	 *            Wiki markup of the list item.
	 */
	public void addListItem(String str);

	/**
	 * Adds a table tag with the specified parameters and caption. Both are
	 * optional.
	 * 
	 * @param params
	 * @param caption
	 */
	public void startTable(String params, String caption);

	/**
	 * Ends a table and pops the table tag from the stack.
	 */
	public void endTable();

	/**
	 * Adds a table heading. The heading may have parameters.
	 * 
	 * @param heading
	 * @param params
	 */
	public void addTableColHeading(String heading, String params);

	/**
	 * Adds a table row heading. The heading may have parameters.
	 * 
	 * @param heading
	 * @param params
	 */
	public void addTableRowHeading(String heading, String params);

	/**
	 * Adds a new row to the table.
	 * 
	 * @param params
	 *            Format modifiers.
	 */
	public void startTableRow(String params);

	/**
	 * Ends a table row.
	 */
	public void endTableRow();

	/**
	 * Adds a new cell to the table.
	 * 
	 * @param content
	 *            Content of the cell.
	 * @param params
	 *            Format modifiers.
	 */
	public void addTableCell(String content, String params);

	/**
	 * Handles <code>&lt;ref ...> ... &lt;/ref></code> tags in wikitext.
	 * 
	 * @param str
	 */
	public void addReferenceTag(String str);

	/**
	 * Render the conversion output to the supplied StringBuilder.
	 * 
	 * @param b
	 * 
	 */
	
	public void render(StringBuilder b);

	/**
	 * Clear tag stack.
	 */
	public void reset();
	
	
	/**
	 * Clear tag stack, and new root item to tagtree
	 */
	public void reInitialize();

	/**
	 * Adds the supplied array of CSS classes to the specified class.
	 * 
	 * @param tagClass
	 *            The class to apply the CSS classes to.
	 * @param css
	 *            An array of Strings describing the CSS classes.
	 */
	public void addCssClasses(Class<? extends Tag> tagClass, List<String> css);

	/**
	 * Perform any preprocessing.
	 * 
	 * @param wikitext
	 * @return Modified wikitext.
	 */
	public String preprocess(String wikitext);

	/**
	 * Perform any postprocessing.
	 * 
	 * @param wikitext
	 */
	public void postprocess(String wikitext);

	/**
	 * Sets the setBeginEnd postprocessor switch. If the switch is true, then
	 * the character position of the beginning and ending of String tags are
	 * stored in the output HTML.
	 * 
	 * @param setBeginEnd
	 */
	public void setStoreBeginEnd(boolean setBeginEnd);

	/**
	 * Returns the value of the setBeginEnd postprocessor switch.
	 */
	public boolean getStoreBeginEnd();

}
