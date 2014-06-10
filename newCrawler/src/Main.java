import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Manga;
import model.Updates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.DAOException;
import dao.DAOFactory;
import dao.MangaDAO;
import dao.UpdatesDAO;


public class Main {
	private static String MangaList = "http://www.mangaeden.com/api/list/";
	//private static String MangaInfo = "http://www.mangaeden.com/api/manga/";
	//private static String ChapterPage = "http://www.mangaeden.com/api/chapter/";
	private static int ita = 1;
	private static int eng = 0;
	public static void main(String[] args){
		
		DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		//System.out.println("DAOFactory successfully obtained: " + testTaw);
		UpdatesDAO updateDAO = testTaw.getUpdatesDAO();
		
		JSONObject listaEng = null;
		JSONArray mangaJSONEng = null;
		JSONObject listaIta = null;
		JSONArray mangaJSONIta = null;
		
		try {
			listaEng = crawlList(MangaList, eng);
			mangaJSONEng = listaEng.getJSONArray("manga");
			listaIta = crawlList(MangaList, ita);
			mangaJSONIta = listaIta.getJSONArray("manga");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Manga> mangaList = new ArrayList<Manga>();
		Updates lastUpdate =null;
		try{
			lastUpdate = updateDAO.last();
		} catch (DAOException e){
			System.out.println("DB inaccessibile");
			return;
		}
		
		Long lastdate = (long) 0;
		if (lastUpdate!=null){
		 lastdate = lastUpdate.getRunDate().longValue();
		}
		
		
		if(lastdate>0.0){
			for(int i=0; i<mangaJSONEng.length(); i++){
				try {
					JSONObject obj = mangaJSONEng.getJSONObject(i);
					Manga manga = map(obj, eng);
					if(manga.getLastDate().longValue()>lastdate){
						mangaList.add(manga);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (int i=0; i<mangaJSONIta.length(); i++){
				try{
					JSONObject obj = mangaJSONIta.getJSONObject(i);
					Manga manga = map(obj, ita);
					if(manga.getLastDate().longValue()>lastdate){
						mangaList.add(manga);
					}
				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			//PRIMA VOLTA Caricare tutto DB
			
			for(int i=0; i<mangaJSONEng.length(); i++){
				try {
					JSONObject obj;
					obj = mangaJSONEng.getJSONObject(i);
					Manga manga = map(obj, eng);
					mangaList.add(manga);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int i=0; i<mangaJSONIta.length(); i++){
				try {
					JSONObject obj;
					obj = mangaJSONIta.getJSONObject(i);
					Manga manga = map(obj, ita);
					mangaList.add(manga);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		updateDB(mangaList, lastdate);
}
	
	private static void updateDB(List<Manga> mangaList, Long lastDate){
		System.out.println();
		System.out.println("Numero di manga da aggiornare: "+mangaList.size());
		
		DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		MangaDAO mangaDAO = testTaw.getMangaDAO();
		UpdatesDAO updateDAO = testTaw.getUpdatesDAO();
		
		Iterator<Manga> iterator = mangaList.iterator();
		int j=0;
		int k=0;
		int i=0;
		while(iterator.hasNext()){
			Manga mangatemp = iterator.next();
			if(lastDate == 0){
				mangaDAO.create(mangatemp);
				System.out.println("#"+i+" Aggiungo: "+mangatemp);
			}else if(mangatemp.getLastDate().longValue()>lastDate){
				// cerco un manga con stesso id, se presente AGGIORNO, se no AGGIUNGO in DB
				Manga mangaToCheck = mangaDAO.find(mangatemp.getIDManga());
				if(mangaToCheck!=null){
					mangaDAO.update(mangatemp);
					System.out.println("#"+i+" Aggiorno: "+mangatemp);
					j++;
				}else{
					mangaDAO.create(mangatemp);
					System.out.println("#"+i+" Aggiungo: "+mangatemp);
					k++;
				}
			}
			i++;
		}
		System.out.println("Ho aggiornato "+j+" manga");
		System.out.println("Ho aggiunto "+k+" manga");
		Updates thisUpdate = new Updates();
		updateDAO.create(thisUpdate);
		return;
		
	}
	
    private static Manga map(JSONObject obj, int lang) throws JSONException {
    	Manga manga = new Manga();
        manga.setIDManga(obj.getString("i"));
    	manga.setAlias(obj.getString("a"));
    	try{
    		manga.setImage(obj.getString("im"));
    	}catch (JSONException e){
    		manga.setImage("null");
    	}
    	Double ld = 0.0;
    	if(obj.has("ld"))
    		ld = obj.getDouble("ld");
    	manga.setLastDate(ld);
    	manga.setStatus(obj.getInt("s"));
    	manga.setTitle(obj.getString("t"));
    	manga.setHits(obj.getInt("h"));
    	manga.setLang(lang);
        return manga;
    }
	
	private static JSONObject crawlList(String pageToCrawl, int idOrLang) throws JSONException, IOException
	{
		URL crawlURL = new URL(pageToCrawl+ "/" + idOrLang + "/");
		BufferedReader in = new BufferedReader(new InputStreamReader(crawlURL.openStream()));
		StringBuilder assembly = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) { assembly.append(inputLine); }
		in.close();
		JSONObject results = new JSONObject(assembly.toString());
		return results;
	}
	
}
