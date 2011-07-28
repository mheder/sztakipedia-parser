/*
 * Sztakipedia parser - http://http://code.google.com/p/sztakipedia-parser
 *
 * Copyright (C) 2011 MTA SZTAKI 
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
package hu.sztaki.sztakipediaparser.wiki.visitor.html;

import hu.sztaki.sztakipediaparser.wiki.tags.AnchorTag;
import hu.sztaki.sztakipediaparser.wiki.tags.BodyTag;
import hu.sztaki.sztakipediaparser.wiki.tags.BoldTag;
import hu.sztaki.sztakipediaparser.wiki.tags.DivTag;
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
import hu.sztaki.sztakipediaparser.wiki.tags.TemplateParam;
import hu.sztaki.sztakipediaparser.wiki.tags.TemplateTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TextTag;
import hu.sztaki.sztakipediaparser.wiki.tags.UnorderedListItemTag;
import hu.sztaki.sztakipediaparser.wiki.tags.UnorderedListTag;
import hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor;

import java.util.ArrayList;

public class HTMLVisitor implements TagVisitor {

	final StringBuilder out;

	public HTMLVisitor() {
		out = new StringBuilder();
	}

	public String getHTML() {
		return out.toString();
	}

	private void visitChildren(Tag tag) {
		if (tag.getChildren() != null)
			for (Tag t : tag.getChildren())
				t.accept(this);
	}

	public void visit(AnchorTag tag) {
		// Handle plainlinks
		if (tag.isPlainlink()) {
			out.append("<span class=\"plainlinks\">");
		}

		out.append("<a");
		// Add attributes
		tag.renderAttributes(out);

		// Add CSS classes
		tag.renderCssClasses(out);
		out.append(">");

		// Render children
		visitChildren(tag);

		// Close tag
		out.append("</a>");

		// Handle plainlinks
		if (tag.isPlainlink()) {
			out.append("</span>");
		}
	}

	public void visit(BodyTag tag) {
		visitChildren(tag);
	}

	public void visit(BoldTag tag) {
		out.append("<b>");
		visitChildren(tag);
		out.append("</b>");
	}

	public void visit(DivTag tag) {
		out.append("<div");
		// Add attributes
		tag.renderAttributes(out);

		// Add CSS classes
		tag.renderCssClasses(out);
		out.append(">");

		visitChildren(tag);

		// Close tag
		out.append("</div>");
	}

	public void visit(HeadingTag tag) {
		// Set CSS class
		switch (tag.getLevel()) {
		case 1:
			tag.addClass("section-heading");
			break;
		case 2:
			tag.addClass("subsection-heading");
			break;
		case 3:
			tag.addClass("sub-subsection-heading");
			break;
		default:
			tag.addClass("sub-subsection-heading");
		}

		out.append("<div");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		out.append(">");

		visitChildren(tag);

		// Close tag
		out.append("</div>");
	}

	public void visit(HRTag tag) {
		out.append("<hr");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		out.append(" />");
	}

	public void visit(ImageTag tag) {
		ArrayList<String> outerDivClasses = new ArrayList<String>();

		// Process parameters
		if (tag.getType() == ImageTag.TYPE_THUMB) {
			outerDivClasses.add("thumbouter");
			tag.setResize(true);
		} else if (tag.getType() == ImageTag.TYPE_FRAME) {
			tag.setResize(false);
			tag.addClass("frameimage");
			outerDivClasses.add("frameouter");
		} else if (tag.getType() == ImageTag.TYPE_NULL) {
			tag.setResize(false);
			tag.setBorder(false);
			if (tag.getAlt().isEmpty()) {
				tag.setAlt(tag.getCaption());
			}
		} else if (tag.getType() == ImageTag.TYPE_FRAMELESS) {
			outerDivClasses.add("framelessouter");
			tag.setResize(true);
		}

		if (tag.getBorder()) {
			outerDivClasses.add("imageborder");
		}

		if (!tag.getAlt().isEmpty()) {
			tag.addAttribute("alt", tag.getAlt());
		}

		if (tag.getLocation() == ImageTag.LOCATION_RIGHT) {
			outerDivClasses.add("imageright");
		} else if (tag.getLocation() == ImageTag.LOCATION_LEFT) {
			outerDivClasses.add("imageleft");
		} else if (tag.getLocation() == ImageTag.LOCATION_CENTER) {
			outerDivClasses.add("imagecenter");
		} else if (tag.getLocation() == ImageTag.LOCATION_NONE) {
			outerDivClasses.add("imagenone");
		}

		// TODO Implement vertical alignment

		if (tag.getResize()) {
			if (tag.getUpright()
					&& (tag.getType() == ImageTag.TYPE_THUMB || tag.getType() == ImageTag.TYPE_FRAMELESS)) {
				if (tag.getUprightFactor() > 0) {
					tag.addAttribute("width", (tag.getUprightFactor() * 100)
							+ "%");
					// addAttribute("height", (uprightFactor * 100) + "%");
				} else {
					tag.addAttribute("width", "75%");
					// addAttribute("height", "75%");
				}
			} else if (!tag.getUpright()
					&& (tag.getType() == ImageTag.TYPE_THUMB || tag.getType() == ImageTag.TYPE_FRAMELESS)) {
				if (tag.getWidth() != 0) {
					tag.addAttribute("width", String.valueOf(tag.getWidth()));
				}

				if (tag.getHeight() != 0) {
					tag.addAttribute("height", String.valueOf(tag.getHeight()));
				}

				if (tag.getWidth() == 0 && tag.getHeight() == 0) {
					if (tag.getType() == ImageTag.TYPE_THUMB) {
						tag.addAttribute("width", "220");
						tag.addClass("thumbimage");
					} else {
						tag.addAttribute("width", "220");
						tag.addClass("framelessimage");
					}
				}
			}
		}

		// Render outer container div
		out.append("<div wikitype=\"image\"");
		if (!outerDivClasses.isEmpty()) {
			out.append(" class=\"");
			for (String c : outerDivClasses) {
				out.append(c + " ");
			}
			out.append("\"");
		}
		out.append(">");

		// Render link to the image's info page
		if (tag.getEnableLink()) {
			out
					.append("<a class=\"imagelink\" href=\"" + tag.getLink()
							+ "\">");
		}

		// Open image tag
		out.append("<img");

		// Add attributes
		tag.renderAttributes(out);

		// Add CSS classes
		tag.renderCssClasses(out);

		// Close image tag
		out.append(" />");

		// Close info page link
		if (tag.getEnableLink()) {
			out.append("</a>");
		}

		// Render caption
		if ((tag.getType() == ImageTag.TYPE_THUMB || tag.getType() == ImageTag.TYPE_FRAME)
				&& !tag.getCaption().isEmpty()) {
			out
					.append("<div wikitype=\"image-caption\" class=\"imagecaption\">");
			visitChildren(tag);
			out.append("</div>");
		}

		// Close outer container div
		out.append("</div>");
	}

	public void visit(IndentTag tag) {
		out.append("\n<br/>");
		switch (tag.getLevel()) {
		case 1:
			for (int i = 0; i < IndentTag.LEVEL1; i++) {
				out.append("&nbsp;");
			}
			break;
		case 2:
			for (int i = 0; i < IndentTag.LEVEL2; i++) {
				out.append("&nbsp;");
			}
			break;
		case 3:
			for (int i = 0; i < IndentTag.LEVEL3; i++) {
				out.append("&nbsp;");
			}
			break;
		default:
			for (int i = 0; i < IndentTag.LEVEL1; i++) {
				out.append("&nbsp;");
			}
			break;
		}
	}

	public void visit(ItalicTag tag) {
		out.append("<i>");
		visitChildren(tag);
		out.append("</i>");
	}

	public void visit(ParagraphTag tag) {
		// FIXME Doesn't work yet.
		out.append("<p");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		out.append(">");
		visitChildren(tag);
		out.append("</p>");
	}

	public void visit(RawWikiTag tag) {
		tag.addBeginEndAttr();
		out.append("<span class=\"raw-wikitext\"");
		tag.renderAttributes(out);
		out.append(">");
		out.append(tag.getWikitext());
		out.append("</span>");
	}

	public void visit(ReferenceTag tag) {
		// FIXME Doesn't work yet.
		visitChildren(tag);
	}

	public void visit(TextTag tag) {
		tag.addBeginEndAttr();
		if (!tag.getAttributes().isEmpty()) {
			out.append("<span");
			tag.renderAttributes(out);
			out.append(">");
		}

		// Append content to the output
		out.append(tag.getContent());

		if (!tag.getAttributes().isEmpty()) {
			out.append("</span>");
		}

		visitChildren(tag);
	}

	public void visit(TableCaptionTag tag) {
		out.append("<caption");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		out.append(">");
		visitChildren(tag);
		out.append("</caption>");
	}

	public void visit(TableCellTag tag) {
		out.append("<td");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		if (tag.getParams() != null && !tag.getParams().isEmpty()) {
			out.append(" " + tag.getParams());
		}
		out.append(">");
		visitChildren(tag);
		out.append("</td>");
	}

	public void visit(TableColHeadingTag tag) {
		out.append("<th");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		if (tag.getParams() != null && !tag.getParams().isEmpty()) {
			out.append(" " + tag.getParams());
		}
		out.append(">");
		visitChildren(tag);
		out.append("</th>");
	}

	public void visit(TableRowHeadingTag tag) {
		out.append("<th scope=\"row\"");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		if (tag.getParams() != null && !tag.getParams().isEmpty()) {
			out.append(" " + tag.getParams());
		}
		out.append(">");
		visitChildren(tag);
		out.append("</th>");
	}

	public void visit(TableRowTag tag) {
		out.append("<tr");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		if (tag.getParams() != null && !tag.getParams().isEmpty()) {
			out.append(" " + tag.getParams());
		}
		out.append(">");
		visitChildren(tag);
		out.append("</tr>");
	}

	public void visit(TableTag tag) {
		out.append("<table");
		tag.renderCssClasses(out);
		tag.renderAttributes(out);
		if (tag.getParams() != null && !tag.getParams().isEmpty()) {
			out.append(" " + tag.getParams());
		}
		out.append(">");

		// Render caption
		if (tag.getCaption() != null) {
			visit(tag.getCaption());
		}

		out.append("<tbody>");
		visitChildren(tag);
		out.append("</tbody>");
		out.append("</table>");
	}

	public void visit(TemplateParam tag) {
		out.append("<div class=\"wiki-template-param\">");
		out.append("<span class=\"wiki-template-param-key");
		if (tag.isImplicit()) {
			out.append(" implicit");
		}
		out.append("\">" + tag.getName() + "</span>");
		out.append("<span class=\"wiki-template-param-value\">"
				+ tag.getValue() + "</span>");
		out.append("</div>");
	}

	public void visit(TemplateTag tag) {
		// Open template tag
		out.append("<div onclick=\"edit_wiki_template(this)\" "
				+ "onmouseover=\"show_wiki_template(this)\"");
		tag.renderAttributes(out);
		tag.renderCssClasses(out);
		out.append(">");
		out.append("<span class=\"wiki-template-title\">" + tag.getName()
				+ "</span>");

		// Render template parameters
		out.append("<div class=\"wiki-template-params\">");
		for (TemplateParam param : tag.getParameters()) {
			visit(param);
		}
		out.append("</div>");

		// Close template tag
		out.append("</div>");
	}

	public void visit(UnorderedListItemTag tag) {
		out.append("<li");
		tag.renderAttributes(out);
		tag.renderCssClasses(out);
		out.append(">");
		visitChildren(tag);
		out.append("</li>");
	}

	public void visit(UnorderedListTag tag) {
		// TODO Set attributes and css classes of <li> tag.
		if (tag.getWrap()) {
			out.append("<li>");
		}

		out.append("<ul");
		tag.renderAttributes(out);
		tag.renderCssClasses(out);
		out.append(">");
		visitChildren(tag);
		out.append("</ul>");

		if (tag.getWrap()) {
			out.append("</li>");
		}
	}

}
