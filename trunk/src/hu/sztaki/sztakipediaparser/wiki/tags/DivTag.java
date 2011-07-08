package hu.sztaki.sztakipediaparser.wiki.tags;

public class DivTag extends AbstractTag {

	public DivTag() {
		super();
	}

	public DivTag(AbstractTag parent) {
		super(parent);
	}

	@Override
	public void render(StringBuilder b) {
		// Start tag
		openTag(b);
		
		// Render children
		renderChildren(b);
		
		// End tag
		closeTag(b);
	}

	@Override
	protected void openTag(StringBuilder b) {
		b.append("<div");
		
		// Add attributes
		renderAttributes(b);
		
		// Add CSS classes
		renderCssClasses(b);
		
		b.append(">");
	}

	@Override
	protected void closeTag(StringBuilder b) {
		b.append("</div>\n");
	}

}
