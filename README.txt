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

TODO 5-line code snippet on how to use
 

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