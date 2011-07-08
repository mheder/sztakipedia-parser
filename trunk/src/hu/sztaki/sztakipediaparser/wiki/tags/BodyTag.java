package hu.sztaki.sztakipediaparser.wiki.tags;

import java.util.Locale;

public class BodyTag extends AbstractTag {

	public BodyTag() {
		super();
	}

	public BodyTag(Locale locale) {
		super(locale);
	}

	// These are overridden to prevent the setting of parent node since the BodyTag is always a root node.
	private BodyTag(AbstractTag parent) {}
	private BodyTag(AbstractTag parent, Locale locale) {}

	@Override
	public void render(StringBuilder b) {
		for(AbstractTag c : children) {
			c.render(b);
		}
	}

}
