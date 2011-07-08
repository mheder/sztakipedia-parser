package hu.sztaki.sztakipediaparser.wiki.tags;

import java.util.Locale;

public class AnchorTag extends AbstractTag {
	/**************/
	/*** Fields ***/
	/**************/
	
	/**
	 * True for plainlinks: http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs (External links section)
	 * e.g. <span class="plainlinks"> [http://www.sysinternals.com/ntw2k/freeware/winobj.shtml WinObj] </span>
	 */
	private boolean plainlink = false;
	
	/********************/
	/*** Constructors ***/
	/********************/
	
	public AnchorTag() {
		super();
	}
	
	public AnchorTag(Locale locale) {
		super(locale);
	}
	
	public AnchorTag(AbstractTag parent) {
		super(parent);
	}
	
	public AnchorTag(AbstractTag parent, Locale locale) {
		super(parent, locale);
	}

	/***************/
	/*** Methods ***/
	/***************/
	
	@Override
	public void render(StringBuilder b) {
		// Handle plainlinks
		if(plainlink) {
			b.append("<span class=\"plainlinks\">\n");
		}
		
		// Start tag
		b.append("<a");
		
		// Add attributes
		if(!attributes.isEmpty()) {
			for(String name : attributes.keySet()) {
				b.append(" " + name + "=\"" + attributes.get(name) + "\"");
			}
		}
		
		// Add CSS classes
		if(!classes.isEmpty()) {
			b.append(" class=\"");
			for(String name : classes) {
				if(classes.indexOf(name) == classes.size() - 1) {
					b.append(name);
				} else {
					b.append(name + " ");
				}
			}
			b.append("\"");
		}
		b.append(">");
		
		// Render children
		for(AbstractTag c : children) {
			c.render(b);
		}
		
		// End tag
		b.append("</a>\n");
		
		// Handle plainlinks
		if(plainlink) {
			b.append("</span>\n");
		}
	}

	/***************************/
	/*** Getters and Setters ***/
	/***************************/
	
	public boolean isPlainlink() {
		return plainlink;
	}
	
	public void setPlainlink(boolean plainlink) {
		this.plainlink = plainlink;
	}
}
