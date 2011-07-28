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
import hu.sztaki.sztakipediaparser.wiki.tags.AnchorTag;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Simple unit tests for the parser.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class ParserTest {
	private Parser parser;
	private IWikiInterpreter converter;

	// Input Fixtures
	private String Test1Input = "[http://www.sztaki.hu SZTAKI]";
	private String Test2Input = "[http://www.google.com]"
			+ "[http://www.sztaki.hu SZTAKI]"
			+ "[www.facebook.com]"
			+ "[www.facebook.com ''facebook'' '''facebook''' '''''facebook''''']";
	private String Test3Input = "[[Texas]]";
	private String Test4Input = "[[Texas state|Lone Star State]]";
	private String Test5Input = "''italic'', '''bold''', '''''bolditalic'''''";
	private String Test6Input = "[[Texas|''Lone'' '''Star''' '''''State''''']]";
	private String Test7Input = "[[:File:Example.svg|Caption with a [[Texas|'''bold''' and ''italic'' '''''link''''']].]]";
	private String Test8Input = "\n==Section ---- heading==\n===Subsection heading===\n"
			+ "====Sub-subsection heading====\n";
	private String Test9Input = "\n==Section with [[Link|Hyperlink]] and [www.facebook.com external link]==\n";
	private String Test10Input = "This is above...\n----\n    and this is below.";
	private String Test11Input = "[[Texas";
	private String Test12Input = "{{template    Name|par1  =  value1   |value2  |\n\n   value3|[[:File:Example.svg|Caption with a [[Texas|'''bold''' and ''italic'' '''''link''''']].]]|par5=value5 and 6|par10=value10}}";
	private String Test13Input = "blablabla\n" + ":indent level1\n"
			+ "::indent level2\n" + ":::indent level3\n" + "::::indent level3";
	private String Test14Input = "\n*list1\n*still 1\n**now [[Two|2 (number)]]\n***threee\n**two again [www.facebook.com facebook home]\n"
			+ "***and three ---- again [[Texas]] ====heading====\n";
	private String Test15Input = "\n==Section heading==\n===Followed by a subsection heading===\n";
	private String Test16Input = "{{template1|par1={{template2|par2=val2}}|val2}}";
	private String Test17Input = "{| class=\"wikitable\"" + "\n|-"
			+ "\n! Header 1" + "\n! Header 2" + "\n! Header 3" + "\n|-"
			+ "\n|row 1, cell 1" + "\n|row 1, cell 2" + "\n|row 1, cell 3"
			+ "\n|-" + "\n|row 2, cell 1" + "\n|row 2, cell 2"
			+ "\n|row 2, cell 3" + "\n|-" + "\n|row 3, cell 1"
			+ "\n|row 3, cell 2" + "\n|row 3, cell 3" + "\n|}";
	private String Test18Input = "{|" + "\n|+ The table's caption" + "\n|-"
			+ "\n|Cell 1||Cell 2||Cell 3" + "\n|-" + "\n|Cell A" + "\n|Cell B"
			+ "\n|Cell C" + "\n|}";
	private String Test19Input = "<ref name=\"ref\">{{cite web |url=http://lynx.net |title=lynx}}</ref>";
	private String Test20Input = "http://facebook.com";
	private String Test21Input = "www.facebook.com";
	private String Test22Input = "https://www.facebook.com";

	// Expected Output
	private String Test1Output = "<a href=\"http://www.sztaki.hu\" class=\"external autonumber\">SZTAKI</a>";
	private String Test2Output = "<a href=\"http://www.google.com\">[1]</a>"
			+ "<a href=\"http://www.sztaki.hu\">SZTAKI</a>"
			+ "<a href=\"www.facebook.com\">[2]</a>"
			+ "<a href=\"www.facebook.com\"><i>facebook</i> <b>facebook</b> <b><i>facebook</i></b></a>";
	private String Test3Output = "<a title=\"Texas\" href=\"http://en.wikipedia.org/wiki/Texas\" class=\"external autonumber\">Texas</a>";
	private String Test4Output = "<a title=\"Texas state\" href=\"http://en.wikipedia.org/wiki/Texas state\" class=\"different\">Lone Star State</a>";
	private String Test5Output = "<i>italic</i>, <b>bold</b>, <b><i>bolditalic</i></b>";
	private String Test6Output = "<a title=\"Texas\" href=\"http://en.wikipedia.org/wiki/Texas\" class=\"different\"><i>Lone</i> <b>Star</b> <b><i>State</i></b></a>";
	private String Test7Output = "<a href=\"http://en.wikipedia.org/wiki/File:Example.svg\">Caption with a "
			+ "<a title=\"Texas\" href=\"http://en.wikipedia.org/wiki/Texas\" class=\"different\"><b>bold</b> and <i>italic</i> <b><i>link</i></b></a>.</a>";
	private String Test8Output = "<div class=\"section-heading\">Section ---- heading</div>\n"
			+ "<div class=\"subsection-heading\">Subsection heading</div>\n"
			+ "<div class=\"sub-subsection-heading\">Sub-subsection heading</div>";
	private String Test9Output = "<div class=\"section-heading\">Section with "
			+ "<a title=\"Link\" href=\"http://en.wikipedia.org/wiki/Link\" class=\"different\">Hyperlink</a> and "
			+ "<a href=\"www.facebook.com\">external link</a></div>";
	private String Test10Output = "This is above...<hr />    and this is below.";
	private String Test11Output = "<span class=\"raw-wikitext\">[[Texas</span>";
	private String Test12Output = "<div onclick=\"edit_wiki_template(this)\" onmouseover=\"show_wiki_template(this)\" class=\"wiki-template multiline\">"
			+ "<span class=\"wiki-template-title\">template Name</span>"
			+ "<div class=\"wiki-template-params\">"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key\">par1</span>"
			+ "<span class=\"wiki-template-param-value\">value1</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">1</span>"
			+ "<span class=\"wiki-template-param-value\">value2</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">2</span>"
			+ "<span class=\"wiki-template-param-value\">value3</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">3</span>"
			+ "<span class=\"wiki-template-param-value\">"
			+ Test7Output
			+ "</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key\">par5</span>"
			+ "<span class=\"wiki-template-param-value\">value5 and 6</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key\">par10</span>"
			+ "<span class=\"wiki-template-param-value\">value10</span>"
			+ "</div>" + "</div>" + "</div>";
	private String Test13Output = "blablabla"
			+ "\n<br/>&nbsp;&nbsp;&nbsp;indent level1"
			+ "\n<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;indent level2"
			+ "\n<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;indent level3"
			+ "\n<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:indent level3";
	private String Test14Output = "<ul>"
			+ "<li>list1</li>"
			+ "<li>still 1</li>"
			+ "<li><ul>"
			+ "<li>now <a title=\"Two\" href=\"http://en.wikipedia.org/wiki/Two\" class=\"different\">2 (number)</a>"
			+ "</li>"
			+ "<li><ul>"
			+ "<li>threee</li>"
			+ "</ul>"
			+ "</li>"
			+ "<li>two again <a href=\"www.facebook.com\">facebook home</a>"
			+ "</li>"
			+ "<li><ul>"
			+ "<li>and three ---- again <a title=\"Texas\" href=\"http://en.wikipedia.org/wiki/Texas\">Texas</a>"
			+ " ====heading====</li>" + "</ul>" + "</li>" + "</ul>" + "</li>"
			+ "</ul>";
	private String Test15Output = "<div class=\"section-heading\">Section heading</div>\n"
			+ "<div class=\"subsection-heading\">Followed by a subsection heading</div>";
	private String Test16Output = "<div onclick=\"edit_wiki_template(this)\" onmouseover=\"show_wiki_template(this)\" class=\"wiki-template inline\">"
			+ "<span class=\"wiki-template-title\">template1</span>"
			+ "<div class=\"wiki-template-params\">"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key\">par1</span>"
			+ "<span class=\"wiki-template-param-value\">"
			+ "<div onclick=\"edit_wiki_template(this)\" onmouseover=\"show_wiki_template(this)\" class=\"wiki-template inline\">"
			+ "<span class=\"wiki-template-title\">template2</span>"
			+ "<div class=\"wiki-template-params\">"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key\">par2</span>"
			+ "<span class=\"wiki-template-param-value\">val2</span>"
			+ "</div>"
			+ "</div>"
			+ "</div>"
			+ "</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">1</span>"
			+ "<span class=\"wiki-template-param-value\">val2</span>"
			+ "</div>" + "</div>" + "</div>";
	private String Test17Output = "<table class=\"wikitable\">" + "<tbody><tr>"
			+ "<th>Header 1</th>" + "<th>Header 2</th>" + "<th>Header 3</th>"
			+ "</tr>" + "<tr>" + "<td>row 1, cell 1</td>"
			+ "<td>row 1, cell 2</td>" + "<td>row 1, cell 3</td>" + "</tr>"
			+ "<tr>" + "<td>row 2, cell 1</td>" + "<td>row 2, cell 2</td>"
			+ "<td>row 2, cell 3</td>" + "</tr>" + "<tr>"
			+ "<td>row 3, cell 1</td>" + "<td>row 3, cell 2</td>"
			+ "<td>row 3, cell 3</td>" + "</tr>" + "</tbody></table>";
	private String Test18Output = "<table>" + "<caption>"
			+ "The table's caption</caption><tbody>" + "<tr>"
			+ "<td>Cell 1</td>" + "<td>Cell 2</td>" + "<td>Cell 3</td>"
			+ "</tr>" + "<tr>" + "<td>Cell A</td>" + "<td>Cell B</td>"
			+ "<td>Cell C</td>" + "</tr>" + "</tbody></table>";
	private String Test19Output = "<div onclick=\"edit_wiki_template(this)\" onmouseover=\"show_wiki_template(this)\" class=\"wiki-template inline\">"
			+ "<span class=\"wiki-template-title\">cite web</span>"
			+ "<div class=\"wiki-template-params\">"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key\">url</span>"
			+ "<span class=\"wiki-template-param-value\">http://lynx.net</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key\">title</span>"
			+ "<span class=\"wiki-template-param-value\">lynx</span>"
			+ "</div>" + "</div>" + "</div>";
	private String Test20Output = "<div onclick=\"edit_wiki_template(this)\" onmouseover=\"show_wiki_template(this)\" class=\"wiki-template inline\">"
			+ "<span class=\"wiki-template-title\">URL</span>"
			+ "<div class=\"wiki-template-params\">"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">1</span>"
			+ "<span class=\"wiki-template-param-value\">http://facebook.com</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">2</span>"
			+ "<span class=\"wiki-template-param-value\"></span>"
			+ "</div>"
			+ "</div>" + "</div>";
	private String Test21Output = "<div onclick=\"edit_wiki_template(this)\" onmouseover=\"show_wiki_template(this)\" class=\"wiki-template inline\">"
			+ "<span class=\"wiki-template-title\">URL</span>"
			+ "<div class=\"wiki-template-params\">"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">1</span>"
			+ "<span class=\"wiki-template-param-value\">www.facebook.com</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">2</span>"
			+ "<span class=\"wiki-template-param-value\"></span>"
			+ "</div>"
			+ "</div>" + "</div>";
	private String Test22Output = "<div onclick=\"edit_wiki_template(this)\" onmouseover=\"show_wiki_template(this)\" class=\"wiki-template inline\">"
			+ "<span class=\"wiki-template-title\">URL</span>"
			+ "<div class=\"wiki-template-params\">"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">1</span>"
			+ "<span class=\"wiki-template-param-value\">https://www.facebook.com</span>"
			+ "</div>"
			+ "<div class=\"wiki-template-param\">"
			+ "<span class=\"wiki-template-param-key implicit\">2</span>"
			+ "<span class=\"wiki-template-param-value\"></span>"
			+ "</div>"
			+ "</div>" + "</div>";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void Test1() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		// Setup converter
		ArrayList<String> classes = new ArrayList<String>();
		classes.add("external");
		classes.add("autonumber");
		converter.addCssClasses(AnchorTag.class, classes);

		String html = parser.parse(Test1Input);
		assertTrue(html.equals(Test1Output));
	}

	@Test
	public void Test2() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test2Input);
		assertTrue(html.equals(Test2Output));
	}

	@Test
	public void Test3() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		// Setup converter
		ArrayList<String> classes = new ArrayList<String>();
		classes.add("external");
		classes.add("autonumber");
		converter.addCssClasses(AnchorTag.class, classes);

		String html = parser.parse(Test3Input);
		assertTrue(html.equals(Test3Output));
	}

	@Test
	public void Test4() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test4Input);
		assertTrue(html.equals(Test4Output));
	}

	@Test
	public void Test5() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test5Input);
		assertTrue(html.equals(Test5Output));
	}

	@Test
	public void Test6() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test6Input);
		assertTrue(html.equals(Test6Output));
	}

	@Test
	public void Test7() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test7Input);
		assertTrue(html.equals(Test7Output));
	}

	@Test
	public void Test8() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test8Input);
		assertTrue(html.equals(Test8Output));
	}

	@Test
	public void Test9() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test9Input);
		assertTrue(html.equals(Test9Output));
	}

	@Test
	public void Test10() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test10Input);
		assertTrue(html.equals(Test10Output));
	}

	@Test
	public void Test11() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test11Input);
		assertTrue(html.equals(Test11Output));
	}

	@Test
	public void Test12() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test12Input);
		assertTrue(html.equals(Test12Output));
	}

	@Test
	public void Test13() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test13Input);
		assertTrue(html.equals(Test13Output));
	}

	@Test
	public void Test14() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test14Input);
		assertTrue(html.equals(Test14Output));
	}

	@Test
	public void Test15() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test15Input);
		assertTrue(html.equals(Test15Output));
	}

	@Test
	public void Test16() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test16Input);
		assertTrue(html.equals(Test16Output));
	}

	@Test
	public void Test17() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test17Input);
		assertTrue(html.equals(Test17Output));
	}

	@Test
	public void Test18() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test18Input);
		assertTrue(html.equals(Test18Output));
	}

	@Test
	public void Test19() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test19Input);
		assertTrue(html.equals(Test19Output));
	}

	@Test
	public void Test20() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test20Input);
		assertTrue(html.equals(Test20Output));
	}

	@Test
	public void Test21() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test21Input);
		assertTrue(html.equals(Test21Output));
	}

	@Test
	public void Test22() throws Exception {
		converter = new DefaultWikiInterpreter();
		parser = new Parser(converter);

		String html = parser.parse(Test22Input);
		assertTrue(html.equals(Test22Output));
	}
}
