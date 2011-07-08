package hu.sztaki.sztakipediaparser.wiki.tags;

import java.util.Locale;

public class StringTag extends AbstractTag {
	/**************/
	/*** Fields ***/
	/**************/
	
	private String content = "";
	
	/********************/
	/*** Constructors ***/
	/********************/
	
	public StringTag(String content) {
		super();
		this.content = content;
	}
	
	public StringTag(AbstractTag parent, String content) {
		super(parent);
		this.content = content;
	}
	
	public StringTag(AbstractTag parent, Locale locale, String content) {
		super(parent, locale);
		this.content = content;
	}

	/***************/
	/*** Methods ***/
	/***************/
	
	@Override
	public void render(StringBuilder b) {
		b.append(content);
	}

	/***************************/
	/*** Getters and Setters ***/
	/***************************/
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
