package hu.sztaki.sztakipediaparser.wiki.tags;

public class ImageTag extends AbstractTag {

	public ImageTag() {
		super();
	}

	public ImageTag(AbstractTag parent) {
		super(parent);
	}

	@Override
	public void render(StringBuilder b) {
		// Start tag
		openTag(b);
		
		// End tag
		closeTag(b);
	}

	@Override
	protected void openTag(StringBuilder b) {
		b.append("<img");
		
		// Add attributes
		renderAttributes(b);
		
		// Add CSS classes
		renderCssClasses(b);
	}

	@Override
	protected void closeTag(StringBuilder b) {
		b.append(" />\n");
	}

}
