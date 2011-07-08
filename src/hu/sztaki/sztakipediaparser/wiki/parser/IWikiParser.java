package hu.sztaki.sztakipediaparser.wiki.parser;

import hu.sztaki.sztakipediaparser.wiki.converter.IWikiConverter;

public interface IWikiParser {
	public String parse(IWikiConverter c) throws ParseException, TokenMgrError;
}
