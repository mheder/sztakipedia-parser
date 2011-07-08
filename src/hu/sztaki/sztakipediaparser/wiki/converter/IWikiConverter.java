package hu.sztaki.sztakipediaparser.wiki.converter;

import java.util.ArrayList;


public interface IWikiConverter {
	/**
	 * Appends an external link to the output buffer.
	 * http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * 
	 * @param url URL of the link. The validity of the url is NOT checked by the parser.
	 * @param alias Display name of the url.
	 * @param plainlink Plainlink URL.
	 * @param wikitext Original raw wikitext of the tag.
	 */
	public void addExternalLinkTag(String url, String alias, boolean plainlink, String wikitext);
	
	/**
	 * Appends an internal wiki link to the output buffer. e.g. [[Texas|Lone Star State]]
	 * 
	 * @param url It is not an URL but the name of the article to link to.
	 * @param alias Alias to display in the output.
	 * @param wikitext The raw wikitext.
	 */
	public void addInternalLinkTag(String url, String alias, String wikitext);
	
	public void render(StringBuilder b);
	
	/**
	 * Adds the supplied array of CSS classes to the specified class.
	 * @param C The class to apply the CSS classes to.
	 * @param css An array of Strings describing the CSS classes.
	 */
	public void addCssClasses(Class C, ArrayList<String> css);
}
