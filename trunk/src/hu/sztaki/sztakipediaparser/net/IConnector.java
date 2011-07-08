package hu.sztaki.sztakipediaparser.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface IConnector {
	public boolean login(String username, String password) throws ConnectionDownException;
	public String query(String q) throws ConnectionDownException, UnsupportedEncodingException, IOException;
}
