                 Sztakipedia parser v0.1b
                 ------------------------

Description
-----------

Sztakipedia parser is a Java application and library to parse Wikipedia-style
wikitext and convert it to other formats. It supports only a subset of the wiki 
syntax understood by MediaWiki's parser and deliberately does not implement its
obscure/undocumented features, such as on-the-fly error recovery.

Sztakipedia is maintained on Google Code:
http://code.google.com/p/sztakipedia-parser

 
Copyright and licensing
-----------------------

Source code is made available under GNU GPL v2+, licensed to MTA SZTAKI, 
Hungary. For exact terms and conditions, see LICENSE.txt


Documentation
-------------

For more information, see the project page: 
http://code.google.com/p/sztakipedia-parser

Javadoc for the current version can be found at:
http://sztakipedia-parser.googlecode.com/svn/trunk/docs/javadoc/index.html


Installation
------------

To grab the latest source code and compile it:

$ svn co http://sztakipedia-parser.googlecode.com/svn/trunk/ sztakipedia-parser
$ cd sztakipedia-parser
$ ant jar


Command line usage
------------------

To convert a piece of wikitext stored in a file to XHTML:

$ java -jar sztakipedia-parser-0.1b.jar input.mediawiki output.xhtml


Library usage
-------------

// Create interpreter
IWikiInterpreter c = new DefaultWikiInterpreter();

// Set CSS classes, pre- and postprocessing options here, see below.

// Create parser
Parser p = new Parser(c);

// Parse wikitext
String html = p.parse(wikitext);

The following settings are valid for the DefaultWikiInterpreter and
HTML output generation.

To set CSS classes for each tag, use the method
public void addCssClasses(Class<? extends Tag> C, List<String> css)

Example: 
Adding a "container" class to all div tags in the output.
List<String> classes = new ArrayList<String>();
classes.add("container");
c.addCssClasses(DivTag.class, classes);

To enable or disable pre- or postprocessing, use the methods
public void setPreprocess(boolean preprocess) and
public void setPostprocess(boolean postprocess)

To enable or disable automatic linking of bare URLs, use the method
public void setAutoLink(boolean autoLink)
Postprocessing must be enabled for this to work.

To enable or disable free text (ie. doesn't contain any wikitext markup) 
begin and end character positions in the output, use the method
public void setBeginEnd(boolean setBeginEnd)
Postprocessing must be enabled for this to work. If enabled, each free 
text will be enclosed in a <span begin="..." end="..."></span> HTML element,
where the begin and end properties are character positions in the 
original wikitext.
 

Authors
-------

See AUTHORS.txt


Feedback
--------

To submit bugs, feature requests, patches please visit
http://code.google.com/p/sztakipedia-parser/issues

To join mailing list, get announcements please visit
http://groups.google.com/group/sztakipedia


History
-------

This is the second complete re-write of a Java wiki parser at MTA SZTAKI, but
the first to be based on JavaCC.
