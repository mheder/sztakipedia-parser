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

import java.util.Formatter;
import java.util.regex.Pattern;

/**
 * Class to store some utility methods.
 * 
 * <p>
 * This class has some simple utility methods.
 * </p>
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class ConverterUtils {
	
	static final Pattern trimWhitespacePattern;
	
	static {
		final String whitespaceRegex = "[_ \t\r\n]";
		trimWhitespacePattern = Pattern.compile("^" + whitespaceRegex + "*+(.*?)" + whitespaceRegex + "*$");		
	}
	
	/**
	 * Trims whitespace and underscore characters from both ends of the string.
	 * 
	 * @param str
	 * @return Trimmed string.
	 */
	public static String trim(String str) {
		return trimWhitespacePattern.matcher(str).group(1);
	}

	/**
	 * Converts a hex byte array to string.
	 * 
	 * @param hash
	 * @return
	 */
	public static String byteArray2Hex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}
}
