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
package hu.sztaki.sztakipediaparser.wiki.parser;

import static org.junit.Assert.assertTrue;
import hu.sztaki.sztakipediaparser.wiki.converter.DefaultWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.converter.IWikiInterpreter;

import org.junit.Test;

/**
 * Unit tests by MediaWiki. They can be found in the
 * maintenance/tests/parser/parserTests.txt file of the mediawiki source code.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class MediawikiTests {
	private Parser parser;
	private IWikiInterpreter converter;

	/* Basic test inputs */
	private String Basic1Input = "This is a simple paragraph.";
	private String Basic2Input = "* Item 1\n* Item 2";
	private String Basic3Input = "* plain\n* plain''italic''plain\n* plain''italic''plain''italic''plain\n"
			+ "* plain'''bold'''plain\n* plain'''bold'''plain'''bold'''plain"
			+ "\n* plain''italic''plain'''bold'''plain\n* plain'''bold'''plain''italic''plain"
			+ "\n* plain''italic'''bold-italic'''italic''plain\n* plain'''bold''bold-italic''bold'''plain"
			+ "\n* plain'''''bold-italic'''italic''plain\n* plain'''''bold-italic''bold'''plain"
			+ "\n* plain''italic'''bold-italic'''''plain\n* plain'''bold''bold-italic'''''plain"
			+ "\n* plain l'''italic''plain\n* plain l''''bold''' plain";

	/* External link test inputs */
	private String ExtLink1Input = "Non-bracketed: http://example.com";
	private String ExtLink2Input = "Numbered: [http://example.com]\n"
			+ "Numbered: [http://example.net]\n"
			+ "Numbered: [http://example.com]";
	private String ExtLink3Input = "Specified text: [http://example.com link]";
	private String ExtLink4Input = "Linktrails should not work for external links: [http://example.com link]s";
	private String ExtLink5Input = "http://example.com/1$2345";
	private String ExtLink6Input = "[http://example.com/1$2345]";

	/* Basic test outputs */
	private String Basic1Output = "<p>This is a simple paragraph.</p>";
	private String Basic2Output = "<ul><li> Item 1\n</li><li> Item 2\n</li></ul>";
	private String Basic3Output = "<ul><li> plain"
			+ "</li><li> plain<i>italic</i>plain"
			+ "</li><li> plain<i>italic</i>plain<i>italic</i>plain"
			+ "</li><li> plain<b>bold</b>plain"
			+ "</li><li> plain<b>bold</b>plain<b>bold</b>plain"
			+ "</li><li> plain<i>italic</i>plain<b>bold</b>plain"
			+ "</li><li> plain<b>bold</b>plain<i>italic</i>plain"
			+ "</li><li> plain<i>italic<b>bold-italic</b>italic</i>plain"
			+ "</li><li> plain<b>bold<i>bold-italic</i>bold</b>plain"
			+ "</li><li> plain<i><b>bold-italic</b>italic</i>plain"
			+ "</li><li> plain<b><i>bold-italic</i>bold</b>plain"
			+ "</li><li> plain<i>italic<b>bold-italic</b></i>plain"
			+ "</li><li> plain<b>bold<i>bold-italic</i></b>plain"
			+ "</li><li> plain l'<i>italic</i>plain"
			+ "</li><li> plain l'<b>bold</b> plain</li></ul>";

	/* External link test outputs */
	private String ExtLink1Output = "<p>Non-bracketed: <a href=\"http://example.com\">http://example.com</a></p>";
	private String ExtLink2Output = "<p>Numbered: <a href=\"http://example.com\">[1]</a>\n"
			+ "Numbered: <a href=\"http://example.net\">[2]</a>\n"
			+ "Numbered: <a href=\"http://example.com\">[3]</a></p>";
	private String ExtLink3Output = "<p>Specified text: <a href=\"http://example.com\">link</a></p>";
	private String ExtLink4Output = "<p>Linktrails should not work for external links: <a href=\"http://example.com\">link</a>s</p>";
	private String ExtLink5Output = "<p><a href=\"http://example.com/1$2345\">http://example.com/1$2345</a></p>";
	private String ExtLink6Output = "<p><a href=\"http://example.com/1$2345\">[1]</a></p>";

	/* Basic tests */
	@Test
	public void Basic1() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Basic1Input);
		assertTrue(html.equals(Basic1Output));
	}

	@Test
	public void Basic2() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Basic2Input);
		assertTrue(html.equals(Basic2Output));
	}

	@Test
	public void Basic3() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Basic3Input);
		assertTrue(html.equals(Basic3Output));
	}

	/* External link tests */
	@Test
	public void ExtLink1() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(ExtLink1Input);
		assertTrue(html.equals(ExtLink1Output));
	}

	@Test
	public void ExtLink2() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(ExtLink2Input);
		assertTrue(html.equals(ExtLink2Output));
	}

	@Test
	public void ExtLink3() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(ExtLink3Input);
		assertTrue(html.equals(ExtLink3Output));
	}

	@Test
	public void ExtLink4() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(ExtLink4Input);
		assertTrue(html.equals(ExtLink4Output));
	}

	@Test
	public void ExtLink5() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(ExtLink5Input);
		assertTrue(html.equals(ExtLink5Output));
	}

	@Test
	public void ExtLink6() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(ExtLink6Input);
		assertTrue(html.equals(ExtLink6Output));
	}
}
