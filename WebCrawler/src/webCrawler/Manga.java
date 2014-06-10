package webCrawler;

import java.sql.Date;

/**
 * Oggetto manga, permette il get&set dei vari campi
 *
 * ############### Implementare img default
 */
public class Manga {
	private String IDManga, alias, image, title;
	private double lastdate;
	private int status, hits, ch_len, lang;
	
	//link esterni
	private static String ApiLink = "https://www.mangaeden.com/api/manga/";
	private static String LinkMangaEng = "http://www.mangaeden.com/en-manga/";
	private static String LinkMangaIta = "http://www.mangaeden.com/it-manga/";
	private static String ImgDefaultLink =""; //da implementare img default
	private static String ImgLink = "http://cdn.mangaeden.com/mangasimg/";
	
	
	/**
	 * Costruttore di manga, popola a default: alias="", image="", title="", lastadate=0, status=0, hits=0,
	 * ch_len= 0, lang = -1; 
	 * 
	 * @param id del manga
	 * 
	 */
	public Manga(String id){
		this.IDManga = id;
		this.alias = "";
		this.image = "";
		this.title = "";
		this.lastdate = 0;
		this.status = 0;
		this.hits = 0;
		this.ch_len= 0;
		this.lang = -1; 
	}
	/**
	 * Costruttore di Manga, popola tutti i dati!
	 * @param id del manga
	 * @param alias del manga
	 * @param image del manga
	 * @param title del manga
	 * @param lastdate aggiornamento ultimo capitolo
	 * @param status del manga 0->Suspended, 1->Ongoing, 2->Completed
	 * @param hits numero visualizzazioni
	 * @param ch_len numero capitoli
	 * @param lang linguaggio manga, 0 o 1
	 */
	public Manga(String id, String alias, String image, String title, double lastdate, int status, int hits,int ch_len, int lang){
		this.IDManga = id;
		this.alias = alias;
		this.image = image;
		this.title = title;
		this.lastdate = lastdate;
		this.status = status;
		this.hits = hits;
		this.ch_len= 0;
		this.lang = 0;
	}
	
	//metodi set
	public void setAlias(String a){this.alias = a;}
	public void setImage(String i){this.image = i;}
	public void setTitle(String t){this.title = t;}
	public void setLastDate(double l){this.lastdate = l;}
	public void setStatus(int s){this.status = s;}
	public void setHits(int h){this.hits = h;}
	public void setChLen(int c){this.ch_len = c;}
	public void setLang(int l){this.lang = l;}
	
	//metodi get base
	public String getID(){return this.IDManga;}
	public String getAlias(){return this.alias;}
	public String getImage(){return this.image;}
	public String getTitle(){return this.title;}
	public double getLastDate(){return this.lastdate;}
	public int getStatus(){return this.status;}
	public int getHits(){return this.hits;}
	public int getChLen(){return this.ch_len;}
	public int getLang(){return this.lang;}
	
	//metodi get complessi
	
	/**
	 * Calcola la data e la ritorna come oggetto java.sql.date
	 * @return Date <b>FormattedDate</b> oggetto in formato standard yyyy-mm-dd.
	 */
	public Date getLastDateFormatted(){
		Date FormattedDate = new Date((long)this.lastdate * 1000);
		return FormattedDate;
	}
	
	/**
	 * Elabora il link alla pagina di edenmanga del manga oggetto tenendo conto del linguaggio
	 * @return String <b>Link</b> alla pagina di edenmanga, può ritornare la stringa "errore" nel caso
	 * lang non sia ne ita ne eng (lang=-1)
	 */
	public String getLinkManga(){
		String link="";
		switch (lang){
		case 0: 
			link = Manga.LinkMangaEng + this.alias;
			break;
		case 1: 
			link= Manga.LinkMangaIta + this.alias;
			break;
		default:
			link="errore";
			break;
		}
		return link;
	}
	
	/**
	 * DA COMPLETARE (implementare link img default)
	 * Elabora il link per l'immagine del manga 
	 * @return String <b>link</b> per l'immagine del manga
	 */
	public String getLinkImg(){
		String link="";
		if (this.image!=null || this.image!=""){
			link= Manga.ImgLink + this.image;
			}
		else{
			link= Manga.ImgDefaultLink; //da implementare img default
		}
		return link;
	}
	
	/**
	 * Elabora il Link alle API del manga, dove è possibile recuperare più informazioni tra le quali anche i vari capitoli
	 * @return String <b>link</b> per le API del manga
	 */
	public String getLinkCh(){
		String link= Manga.ApiLink + this.IDManga;
		return link;
	}
	
	
}
