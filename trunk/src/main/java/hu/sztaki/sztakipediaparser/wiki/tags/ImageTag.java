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

import hu.sztaki.sztakipediaparser.wiki.visitor.TagVisitor;

import java.util.ArrayList;

/**
 * Represents an image in the AST.
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class ImageTag extends AbstractTag {
	public static int TYPE_NULL = 0;
	public static int TYPE_THUMB = 1;
	public static int TYPE_FRAME = 2;
	public static int TYPE_FRAMELESS = 3;

	public static int LOCATION_NULL = 0;
	public static int LOCATION_RIGHT = 1;
	public static int LOCATION_LEFT = 2;
	public static int LOCATION_CENTER = 3;
	public static int LOCATION_NONE = 4;

	public static int ALIGN_NULL = 0;
	public static int ALIGN_BASELINE = 1;
	public static int ALIGN_MIDDLE = 2;
	public static int ALIGN_SUB = 3;
	public static int ALIGN_SUPER = 4;
	public static int ALIGN_TEXT_TOP = 5;
	public static int ALIGN_TEXT_BOTTOM = 6;
	public static int ALIGN_TOP = 7;
	public static int ALIGN_BOTTOM = 8;

	private String caption = "";
	private String alt = "";
	private int type = TYPE_NULL;
	private boolean border = false;
	private int location = LOCATION_RIGHT;
	private int alignment = ALIGN_MIDDLE; // TODO implement alignment
	private boolean resize = false;
	private boolean upright = false;
	private float uprightFactor = 0;
	private int width = 0;
	private int height = 0;
	private boolean enableLink = true;
	private String link = "";

	public ImageTag() {
		super();
	}

	public ImageTag(Tag parent) {
		super(parent);
	}

	@Override
	public void render(StringBuilder b) {
		ArrayList<String> outerDivClasses = new ArrayList<String>();

		// Process parameters
		if (type == TYPE_THUMB) {
			outerDivClasses.add("thumbouter");
			resize = true;
		} else if (type == TYPE_FRAME) {
			resize = false;
			addClass("frameimage");
			outerDivClasses.add("frameouter");
		} else if (type == TYPE_NULL) {
			resize = false;
			border = false;
			if (alt.isEmpty()) {
				alt = caption;
			}
		} else if (type == TYPE_FRAMELESS) {
			outerDivClasses.add("framelessouter");
			resize = true;
		}

		if (border) {
			outerDivClasses.add("imageborder");
		}

		if (!alt.isEmpty()) {
			addAttribute("alt", alt);
		}

		if (location == LOCATION_RIGHT) {
			outerDivClasses.add("imageright");
		} else if (location == LOCATION_LEFT) {
			outerDivClasses.add("imageleft");
		} else if (location == LOCATION_CENTER) {
			outerDivClasses.add("imagecenter");
		} else if (location == LOCATION_NONE) {
			outerDivClasses.add("imagenone");
		}

		// TODO Implement vertical alignment

		if (resize) {
			if (upright && (type == TYPE_THUMB || type == TYPE_FRAMELESS)) {
				if (uprightFactor > 0) {
					addAttribute("width", (uprightFactor * 100) + "%");
					// addAttribute("height", (uprightFactor * 100) + "%");
				} else {
					addAttribute("width", "75%");
					// addAttribute("height", "75%");
				}
			} else if (!upright
					&& (type == TYPE_THUMB || type == TYPE_FRAMELESS)) {
				if (width != 0) {
					addAttribute("width", String.valueOf(width));
				}

				if (height != 0) {
					addAttribute("height", String.valueOf(height));
				}

				if (width == 0 && height == 0) {
					if (type == TYPE_THUMB) {
						addAttribute("width", "220");
						addClass("thumbimage");
					} else {
						addAttribute("width", "220");
						addClass("framelessimage");
					}
				}
			}
		}

		b.append("<div wikitype=\"image\" wikitext=\"" + wikitext + "\"");
		if (!outerDivClasses.isEmpty()) {
			b.append(" class=\"");
			for (String c : outerDivClasses) {
				b.append(c + " ");
			}
			b.append("\"");
		}
		/*
		 * if (begin != null) { b.append(" wikitext-begin=\"" +
		 * String.valueOf(begin) + "\""); }
		 * 
		 * if (end != null) { b.append(" wikitext-end=\"" + String.valueOf(end)
		 * + "\""); }
		 */

		b.append(">");

		if (enableLink) {
			b.append("<a class=\"imagelink\" href=\"" + link + "\">");
		}

		// Start tag
		openTag(b);

		// End tag
		closeTag(b);

		if (enableLink) {
			b.append("</a>");
		}

		if ((type == TYPE_THUMB || type == TYPE_FRAME) && !caption.isEmpty()) {
			b.append("<div wikitype=\"image-caption\" class=\"imagecaption\">");
			renderChildren(b);
			b.append("</div>");
		}

		b.append("</div>");
	}

	@Override
	public void openTag(StringBuilder b) {
		b.append("<img");

		// Add attributes
		renderAttributes(b);

		// Add CSS classes
		renderCssClasses(b);
	}

	@Override
	public void closeTag(StringBuilder b) {
		b.append(" />");
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public void setResize(boolean resize) {
		this.resize = resize;
	}

	public void setUpright(boolean upright) {
		this.upright = upright;
		this.resize = true;
	}

	public void setUprightFactor(float uprightFactor) {
		this.uprightFactor = uprightFactor;
		this.upright = true;
		this.resize = true;
	}

	public void setWidth(int width) {
		this.width = width;
		this.resize = true;
	}

	public void setHeight(int height) {
		this.height = height;
		this.resize = true;
	}

	public void setEnableLink(boolean enableLink) {
		this.enableLink = enableLink;
	}

	public void setLink(String link) {
		this.link = link;
		this.enableLink = true;
	}
	
    public void accept(TagVisitor visitor) {
        visitor.visit(this);
    }	

}
