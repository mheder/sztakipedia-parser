package hu.sztaki.sztakipediaparser.wiki.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public abstract class AbstractTag {
	/**************/
	/*** Fields ***/
	/**************/
	
	/**
	 * Children nodes.
	 */
	protected ArrayList<AbstractTag> children = new ArrayList<AbstractTag>();
	
	/**
	 * Parent node.
	 */
	protected AbstractTag parent;
	
	/**
	 * HTML attributes like width="250px".
	 */
	protected HashMap<String, String> attributes = new HashMap<String, String>();
	
	/**
	 * List of css classes of the tag.
	 */
	protected ArrayList<String> classes = new ArrayList<String>();
	
	/**
	 * Store raw wikitext just in case.
	 */
	protected String wikitext = "";
	
	/**
	 * Language settings.
	 */
	protected Locale locale;
	
	/********************/
	/*** Constructors ***/
	/********************/
	
	/**
	 * Constructor for a root node with default "en" locale. Its parent is null.
	 */
	public AbstractTag() {
		this(new Locale("en"));
	}
	
	/**
	 * Constructor for a root node with user defined locale. Its parent is null.
	 * @param locale
	 */
	public AbstractTag(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * Constructor for a non-root node with default locale "en".
	 * @param parent
	 */
	public AbstractTag(AbstractTag parent) {
		this(new Locale("en"));
		this.parent = parent;
	}
	
	/**
	 * Constructor for a non-root node with user defined locale.
	 * @param parent Parent node.
	 * @param locale
	 */
	public AbstractTag(AbstractTag parent, Locale locale) {
		this.parent = parent;
		this.locale = locale;
	}
	
	/***************/
	/*** Methods ***/
	/***************/
	
	/**
	 * Adds a single child to this node's children.
	 * @param c New child.
	 */
	public void addChild(AbstractTag c) {
		children.add(c);
	}
	
	/**
	 * Adds a new attribute to the node. Like width="250px".
	 * @param key Name of attribute.
	 * @param value Value of attribute.
	 */
	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	/**
	 * Adds a new css class to the tag.
	 * @param c Single class definition.
	 */
	public void addClass(String c) {
		if(!classes.contains(c)) {
			classes.add(c);
		}
	}
	
	/**
	 * Adds several css classes to the tag.
	 * @param c ArrayList of class definitions.
	 */
	public void addClasses(ArrayList<String> c) {
		for(String ci : c) {
			if(!classes.contains(ci)) {
				classes.add(ci);
			}
		}
	}
	
	/**
	 * Return the appropriate html code string.
	 * @return
	 */
	public abstract void render(StringBuilder b);
	
	/***************************/
	/*** Getters and Setters ***/
	/***************************/
	
	public ArrayList<AbstractTag> getChildren() {
		return children;
	}
	
	public AbstractTag getParent() {
		return parent;
	}
	
	public HashMap<String, String> getAttributes() {
		return attributes;
	}
	
	public ArrayList<String> getClasses() {
		return classes;
	}
	
	public String getWikitext() {
		return wikitext;
	}
	
	public void setWikitext(String wikitext) {
		this.wikitext = wikitext;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocal(Locale locale) {
		this.locale = locale;
	}
}
