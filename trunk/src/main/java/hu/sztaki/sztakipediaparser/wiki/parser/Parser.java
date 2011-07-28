/*
 * Sztakipedia parser - http://http://code.google.com/p/sztakipedia-parser
 *
 * Copyright (C) 2011 MTA SZTAKI 
 * Copyright (C) 2011 Tibor Olah
 *
 * Sztakipedia parser is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * Sztakipedia parser is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sztakipedia parser; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package hu.sztaki.sztakipediaparser.wiki.parser;

import hu.sztaki.sztakipediaparser.wiki.converter.InterpreterUtils;
import hu.sztaki.sztakipediaparser.wiki.converter.DefaultWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.converter.IWikiInterpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Main class of the parser.
 * 
 * <p>
 * This class has a main method which is the entry point of the command line
 * version. It also has the parse method which should be called to generate the
 * html output from the source wikitext.
 * </p>
 * 
 * @author <a href="mailto:sztakipedia@sztaki.hu">Tibor Olah</a>, <a
 *         href="http://sztaki.hu">MTA SZTAKI</a>
 * @since 2011
 */
public class Parser {
	private String[] sections;
	private Splitter splitter = new Splitter("\n==[=]?[=]?([^=]*)==[=]?[=]?");
	private IWikiInterpreter c;

	public Parser(IWikiInterpreter c) {
		this.c = c;
	}

	/**
	 * Break wikitext to and array of strings. Each element in the array will be
	 * a section of the original wikitext.
	 * 
	 * @param wikitext
	 */
	private void breakToSections(String wikitext) {
		sections = splitter.split(wikitext);
	}

	/**
	 * Parse the given wikitext and return the html output in a string. Parsing
	 * is done separately for each section.
	 * 
	 * @param wikitext
	 * @return Html output of the parser.
	 */
	public String parse(String wikitext) {
		wikitext = c.preprocess(wikitext);

		breakToSections(wikitext);

		// Parse each section with JavaCCWikiParser
		for (int i = 0; i < sections.length; i++) {
			// newlines = 0;
			if (!sections[i].isEmpty()) {
				c.reset();
				// Replace lost newlines
				if (sections[i].matches("(?s)==[=]?[=]?([^=]*)==[=]?[=]?.*")) {
					sections[i] = "\n" + sections[i];
				}

				if (sections[i].matches("(?s).*==[=]?[=]?([^=]*)==[=]?[=]?")) {
					sections[i] += "\n";
				}

				JavaCCWikiParser p = new JavaCCWikiParser(new StringReader(
						sections[i]));
				p.parse(c);
			}
		}

		StringBuilder b = new StringBuilder();
		c.postprocess(wikitext);
		c.render(b);

		return InterpreterUtils.trim(b.toString());
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Usage: java -jar SZPParser.jar infile outfile");
			System.exit(0);
		}

		File infile = new File(args[0]);
		String outfile = args[1];

		// Read input
		long time = System.currentTimeMillis();
		String wikitext = readFileAsString(infile);
		System.out.println("Input read in "
				+ (System.currentTimeMillis() - time) + " ms.");

		// Create converter
		IWikiInterpreter c = new DefaultWikiInterpreter();

		// Create parser
		Parser p = new Parser(c);

		// Parse
		time = System.currentTimeMillis();
		String html = p.parse(wikitext);
		System.out.println("Parsed in: " + (System.currentTimeMillis() - time)
				+ " ms.");

		// Write output
		FileWriter writer = new FileWriter(outfile);
		writer.write(html);
		writer.flush();
		writer.close();
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @author Illes Solt
	 * @throws IOException
	 */
	public static String readFileAsString(File file) throws IOException {
		return readFileAsString(file, Charset.defaultCharset());
	}

	/**
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @author Illes Solt
	 * @throws IOException
	 */
	public static String readFileAsString(File file, Charset charset)
			throws IOException {
		final byte[] contents = readFileAsBytes(file);

		return new String(contents, charset);
	}

	/**
	 * Efficiently read a file into a byte array.
	 * 
	 * @param file
	 * @return
	 * @author Illes Solt
	 * @throws IOException
	 */
	protected static byte[] readFileAsBytes(File file) throws IOException {
		final byte[] contents;

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();

			// Get the file's size and then map it into memory
			int sz = (int) fc.size();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);

			if (bb.hasArray()) {
				contents = bb.array();
			} else {
				contents = new byte[sz];
				bb.get(contents);
			}

		} catch (IOException e) {
			throw new RuntimeException("Error while trying to read file: "
					+ file.getCanonicalPath(), e);
		} finally {
			if (fis != null)
				fis.close();
		}

		return contents;
	}
}
