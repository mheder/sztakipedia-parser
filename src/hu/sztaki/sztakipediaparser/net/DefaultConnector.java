package hu.sztaki.sztakipediaparser.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DefaultConnector implements IConnector {
	private URL url = null;
	private URLConnection con = null;

	public DefaultConnector(String url) throws MalformedURLException, IOException {
		this.url = new URL(url);
		con = this.url.openConnection();
		con.setDoOutput(true);
	}
	
	@Override
	public boolean login(String username, String password) throws ConnectionDownException {
		if(con == null) {
			throw new ConnectionDownException();
		}
		
		return false;
	}

	@Override
	public String query(String q) throws ConnectionDownException, UnsupportedEncodingException, IOException {
		if(con == null) {
			throw new ConnectionDownException();
		}

		// Write query
		OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
		writer.write(q);
		writer.flush();
		writer.close();
		
		// Read answer
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String answer = "";
		String line;
		while((line = reader.readLine()) != null) {
			answer += line + "\n";
		}
		reader.close();
		
		return answer;
	}

}
