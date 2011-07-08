package hu.sztaki.sztakipediaparser.wiki.converter;

import hu.sztaki.sztakipediaparser.net.IConnector;
import hu.sztaki.sztakipediaparser.wiki.tags.AbstractTag;
import hu.sztaki.sztakipediaparser.wiki.tags.AnchorTag;
import hu.sztaki.sztakipediaparser.wiki.tags.BodyTag;
import hu.sztaki.sztakipediaparser.wiki.tags.StringTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Stack;


public class DefaultWikiConverter implements IWikiConverter {
	/**************/
	/*** Fields ***/
	/**************/
	
	/**
	 * Tree storing the html tags.
	 */
	private AbstractTag tagtree;
	
	/**
	 * The top node of the tag stack is the actual node. If adding a new node to the tree, the actual
	 * node will be its parent. If the new node is not a terminator node, i.e. it can have children, 
	 * then the actual node is checked first. If the actual node equals the new node, then the tag stack 
	 * is reduced and no new node will be added to the tree. (A terminator node can be an external link
	 * because it doesn't have any children in wikitext sense. However, a pair of ''' bold nodes can have 
	 * any other nodes between them.)
	 */
	private Stack<AbstractTag> tagStack = new Stack<AbstractTag>();
	
	/**
	 * This hashmap defines an array of css classes for each tag.
	 */
	private HashMap<Class, String[]> cssClasses = new HashMap<Class, String[]>();
	
	/**
	 * Used to number unnamed external links.
	 */
	private int urlCounter = 1;
	
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
	
	private IConnector con;
	
	/********************/
	/*** Constructors ***/
	/********************/
	
	/**
	 * Constructor with default locale "en".
	 */
	public DefaultWikiConverter() {
		tagtree = new BodyTag();
		locale = new Locale("en");
	}
	
	/**
	 * Constructor with user supplied locale.
	 * @param locale
	 */
	public DefaultWikiConverter(Locale locale) {
		tagtree = new BodyTag(locale);
		this.locale = locale;
	}

	/***************/
	/*** Methods ***/
	/***************/
	
	@Override
	public void addExternalLinkTag(String url, String alias, boolean plainlink, String wikitext) {
		AbstractTag parent = reduceTagStack();
		AnchorTag tag = new AnchorTag(parent, locale);
		
		url = trim(url);
		addCssClasses(tag);
		tag.setPlainlink(plainlink);
		tag.setWikitext(wikitext);
		tag.addAttribute("href", url);
		if(alias != null) {
			tag.addChild(new StringTag(tag, locale, alias));
		} else {
			tag.addChild(new StringTag(tag, locale, "[" + urlCounter + "]"));
			++urlCounter;
		}
		
		parent.addChild(tag);
	}
	
	@Override
	public void addInternalLinkTag(String url, String alias, String wikitext) {
		AbstractTag parent = reduceTagStack();
		AnchorTag tag = new AnchorTag(parent, locale);
		
		url = trim(url);
		addCssClasses(tag);
		tag.setPlainlink(false);
		tag.setWikitext(wikitext);
		tag.addAttribute("href", rootURL + url);
		if(alias != null) {
			tag.addChild(new StringTag(tag, locale, alias));
		} else {
			tag.addChild(new StringTag(tag, locale, url));
		}
		
		// TODO Handle Interlanguage and InterWiki links
		// TODO Hide namespaces or text in parenthesis if there is a pipe without an alias
		// TODO Handle Category links
		// TODO Handle #REDIRECTs
		// TODO Handle section links
		// TODO How to handle blended links?
		
		parent.addChild(tag);
	}
	
	@Override
	public void render(StringBuilder b) {
		tagtree.render(b);
	}
	
	@Override
	public void addCssClasses(Class C, ArrayList<String> css) {
		if(css == null || C == null) return;
		
		String[] oldCss = cssClasses.get(C);
		if(oldCss != null) {
			for(int i = 0; i < oldCss.length; i++) {
				if(!css.contains(oldCss[i])) {
					css.add(oldCss[i]);
				}
			}
		}
		
		String[] newCss = new String[css.size()];
		newCss = css.toArray(newCss);
		
		cssClasses.put(C, newCss);
	}

	/**
	 * Returns the top element of the stack, or the root node of the tree if the stack is empty.
	 * @return Top element of tag stack or the root node of the tag tree.
	 */
	private AbstractTag reduceTagStack() {
		if(tagStack.isEmpty()) {
			return tagtree;
		}
		
		return tagStack.pop();
	}
	
	/**
	 * Adds all dynamic CSS information to the tag.
	 * @param tag
	 */
	private void addCssClasses(AbstractTag tag) {
		String[] css = cssClasses.get(tag.getClass());
		if(css != null) {
			for(int i = 0; i < css.length; i++) {
				if(css[i] != null) {
					tag.addClass(css[i]);
				}
			}
		}
	}
	
	/**
	 * Trims whitespace and underscore characters from both ends of the string.
	 * @param str
	 * @return
	 */
	private String trim(String str) {
		while(str.startsWith("_") || str.startsWith(" ") || str.startsWith("\n") || 
			  str.startsWith("\t") || str.startsWith("\r")) {
			str = str.substring(1);
		}
		
		while(str.endsWith("_") || str.endsWith(" ") || str.endsWith("\n") ||
			  str.endsWith("\r") || str.endsWith("\t")) {
			str = str.substring(0, str.length() - 1);
		}
		
		return str;
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
		if(rootURL == null) return;
		
		this.rootURL = rootURL;
		
		if(!this.rootURL.endsWith("/")) {
			this.rootURL += "/";
		}
	}
	
	public String getApiUrl() {
		return apiURL;
	}
	
	public void setApiUrl(String apiURL) {
		if(apiURL == null) return;
		
		this.apiURL = apiURL;
		
		if(!this.apiURL.endsWith("/")) {
			this.apiURL += "/";
		}
	}
}
