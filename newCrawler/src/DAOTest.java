/*

import dao.*;
import model.Manga;

public class DAOTest {
	public static void main(String[] args) throws Exception {
		// Obtain DAOFactory.
		
		DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		System.out.println("DAOFactory successfully obtained: " + testTaw);
		
		//DAOTest.TestManga(testTaw);
		
		//DAOTest.TestUser(testTaw);
		
		//DAOTest.CreateUser(testTaw);
		//DAOTest.TestUserManga(testTaw);
		
		//DAOTest.TestManga(testTaw);
        
	}	
	
	private static void TestManga(DAOFactory testTaw){
		// Obtain MangaDAO.
		MangaDAO mangaDAO = testTaw.getMangaDAO();
		System.out.println("MangaDAO successfully obtained: " + mangaDAO);
		
		
		
		// List last 20 mangas.
        //List<Manga> mangas = mangaDAO.listlast20();
        //System.out.println("List of users successfully queried: " + mangas);
        //System.out.println("Thus, amount of users in database is: " + mangas.size());
        
        //for(Manga manga:mangas){
        //	System.out.println(manga.toString());
        //}
        
		
		
		//CREAZIONE MANGA
		System.out.println("Creo manga:");
		Manga manga = new Manga();
		manga.setIDManga("test");
		manga.setAlias("manga-test");
		manga.setTitle("manga prova");
		System.out.println("pre-create");
		mangaDAO.create(manga);
		System.out.println("manga creato!" +manga);
		
		//RICERCA MANGA
		System.out.println("cerco manga:");
		Manga mangatrovato = mangaDAO.find("test");
		System.out.println("manga trovato!" +mangatrovato.toString());
		
		//AGGIORNO MANGA
		System.out.println("aggiorno manga:");
		manga.setAlias("manga-test-aggiornato");
        mangaDAO.update(manga);
		
      //RICERCA MANGA
        System.out.println("cerco manga dopo aggiornamento:");
      	mangatrovato = mangaDAO.find("test");
      	System.out.println("manga trovato!" +mangatrovato.toString());
		
		//ELIMINAZIONE MANGA
		System.out.println("ora elimino il manga");
		mangaDAO.delete(mangatrovato);
        System.out.println("Manga eliminato! " + mangatrovato);
        
        
        mangatrovato = mangaDAO.find("test");
        if(mangatrovato!=null){
        	System.out.println("manga trovato comunque!" +mangatrovato.toString());
        }else{
        	System.out.println("non trovo il manga");
        }
	}

}
*/