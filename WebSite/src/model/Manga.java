package model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Oggetto manga, permette il get&set dei vari campi
 *
 * ############### Implementare img default
 * WebSite, ma ideato per WebCrawler
 */
public class Manga implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Properties ---------------------------------------------------------------------------------
	private String IDManga, alias, image, title;
	private Double lastdate;
	private Integer status, hits, ch_len, lang;
	
	//link esterni
	private static String ApiLink = "https://www.mangaeden.com/api/manga/";
	private static String LinkMangaEng = "http://www.mangaeden.com/en-manga/";
	private static String LinkMangaIta = "http://www.mangaeden.com/it-manga/";
	private static String ImgDefaultLink ="http://static.fjcdn.com/pictures/404_fdee8d_249836.jpg";
	private static String ImgLink = "http://cdn.mangaeden.com/mangasimg/";
	
	/**
	 * Costruttore senza parametri
	 *  *this.IDManga = null;
		this.alias = null;
		this.image = null;
		this.title = null;
		this.lastdate = (double) oggi.getTime();
		this.status = 1;
		this.hits = 0;
		this.ch_len= 0;
		this.lang = -1; 
	 */
	public Manga(){
		Date oggi = new Date();
		this.IDManga = null;
		this.alias = null;
		this.image = null;
		this.title = null;
		this.lastdate = (double) oggi.getTime();
		this.status = 1;
		this.hits = 0;
		this.ch_len= 0;
		this.lang = -1; 
	}
	
	//Setter ---------------------------------------------------------------------------
	public void setIDManga(String id){this.IDManga = id;}
	public void setAlias(String alias){this.alias = alias;}
	public void setImage(String img){this.image = img;}
	public void setTitle(String title){this.title = title;}
	public void setLastDate(Double lastdate){this.lastdate = lastdate;}
	public void setStatus(Integer status){this.status = status;}
	public void setHits(Integer hits){this.hits = hits;}
	public void setChLen(Integer chlen){this.ch_len = chlen;}
	public void setLang(Integer lang){this.lang = lang;}
	
	
	//Base getters ---------------------------------------------------------------------------
	public String getIDManga(){return this.IDManga;}
	public String getAlias(){return this.alias;}
	public String getImage(){return this.image;}
	public String getTitle(){return this.title;}
	public Double getLastDate(){return this.lastdate;}
	public int getStatus(){return this.status;}
	public int getHits(){return this.hits;}
	public int getChLen(){return this.ch_len;}
	public int getLang(){return this.lang;}
	
	
	//Advanced getters ---------------------------------------------------------------------------
	/**
	 * Calcola la data e la ritorna come oggetto java.sql.date
	 * @return Date <b>FormattedDate</b> oggetto in formato standard yyyy-mm-dd. Pu� tornare null
	 */
	public Date getLastDateFormatted(){
		if (this.lastdate!=null){
			Date FormattedDate = new Date(this.lastdate.longValue());
			return FormattedDate;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Elabora il link alla pagina di edenmanga del manga oggetto tenendo conto del linguaggio
	 * @return String <b>Link</b> alla pagina di edenmanga, pu� ritornare la stringa "errore" nel caso
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
		if (this.image==null || this.image.equals("null")){
			link = Manga.ImgDefaultLink;
		}
		else{
			link = Manga.ImgLink + this.image;
			
		}
		return link;
	}
	
	/**
	 * Elabora il Link alle API del manga, dove � possibile recuperare pi� informazioni tra le quali anche i vari capitoli
	 * @return String <b>link</b> per le API del manga
	 */
	public String getLinkCh(){
		String link= Manga.ApiLink + this.IDManga;
		return link;
	}
	
	// Object overrides ---------------------------------------------------------------------------
	
	 /**
     * The user ID is unique for each User. So this should compare User by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Manga) && (IDManga != null)
             ? IDManga.equals(((Manga) other).IDManga)
             : (other == this);
    }

    /**
     * The user ID is unique for each User. So User with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (IDManga != null) 
             ? (this.getClass().hashCode() + IDManga.hashCode()) 
             : super.hashCode();
    }

    /**
     * Returns the String representation of this User. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Manga\n[IDManga = %s,\n alias = %s,\n image = %s,\n title = %s,\n lastdate = %f,\n status = %d,\n hits = %d,\n ch_len = %d,\n lang = %d]", 
        		IDManga, alias, image, title, lastdate, status, hits, ch_len, lang);
    }
	
}
