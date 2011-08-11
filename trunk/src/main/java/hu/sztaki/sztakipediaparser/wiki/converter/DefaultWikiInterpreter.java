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

import hu.sztaki.sztakipediaparser.localization.DefaultLanguageHandler;
import hu.sztaki.sztakipediaparser.wiki.parser.IWikiParser;
import hu.sztaki.sztakipediaparser.wiki.parser.JavaCCWikiParser;
import hu.sztaki.sztakipediaparser.wiki.parser.Splitter;
import hu.sztaki.sztakipediaparser.wiki.tags.AnchorTag;
import hu.sztaki.sztakipediaparser.wiki.tags.BodyTag;
import hu.sztaki.sztakipediaparser.wiki.tags.BoldTag;
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
import hu.sztaki.sztakipediaparser.wiki.tags.TemplateTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TextTag;
import hu.sztaki.sztakipediaparser.wiki.tags.UnorderedListItemTag;
import hu.sztaki.sztakipediaparser.wiki.tags.UnorderedListTag;
import hu.sztaki.sztakipediaparser.wiki.visitor.html.HTMLVisitor;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

/**
 * Converter class.
 * 
 * <p>
 * This class builds and stores the abstract syntax tree during the parsing
 * process. Provides methods for the parser to add nodes to the tree. It can
 * preprocess the original wikitext and postprocess the finished AST. It is also
 * responsible for the generation of the html output from the AST.
 * </p>
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class DefaultWikiInterpreter implements IWikiInterpreter {
	/**************/
	/*** Fields ***/
	/**************/

	/**
	 * Tree storing the html tags.
	 */
	private Tag tagtree;

	/**
	 * The top node of the tag stack is the actual node. If adding a new node to
	 * the tree, the actual node will be its parent. If the new node is not a
	 * terminator node, i.e. it can have children, then the actual node is
	 * checked first. If the actual node equals the new node, then the tag stack
	 * is reduced and no new node will be added to the tree. (A terminator node
	 * can be an external link because it doesn't have any children in wikitext
	 * sense. However, a pair of ''' bold nodes can have any other nodes between
	 * them.)
	 */
	private Stack<Tag> tagStack = new Stack<Tag>();

	/**
	 * This hashmap defines an array of css classes for each tag.
	 */
	private Map<Class<? extends Tag>, String[]> cssClasses = new HashMap<Class<? extends Tag>, String[]>();

	/**
	 * Used to number unnamed external links.
	 */
	private int urlCounter = 1;

	/**
	 * Used to number template tables.
	 */
	private int templateID = 1;

	/**
	 * Used to number references.
	 */
	private int refID = 1;

	/**
	 * Language settings.
	 */
	private Locale locale;

	/**
	 * Root URL for every link. The default is http://en.wikipedia.org/wiki/
	 */
	private String rootURL = "http://en.wikipedia.org/wiki/";

	/**
	 * Wikipedia REST API URL.
	 */
	private String apiURL = "http://en.wikipedia.org/w/api.php";

	/**
	 * URL of uploaded media.
	 */
	private String mediaUrl = "http://upload.wikimedia.org/wikipedia/commons/";

	/**
	 * Wikitext parser for parsing recursive content.
	 */
	private IWikiParser parser;

	/**
	 * MD% digest for image urls.
	 */
	private MessageDigest md;

	/****************************************/
	/*** Pre- and postprocessing switches ***/
	/****************************************/
	/**
	 * Perform preprocessing, ie. call the preprocess method on the wikitext
	 * before parsing.
	 */
	private boolean preprocess = true;

	/**
	 * Perform postprocess, ie. call the postprocess method on the generated
	 * tree after parsing.
	 */
	private boolean postprocess = true;

	/**
	 * Store begin and end character positions of free text. In the resulting
	 * HTML all free text will be enclosed in a <span begin="..."
	 * end="..."></span> element, where the begin property means that the text
	 * started in that position in the original wikitext. The end property has a
	 * similar meaning.
	 */
	private boolean setBeginEnd = false;

	/**
	 * If true, the postprocessing step autlinks every free URLs in the text.
	 * See Bare URLs at
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#External_links
	 */
	private boolean autoLink = true;

	/********************/
	/*** Constructors ***/
	/********************/

	/**
	 * Constructor with default locale "en".
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws NoSuchAlgorithmException
	 */
	public DefaultWikiInterpreter() throws MalformedURLException, IOException,
			NoSuchAlgorithmException {
		tagtree = new BodyTag();
		locale = new Locale("en");

		// Create digest instance
		md = MessageDigest.getInstance("MD5");
	}

	/**
	 * Constructor with user supplied locale.
	 * 
	 * @param locale
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws NoSuchAlgorithmException
	 */
	public DefaultWikiInterpreter(Locale locale, String rootURL, String apiURL,
			String mediaUrl) throws MalformedURLException, IOException,
			NoSuchAlgorithmException {
		tagtree = new BodyTag();

		// Set language
		this.locale = locale;
		DefaultLanguageHandler.setLocale(locale);

		// Set URLs
		this.rootURL = rootURL;
		this.apiURL = apiURL;
		this.mediaUrl = mediaUrl;

		// Create digest instance
		md = MessageDigest.getInstance("MD5");
	}

	/***************/
	/*** Methods ***/
	/***************/

	public void addExternalLinkTag(String url, String alias, boolean plainlink,
			String wikitext) {
		Tag parent = peekTagStack();
		AnchorTag tag = new AnchorTag(parent);

		url = InterpreterUtils.trim(url);
		addCssClasses(tag);
		tag.setPlainlink(plainlink);
		tag.setWikitext(wikitext);
		tag.addAttribute("href", url);
		if (alias != null && !alias.isEmpty()) {
			// Parse alias recursively
			pushToStack(tag); // Push to stack, so that this node will be the
			// parent of subsequent nodes parsed
			// recursively.
			parseRecursively(alias);
			reduceTagStack(); // Finished with this tag, pop it from the stack.
		} else {
			// There is no alias so use the url counter.
			tag.addChild(new TextTag(tag, "[" + urlCounter + "]"));
			++urlCounter;
		}

		// Set relationship to the parent tag
		parent.addChild(tag);
	}

	public void addInternalLinkTag(String url, List<String> params,
			String wikitext) {
		// Handle File and Image links
		if (url.startsWith(DefaultLanguageHandler
				.getWord(DefaultLanguageHandler.WIKI_LINK_FILE)
				+ ":")
				|| url.startsWith(DefaultLanguageHandler
						.getWord(DefaultLanguageHandler.WIKI_LINK_IMAGE)
						+ ":")
				|| url.startsWith(":"
						+ DefaultLanguageHandler
								.getWord(DefaultLanguageHandler.WIKI_LINK_FILE)
						+ ":")
				|| url
						.startsWith(":"
								+ DefaultLanguageHandler
										.getWord(DefaultLanguageHandler.WIKI_LINK_IMAGE)
								+ ":")) {
			addImageTag(url, params, wikitext);
			return;
		}

		Tag parent = peekTagStack();
		AnchorTag tag = new AnchorTag(parent);

		// TODO Handle Interlanguage and InterWiki links
		// TODO Hide namespaces or text in parenthesis if there is a pipe
		// without an alias
		// TODO Handle Category links
		// TODO Handle #REDIRECTs
		// TODO Handle section links
		// TODO How to handle blended links?

		tag.addAttribute("title", url);
		String origurl = url;
		url = InterpreterUtils.trim(url);
		addCssClasses(tag);
		tag.setWikitext(wikitext);
		tag.addAttribute("href", rootURL + url);
		if (!params.isEmpty()) {
			String alias = params.get(params.size() - 1);

			if (!origurl.equals(alias)) {
				tag.addClass("different");
			}

			// Parse alias recursively
			pushToStack(tag); // Push to stack, so that this node will be the
			// parent of subsequent nodes parsed
			// recursively.
			parseRecursively(alias);
			reduceTagStack(); // Finished with this tag, pop it from the stack.
		} else {
			// Use the URL as alias if none specified
			tag.addChild(new TextTag(tag, url));
		}

		parent.addChild(tag);
	}

	public void addStringTag(String content) {
		Tag parent = peekTagStack();

		TextTag tag = new TextTag(parent, content);
		parent.addChild(tag);
	}

	public void startBoldTag(String wikitext) {
		// Query parent tag
		Tag parent = peekTagStack();

		// Create tag and set properties
		BoldTag tag = new BoldTag(parent);
		tag.setWikitext(wikitext);

		// Set relationship with parent
		parent.addChild(tag);

		// Push new tag to tag stack
		pushToStack(tag);
	}

	public void endBoldTag() {
		reduceTagStack();
	}

	public void startItalicTag(String wikitext) {
		// Query parent tag
		Tag parent = peekTagStack();

		// Create tag and set properties
		ItalicTag tag = new ItalicTag(parent);
		tag.setWikitext(wikitext);

		// Set relationship with parent
		parent.addChild(tag);

		// Push new tag to tag stack
		pushToStack(tag);
	}

	public void endItalicTag() {
		reduceTagStack();
	}

	public void startSectionHeading(String wikitext) {
		// Get parent tag
		Tag parent = peekTagStack();

		// Create tag and set properties
		HeadingTag tag = new HeadingTag(parent);
		tag.setLevel(1);
		tag.setWikitext(wikitext);

		// Set relationship with parent
		parent.addChild(tag);

		// Push to stack
		pushToStack(tag);
	}

	public void endSectionHeading() {
		reduceTagStack();
	}

	public void startSubSectionHeading(String wikitext) {
		// Get parent tag
		Tag parent = peekTagStack();

		// Create tag and set properties
		HeadingTag tag = new HeadingTag(parent);
		tag.setLevel(2);
		tag.setWikitext(wikitext);

		// Set relationship with parent
		parent.addChild(tag);

		// Push to stack
		pushToStack(tag);
	}

	public void endSubSectionHeading() {
		reduceTagStack();
	}

	public void startSubSubSectionHeading(String wikitext) {
		// Get parent tag
		Tag parent = peekTagStack();

		// Create tag and set properties
		HeadingTag tag = new HeadingTag(parent);
		tag.setLevel(3);
		tag.setWikitext(wikitext);

		// Set relationship with parent
		parent.addChild(tag);

		// Push to stack
		pushToStack(tag);
	}

	public void endSubSubSectionHeading() {
		reduceTagStack();
	}

	public void addHorizontalRuler(String wikitext) {
		// Get parent tag
		Tag parent = peekTagStack();

		// Create HRTag and set parameters
		HRTag tag = new HRTag(parent);
		tag.setWikitext(wikitext);

		// Set relationship to parent
		parent.addChild(tag);
	}

	public void startParagraph(String wikitext) {
		// Get parent tag
		Tag parent = peekTagStack();

		// Create ParagraphTag and set its properties
		ParagraphTag tag = new ParagraphTag(parent);
		tag.setWikitext(wikitext);

		// Set relationship to parent
		parent.addChild(tag);

		// Push to tagstack
		pushToStack(tag);
	}

	public void endParagraph() {
		reduceTagStack();
	}

	public void addTemplate(String str, boolean multiline) {
		// Parse template string recursively with a new converter
		try {
			DefaultWikiInterpreter c = new DefaultWikiInterpreter(locale,
					rootURL, apiURL, mediaUrl);
			c.setUrlCounter(urlCounter);
			c.setTemplateID(templateID);
			for (Class<? extends Tag> key : cssClasses.keySet()) {
				String[] classes = cssClasses.get(key);
				ArrayList<String> value = new ArrayList<String>(classes.length);
				for (int i = 0; i < classes.length; i++) {
					value.add(classes[i]);
				}

				c.addCssClasses(key, value);
			}
			IWikiParser p = new JavaCCWikiParser(new StringReader(str));
			p.parse(c);
			StringBuilder b = new StringBuilder();
			c.render(b, false);
			String result = b.toString();
			templateID = c.getTemplateID();

			// Extract template name and parameters
			String[] params = result.split("\\|");
			String name = params[0];

			int index = 1;
			Tag parent = peekTagStack();
			TemplateTag tag = new TemplateTag(parent);
			tag.addClass("wiki-template");
			if (multiline) {
				tag.addClass("multiline");
			} else {
				tag.addClass("inline");
			}
			// tag.setWikitext("{{" + str + "}}");
			tag.setName(name);
			tag.setID(templateID++);
			parent.addChild(tag);

			// Set parameters for the tag
			for (int i = 1; i < params.length; i++) {
				String[] nameValuePair = params[i].split("=", 2);

				if (nameValuePair.length == 1) {
					// Unnamed parameter
					tag.addParameter(String.valueOf(index), nameValuePair[0],
							true);
					++index;
				} else {
					// Named parameter or the = sign marks an html parameter
					// such as <a href=...
					if (nameValuePair[0].lastIndexOf(">") < nameValuePair[0]
							.lastIndexOf("<")) {
						// bogus = sign, add an unnamed parameter
						tag
								.addParameter(String.valueOf(index),
										nameValuePair[0] + "="
												+ nameValuePair[1], true);
						++index;
					} else {
						tag.addParameter(nameValuePair[0], nameValuePair[1],
								false);
					}
				}
			}
		} catch (IOException ex) {

		} catch (NoSuchAlgorithmException e) {

		}
	}

	public void addRawWikiTag(String wikitext) {
		Tag parent = peekTagStack();
		RawWikiTag tag = new RawWikiTag(parent);
		tag.setWikitext(wikitext);
		parent.addChild(tag);
	}

	public void addIndentTag(String wikitext, int level) {
		String w = "";
		for (int i = 0; i < level; i++) {
			w += wikitext;
		}

		Tag parent = peekTagStack();
		IndentTag tag = new IndentTag(parent);
		tag.setWikitext(w);
		tag.setLeveL(level);
		parent.addChild(tag);
	}

	public void startList(boolean wrap) {
		Tag parent = peekTagStack();

		UnorderedListTag tag = new UnorderedListTag(parent);
		tag.setWrap(wrap);
		parent.addChild(tag);

		pushToStack(tag);
	}

	public void endList() {
		reduceTagStack();
	}

	public void addListItem(String str) {
		Tag parent = peekTagStack();

		UnorderedListItemTag tag = new UnorderedListItemTag(parent);
		parent.addChild(tag);

		// Parse content recursively
		pushToStack(tag);
		parseRecursively(str);
		reduceTagStack();
	}

	public void startTable(String params, String caption) {
		Tag parent = peekTagStack();

		TableTag tag = new TableTag(parent, params);
		parent.addChild(tag);

		// Parse caption recursively.
		if (caption != null && !caption.isEmpty()) {
			TableCaptionTag captionTag = new TableCaptionTag();
			pushToStack(captionTag);
			parseRecursively(caption);
			reduceTagStack();
			tag.setCaption(captionTag);
		}

		pushToStack(tag);
	}

	public void endTable() {
		reduceTagStack();
	}

	public void addTableColHeading(String heading, String params) {
		Tag parent = peekTagStack();

		if (parent instanceof TableRowTag) {
			TableColHeadingTag tag = new TableColHeadingTag(parent, params);
			parent.addChild(tag);

			if (heading != null && !heading.isEmpty()) {
				pushToStack(tag);
				parseRecursively(heading);
				reduceTagStack();
			}
		}
	}

	public void addTableRowHeading(String heading, String params) {
		Tag parent = peekTagStack();

		if (parent instanceof TableRowTag) {
			TableRowTag row = (TableRowTag) parent;
			TableRowHeadingTag tag = new TableRowHeadingTag(row, params);
			row.addChild(tag);

			if (heading != null && !heading.isEmpty()) {
				pushToStack(tag);
				parseRecursively(heading);
				reduceTagStack();
			}
		}
	}

	public void startTableRow(String params) {
		Tag parent = peekTagStack();

		if (parent instanceof TableTag) {
			TableTag table = (TableTag) parent;
			TableRowTag tag = new TableRowTag(table, params);
			table.addChild(tag);
			pushToStack(tag);
		}
	}

	public void endTableRow() {
		reduceTagStack();
	}

	public void addTableCell(String content, String params) {
		Tag parent = peekTagStack();

		if (parent instanceof TableRowTag) {
			TableRowTag row = (TableRowTag) parent;
			TableCellTag tag = new TableCellTag(row, params);
			row.addChild(tag);

			if (content != null && !content.isEmpty()) {
				pushToStack(tag);
				parseRecursively(content);
				reduceTagStack();
			}
		}
	}

	public void addReferenceTag(String str) {
		Tag parent = peekTagStack();
		ReferenceTag tag = new ReferenceTag(parent, refID);
		++refID;

		// Parse recursively
		if (str != null && !str.isEmpty()) {
			pushToStack(tag);
			parseRecursively(str);
			reduceTagStack();
		}

		parent.addChild(tag);
	}

	public void render(StringBuilder b) {
		render(b, true);
	}

	public void render(StringBuilder b, boolean visitRoot) {
		HTMLVisitor v = new HTMLVisitor();
		if (visitRoot) {
			v.dispatchVisit(tagtree);
		} else {
			for (Tag c : tagtree.getChildren()) {
				v.dispatchVisit(c);
			}
		}
		b.append(v.getHTML());
	}

	public void reset() {
		tagStack.clear();
	}

	public void addCssClasses(Class<? extends Tag> C, List<String> css) {
		if (css == null || C == null)
			return;

		List<String> classes = new ArrayList<String>();
		classes.add("container");

		String[] oldCss = cssClasses.get(C);
		if (oldCss != null) {
			for (int i = 0; i < oldCss.length; i++) {
				if (!css.contains(oldCss[i])) {
					css.add(oldCss[i]);
				}
			}
		}

		String[] newCss = new String[css.size()];
		newCss = css.toArray(newCss);

		cssClasses.put(C, newCss);
	}

	/**
	 * This method adds an internal image link tag. e.g. [[File:...]]
	 * 
	 * @param url
	 *            URL of the image, following the File: keyword.
	 * @param params
	 *            Array of image parameters including the caption.
	 * @param wikitext
	 *            Original wikitext.
	 */
	private void addImageTag(String url, List<String> params, String wikitext) {
		String realUrl = "";

		// Special cases
		if (url.startsWith(":")) {
			// Linking directly to the description page of the image
			url = url.substring(1);
			Tag parent = peekTagStack();

			// Create the link tag. Its parent is the actual node on top of the
			// stack.
			AnchorTag aTag = new AnchorTag(parent);

			// Set alias
			String alias = "";
			if (!params.isEmpty()) {
				// Alias is the last parameter
				alias = params.get(params.size() - 1);
			} else {
				// Alias is the URL
				alias = url;
			}

			// Parse the alias recursively
			pushToStack(aTag);
			parseRecursively(alias);
			reduceTagStack(); // No other children for the aTag, pop it from the
			// stack.

			// Set properties of the link tag
			realUrl = rootURL + url; // Real URL is like
			// http://en.wikipedia.org/wiki/File:Example.svg
			aTag.addAttribute("href", realUrl);
			aTag.setWikitext(wikitext);

			// Set relationships
			parent.addChild(aTag);

			return;
		}

		// Calculate real url of the image according to this:
		// https://secure.wikimedia.org/wikipedia/commons/wiki/FAQ#What_are_the_strangely_named_components_in_file_paths.3F
		String filename = url.substring(url.indexOf(":") + 1).replaceAll(" ",
				"_");
		try {
			md.update(filename.getBytes("UTF-8"));
			String digest = InterpreterUtils.byteArray2Hex(md.digest());
			realUrl = mediaUrl + digest.charAt(0) + "/" + digest.charAt(0)
					+ digest.charAt(1) + "/"
					+ URLEncoder.encode(filename, "UTF-8");

			// Create nodes
			Tag parent = peekTagStack();

			ImageTag imgTag = new ImageTag(parent);
			imgTag.setWikitext(wikitext);
			imgTag.addAttribute("src", realUrl);
			imgTag.setLink(rootURL + url);

			// Extract image parameters, classes and caption
			ArrayList<String> params2 = new ArrayList<String>(params);
			for (String p : params2) {
				if (p.equals("thumb") || p.equals("thumbnail")) {
					imgTag.setType(ImageTag.TYPE_THUMB);
					params.remove(p);
				} else if (p.equals("frame")) {
					imgTag.setType(ImageTag.TYPE_FRAME);
					params.remove(p);
				} else if (p.equals("frameless")) {
					imgTag.setType(ImageTag.TYPE_FRAMELESS);
					params.remove(p);
				} else if (p.equals("border")) {
					imgTag.setBorder(true);
					params.remove(p);
				} else if (p.equals("right")) {
					imgTag.setLocation(ImageTag.LOCATION_RIGHT);
					params.remove(p);
				} else if (p.equals("left")) {
					imgTag.setLocation(ImageTag.LOCATION_LEFT);
					params.remove(p);
				} else if (p.equals("center")) {
					imgTag.setLocation(ImageTag.LOCATION_CENTER);
					params.remove(p);
				} else if (p.equals("none")) {
					imgTag.setLocation(ImageTag.LOCATION_NONE);
					params.remove(p);
				} else if (p.equals("baseline")) {
					imgTag.setAlignment(ImageTag.ALIGN_BASELINE);
					params.remove(p);
				} else if (p.equals("middle")) {
					imgTag.setAlignment(ImageTag.ALIGN_MIDDLE);
					params.remove(p);
				} else if (p.equals("sub")) {
					imgTag.setAlignment(ImageTag.ALIGN_SUB);
					params.remove(p);
				} else if (p.equals("super")) {
					imgTag.setAlignment(ImageTag.ALIGN_SUPER);
					params.remove(p);
				} else if (p.equals("text-top")) {
					imgTag.setAlignment(ImageTag.ALIGN_TEXT_TOP);
					params.remove(p);
				} else if (p.equals("text-bottom")) {
					imgTag.setAlignment(ImageTag.ALIGN_TEXT_BOTTOM);
					params.remove(p);
				} else if (p.equals("top")) {
					imgTag.setAlignment(ImageTag.ALIGN_TOP);
					params.remove(p);
				} else if (p.equals("bottom")) {
					imgTag.setAlignment(ImageTag.ALIGN_BOTTOM);
					params.remove(p);
				} else if (p.equals("upright")) {
					imgTag.setUpright(true);
					params.remove(p);
				} else if (p.matches("^upright=\\d+(.\\d+)?$")) {
					String[] s = p.split("=");
					float factor = Float.parseFloat(s[1]);
					imgTag.setUprightFactor(factor);
					params.remove(p);
				} else if (p.matches("^\\d+px$")) {
					String[] s = p.split("px");
					int width = Integer.parseInt(s[0]);
					imgTag.setWidth(width);
					params.remove(p);
				} else if (p.matches("^x\\d+px$")) {
					String[] s = p.split("px");
					int height = Integer.parseInt(s[0].substring(1));
					imgTag.setHeight(height);
					params.remove(p);
				} else if (p.matches("^\\d+x\\d+px$")) {
					String[] s = p.split("px");
					String[] s2 = s[0].split("x");
					int width = Integer.parseInt(s2[0]);
					int height = Integer.parseInt(s2[1]);
					imgTag.setWidth(width);
					imgTag.setHeight(height);
					params.remove(p);
				} else if (p.equals("link=")) {
					imgTag.setEnableLink(false);
					params.remove(p);
				} else if (p.startsWith("link=")) {
					String link = p.substring(5);
					imgTag.setLink(link);
					params.remove(p);
				}
			}

			// If there is left something in params that is not recognized, then
			// it's a caption
			if (!params.isEmpty()) {
				String caption = params.get(params.size() - 1);
				imgTag.setCaption(caption);

				// Parse caption recursively
				pushToStack(imgTag);
				parseRecursively(caption);
				reduceTagStack();
			}

			parent.addChild(imgTag);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recursively parses the given source with this converter object.
	 * 
	 * @param source
	 */
	private void parseRecursively(String source) {
		parser = new JavaCCWikiParser(new StringReader(source));
		parser.parse(this);
	}

	/**
	 * Returns the top element of the stack, or the root node of the tree if the
	 * stack is empty.
	 * 
	 * @return Top element of tag stack or the root node of the tag tree.
	 */
	private Tag reduceTagStack() {
		if (tagStack.isEmpty()) {
			return tagtree;
		}

		return tagStack.pop();
	}

	/**
	 * Returns the top element of the stack without removing it or the tree if
	 * the stack is empty.
	 * 
	 * @return Top element of tag stack or the root node of the tag tree.
	 */
	private Tag peekTagStack() {
		if (tagStack.isEmpty()) {
			return tagtree;
		}

		return tagStack.peek();
	}

	/**
	 * Pushes a new Tag to the tag stack.
	 * 
	 * @param tag
	 */
	private void pushToStack(Tag tag) {
		tagStack.push(tag);
	}

	/**
	 * Adds all dynamic CSS information to the tag.
	 * 
	 * @param tag
	 */
	private void addCssClasses(Tag tag) {
		String[] css = cssClasses.get(tag.getClass());
		if (css != null) {
			for (int i = 0; i < css.length; i++) {
				if (css[i] != null) {
					tag.addClass(css[i]);
				}
			}
		}
	}

	public String preprocess(String wikitext) {
		if (preprocess) {

		}
		return wikitext;
	}

	public void postprocess(String wikitext) {
		// Run postprocessing
		if (postprocess) {
			Tag node;
			int lastmatch = 0;
			List<Tag> queue = new ArrayList<Tag>();
			queue.add(tagtree);

			while (!queue.isEmpty()) {
				node = queue.remove(0);

				// Autolink bare urls.
				if (autoLink) {
					if (node instanceof TextTag) {
						Splitter splitter = new Splitter(
								"(((http://|https://|ftp://)?www\\.)|((http://|https://|ftp://)(www\\.)?))[A-Za-z0-9\\._/~%\\-\\+&#\\?!=\\(\\)@\\\\]+",
								true);
						String content = ((TextTag) node).getContent();
						String[] parts = splitter.split(content);

						((TextTag) node).setContent(parts[0]);
						for (int i = 1; i < parts.length; i++) {
							if (i % 2 == 0 && !parts[i].isEmpty()) {
								// Content
								TextTag tag = new TextTag(node, parts[i]);
								node.addChild(tag);
							} else if (!parts[i].isEmpty()) {
								// URL
								// TODO Create an URLTag instead of TemplateTag
								TemplateTag tag = new TemplateTag(node, false);
								tag.setName("URL");
								tag.addClass("wiki-template");
								tag.addClass("inline");
								tag.addParameter("1", parts[i], true);
								tag.addParameter("2", " ", true);
								node.addChild(tag);
							}
						}
					}
				}

				// Set begin and end parameters for String tags and Raw wiki
				// tags.
				if (setBeginEnd) {
					if (node instanceof TextTag || node instanceof RawWikiTag) {
						String content = "";
						if (node instanceof TextTag)
							content = ((TextTag) node).getContent();
						if (node instanceof RawWikiTag)
							content = ((RawWikiTag) node).getWikitext();

						int match = wikitext.indexOf(content, lastmatch);
						if (match != -1) {
							lastmatch = match + content.length();
							node.setBegin(match);
							node.setEnd(lastmatch);
						}
					}
				}

				List<Tag> children = node.getChildren();
				if (children != null) {
					queue.addAll(0, children);
				}
			}
		}
	}

	/***************************/
	/*** Getters and Setters ***/
	/***************************/

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getRootUrl() {
		return rootURL;
	}

	public void setRootUrl(String rootURL) {
		if (rootURL == null)
			return;

		this.rootURL = rootURL;

		if (!this.rootURL.endsWith("/")) {
			this.rootURL += "/";
		}
	}

	public String getApiUrl() {
		return apiURL;
	}

	public void setApiUrl(String apiURL) {
		if (apiURL == null)
			return;

		this.apiURL = apiURL;

		if (!this.apiURL.endsWith("/")) {
			this.apiURL += "/";
		}
	}

	public void setUrlCounter(int urlCounter) {
		this.urlCounter = urlCounter;
	}

	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}

	public int getTemplateID() {
		return templateID;
	}

	public void setStoreBeginEnd(boolean setBeginEnd) {
		this.setBeginEnd = setBeginEnd;
	}

	public boolean getStoreBeginEnd() {
		return setBeginEnd;
	}

	public void setPreprocess(boolean preprocess) {
		this.preprocess = preprocess;
	}

	public void setPostprocess(boolean postprocess) {
		this.postprocess = postprocess;
	}

	public void setAutoLink(boolean autoLink) {
		this.autoLink = autoLink;
	}

	public void setBeginEnd(boolean setBeginEnd) {
		this.setBeginEnd = setBeginEnd;
	}
}
