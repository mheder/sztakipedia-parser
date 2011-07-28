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

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCaption() {
		return caption;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getAlt() {
		return alt;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public boolean getBorder() {
		return border;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getLocation() {
		return location;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public void setResize(boolean resize) {
		this.resize = resize;
	}

	public boolean getResize() {
		return resize;
	}

	public void setUpright(boolean upright) {
		this.upright = upright;
		this.resize = true;
	}

	public boolean getUpright() {
		return upright;
	}

	public void setUprightFactor(float uprightFactor) {
		this.uprightFactor = uprightFactor;
		this.upright = true;
		this.resize = true;
	}

	public float getUprightFactor() {
		return uprightFactor;
	}

	public void setWidth(int width) {
		this.width = width;
		this.resize = true;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
		this.resize = true;
	}

	public int getHeight() {
		return height;
	}

	public void setEnableLink(boolean enableLink) {
		this.enableLink = enableLink;
	}

	public boolean getEnableLink() {
		return enableLink;
	}

	public void setLink(String link) {
		this.link = link;
		this.enableLink = true;
	}

	public String getLink() {
		return link;
	}

	public void accept(TagVisitor visitor) {
		visitor.visit(this);
	}

}
