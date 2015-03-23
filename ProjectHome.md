**Sztakipedia parser** is a wikitext to HTML converter written in Java whit (partially) supports the syntax understood by [MediaWiki](http://www.mediawiki.org), Wikipedia's wiki engine.

It use a [JavaCC](http://javacc.java.net/) grammar to parse the raw wikitext and build an abstract syntax tree (AST) from which HTML output can be generated. The output generation can be expanded or modified through a java interface.

Features:
  * Bold, italic, hr, heading, indent constructs
  * Unordered lists
  * Internal and external links
  * Raw URLs to an URL template
  * Images
  * Templates
  * Basic tables
  * The possibility to support other languages, not just English.

It was developed as part of the [Sztakipedia](http://pedia.sztaki.hu) project at [MTA SZTAKI](http://sztaki.hu) in Hungary.