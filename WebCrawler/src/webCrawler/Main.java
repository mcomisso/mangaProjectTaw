package webCrawler;

import org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.*;


public class Main {

	public static void main(String[] args) throws Exception
	{
		Crawler italiano = new Crawler();
		DB database = new DB();
		
		
		TestObjManga();
		JSONObject lista = italiano.crawlList("list", "0");
		JSONArray manga = lista.getJSONArray("manga");
		
		//Al momento è commentato l'insert vero e proprio
		//Insert();
		
		return;	
	}
	
	//metodo per testare la classe manga DA ELIMINARE
	public static void TestObjManga(){
		System.out.println("test manga: ");
		Manga test = new Manga("mangat", "asla", "imga", "tstag", 45, 12, 90,0,0);
		test.setHits(100);
		test.setStatus(43);
		System.out.println("id: " + test.getID());
		System.out.println("Alias: " + test.getAlias());
        System.out.println("IMG: " + test.getImage());
        System.out.println("LastDate: " + test.getLastDate());
        System.out.println("Status: " + test.getStatus());
        System.out.println("Title: " + test.getTitle());
        System.out.println("Hits: " + test.getHits());
	}

	public void SelectFromID(String ID) throws Exception
	{
		DB database = new DB();
		ResultSet risultati = database.select(ID);
		while(risultati.next())
		{
			//System.out.println("ID: " + risultati.getString("IDManga"));
            System.out.println("Alias: " + risultati.getString("Alias"));
            System.out.println("IMG: " + risultati.getString("IMG"));
            System.out.println("LastDate: " + risultati.getString("LastDate"));
            System.out.println("Status: " + risultati.getString("Status"));
            System.out.println("Title: " + risultati.getString("Title"));
            System.out.println("Hits: " + risultati.getString("Hits"));
            System.out.println("Ch_Len: " + risultati.getString("Ch_Len"));
            System.out.println("Lang: " + risultati.getString("Lang"));
		}
		return;
	}
	

	public static void Insert() throws Exception
	{
		Crawler italiano = new Crawler();
		DB database = new DB();
		JSONObject lista = italiano.crawlList("list", "1");
		JSONArray manga = lista.getJSONArray("manga");
		for(int i=0; i<manga.length(); i++)
		{
			JSONObject obj = manga.getJSONObject(i);
			String id, alias, image, title;
			double lastdate;
			int status, hits;
			if(obj.isNull("i") == false) { id = obj.getString("i"); } else { id = null; }
			if(obj.isNull("a") == false) { alias = obj.getString("a"); } else { alias = null; }
			if(obj.isNull("im") == false) { image = obj.getString("im"); } else { image = null; }
			if(obj.isNull("ld") == false) { lastdate = obj.getDouble("ld"); } else { lastdate = 0.0; }
			if(obj.isNull("s") == false) { status = obj.getInt("s"); } else { status = 0; }
			if(obj.isNull("h") == false) { hits = obj.getInt("h"); } else { hits = 0; }
			if(obj.isNull("t") == false) { title = obj.getString("t"); } else { title = null; }
			int chlen = 0;
			int lang = 1;
			//database.insert(id, alias, image, lastdate, status, title, hits, chlen, lang);
			System.out.println("Posizione:"+i+" Ho inserito: " + obj.getString("t"));
		}
		System.out.println("Ho finito di inserire.");
		return;	
	}
}