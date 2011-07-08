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
package hu.sztaki.sztakipediaparser.wiki.tags;

/**
 * A class for storing template parameters.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class TemplateParam {
	private String name = "";

	private String value = "";

	/**
	 * Implicit template parameters don't have names in the original wikitext
	 * template. They are referenced only by their number in the source code of
	 * the template, ie. {{{1}}}. The name string stores this implicit number.
	 */
	private boolean implicit = false;

	public TemplateParam(String name, String value, boolean implicit) {
		this.name = name;
		this.value = value;
		this.implicit = implicit;
	}

	public void render(StringBuilder b) {
		b.append("<div class=\"wiki-template-param\">");
		b.append("<span class=\"wiki-template-param-key");
		if (implicit) {
			b.append(" implicit");
		}
		b.append("\">" + name + "</span>");
		b.append("<span class=\"wiki-template-param-value\">" + value
				+ "</span>");
		b.append("</div>");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isImplicit() {
		return implicit;
	}

	public void setImplicit(boolean implicit) {
		this.implicit = implicit;
	}
}
