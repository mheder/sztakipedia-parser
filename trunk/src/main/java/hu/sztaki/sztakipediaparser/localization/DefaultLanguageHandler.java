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
package hu.sztaki.sztakipediaparser.localization;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class to manage localization.
 * 
 * <p>
 * This class manages localization. Stores the current locale settings and a
 * ResourceBundle object. Provides static methods to set the current locale and
 * get language specific data by keywords.
 * </p>
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class DefaultLanguageHandler {
	// Keys
	public static String WIKI_LINK_FILE = "wiki.link.file";
	public static String WIKI_LINK_IMAGE = "wiki.link.image";
	public static String WIKI_LINK_SPECIAL = "wiki.link.special";
	public static String WIKI_LINK_MEDIA = "wiki.link.media";

	public static String WIKI_IMAGE_LINK = "wiki.image.link";
	public static String WIKI_IMAGE_ALT = "wiki.image.alt";
	public static String WIKI_IMAGE_FRAME = "wiki.image.frame";
	public static String WIKI_IMAGE_THUMB = "wiki.image.thumb";
	public static String WIKI_IMAGE_THUMBNAIL = "wiki.image.thumbnail";
	public static String WIKI_IMAGE_BORDER = "wiki.image.border";
	public static String WIKI_IMAGE_RIGHT = "wiki.image.right";
	public static String WIKI_IMAGE_LEFT = "wiki.image.left";
	public static String WIKI_IMAGE_CENTER = "wiki.image.center";
	public static String WIKI_IMAGE_NONE = "wiki.image.none";
	public static String WIKI_IMAGE_PX = "wiki.image.px";

	private static String baseName = "Wikiwords";
	private static Locale locale = new Locale("en");
	private static ResourceBundle bundle;

	static {
		// Create new ResourceBundle object.
		ResourceBundle.clearCache();
		bundle = ResourceBundle.getBundle(baseName, locale);
	}

	/**
	 * Get language specific data from the currently set Wikiwords file.
	 * 
	 * @param key
	 *            One of the static keys defined in this class.
	 * @return Language specific data.
	 * @throws MissingResourceException
	 */
	public static String getWord(String key) throws MissingResourceException {
		return bundle.getString(key);
	}

	/**
	 * Set the current locale.
	 * 
	 * @param l
	 */
	public static void setLocale(Locale l) {
		if (l != null) {
			locale = (Locale) l.clone();
			bundle = ResourceBundle.getBundle(baseName, locale);
		}
	}

}
