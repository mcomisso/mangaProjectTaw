package webCrawler;

import java.net.*;
import java.io.*;

import org.json.JSONObject;

public class Crawler
{
	public Crawler() throws Exception { }

	/**
	 *	Il metodo apre una connessione all'URL, richiede e legge il contenuto.
	 * @param  crawlType Indica quale lista verra' recuperata dal crawler.
	 * @param  id		 Indica l'id della lista: lingua, manga o capitolo singolo.
	 * @return			 Restituisce un oggetto JSON contenente le informazioni richieste.
	 */
	public JSONObject crawlList(String crawlType, String id) throws Exception
	{
		URL crawlURL = new URL("http://www.mangaeden.com/api/" + crawlType + "/" + id + "/");
		BufferedReader in = new BufferedReader(new InputStreamReader(crawlURL.openStream()));
		StringBuilder assembly = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) { assembly.append(inputLine); }
		in.close();
		JSONObject results = new JSONObject(assembly.toString());
		return results;
	}
}