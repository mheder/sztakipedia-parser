package hu.sztaki.sztakipediaparser.wiki.converter;

import hu.sztaki.sztakipediaparser.wiki.tags.Tag;
import hu.sztaki.sztakipediaparser.wiki.visitor.html.PlainTextContentWriter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class PlainWikiInterpreter extends DefaultWikiInterpreter {

	public PlainWikiInterpreter() throws MalformedURLException, IOException,
			NoSuchAlgorithmException {
		super();
	}

	public PlainWikiInterpreter(Locale locale) throws MalformedURLException,
			IOException, NoSuchAlgorithmException {
		super(locale);
	}

	public PlainWikiInterpreter(Locale locale, String rootURL, String apiURL,
			String mediaUrl) throws MalformedURLException, IOException,
			NoSuchAlgorithmException {
		super(locale, rootURL, apiURL, mediaUrl);
	}

	@Override
	public void render(StringBuilder b, boolean visitRoot) {
		PlainTextContentWriter v = new PlainTextContentWriter();
		if (visitRoot) {
			v.dispatchVisit(tagtree);
		} else {
			for (Tag c : tagtree.getChildren()) {
				v.dispatchVisit(c);
			}
		}
		b.append(v.getContent());
	}
}
