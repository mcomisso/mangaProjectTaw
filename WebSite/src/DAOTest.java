
import java.util.List;

import dao.*;
import model.Manga;
import model.User;
import model.User_Manga;

public class DAOTest {
	public static void main(String[] args) throws Exception {
		// Obtain DAOFactory.
		
		DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		System.out.println("DAOFactory successfully obtained: " + testTaw);
		
		//DAOTest.TestManga(testTaw);
		
		//DAOTest.TestUser(testTaw);
		
		//DAOTest.CreateUser(testTaw);
		//TestUserManga(testTaw);
        
	}	
	
	private static void TestUserManga(DAOFactory testTaw){
		//prendo i vari DAO
		User_MangaDAO umDAO = testTaw.getUser_MangaDAO();
		System.out.println("User_MangaDAO successfully obtained: " + umDAO);
		UserDAO userDAO = testTaw.getUserDAO();
        System.out.println("UserDAO successfully obtained: " + userDAO);
        MangaDAO mangaDAO = testTaw.getMangaDAO();
		System.out.println("MangaDAO successfully obtained: " + mangaDAO);
		
		//Creo e eleziono un utente
		
		// Creo Mario Bianchi.
		System.out.println("Creo Mario Bianchi:");
        User user = new User();
        user.setEmail("mario@bianchi@gmail.com");
        user.setPassword("mbianchi");
        user.setFirstname("Mario");
        user.setLastname("Bianchi");
        userDAO.create(user);
        System.out.println("User successfully created: " + user);
		
		
		//Creo e seleziono un manga
        //CREAZIONE manga prova
        System.out.println("Creo mangaprova:");
  		Manga manga = new Manga();
  		manga.setIDManga("mprova");
  		manga.setAlias("manga-prova");
  		manga.setTitle("manga di prova");
  		mangaDAO.create(manga);
		System.out.println("manga creato!" + manga);
		
		//Creazione associazione tra utente e manga
		System.out.println("Creo usermanga:");
		User_Manga usermanga = new User_Manga();
		usermanga.setManga(manga);
		usermanga.setUser(user);
		umDAO.create(usermanga);
		System.out.println("usermanga creato!" + usermanga);
		System.out.println("provo a stampare id di usermanga: "+usermanga.getId());
		
		System.out.println("Cerco tutti i manga associati a Mario Bianchi: ");
		List<Manga> massociati = umDAO.listMangaOfUser(user);
		if (massociati.size()>0){
			System.out.println("Ho trovato" + massociati.size() + " manga associati a Mario Bianchi: ");
			for(Manga m:massociati){
				System.out.println(m);
			}
		}
		
		System.out.println("Cerco gli ultimi 20 manga associati a Mario Bianchi: ");
		List<Manga> m20associati = umDAO.listMangaOfUser(user);
		if (m20associati.size()>0){
			System.out.println("Ho trovato" + m20associati.size() + " manga su 20 cercati, associati a Mario Bianchi: ");
			for(Manga m:m20associati){
				System.out.println(m);
			}
		}
		
		System.out.println("Cerco utenti associati al manga con id mprova: ");
		List<User> uassociati = umDAO.findUsersByManga(manga);
		if (uassociati.size()>0){
			System.out.println("Ho trovato" + uassociati.size() + " utenti associati a mprova: ");
			for(User u:uassociati){
				System.out.println(u);
			}
		}
		
		System.out.println("Cerco di eliminare associazione: ");
		umDAO.delete(usermanga);
		System.out.println("ELIMINATO? "+ usermanga);
		
	}
	
	private static void CreateUser(DAOFactory testTaw){
		UserDAO userDAO = testTaw.getUserDAO();
        System.out.println("UserDAO successfully obtained: " + userDAO);
        
        
        
        // Create diego.
        User user = new User();
        user.setEmail("diego.regini@gmail.com");
        user.setPassword("ve1nezia");
        user.setFirstname("Diego");
        user.setLastname("Regini");
        userDAO.create(user);
        System.out.println("User successfully created: " + user);
        
        // Create matteo.
        User otheruser = new User();
        otheruser.setEmail("matteo@test.com");
        otheruser.setPassword("ve2nezia");
        otheruser.setFirstname("Matteo");
        otheruser.setLastname("Comisso");
        userDAO.create(otheruser);
        System.out.println("User successfully created: " + otheruser);
        
     // Create matteo.
        User anotheruser = new User();
        anotheruser.setEmail("test@test.com");
        anotheruser.setPassword("test");
        anotheruser.setFirstname("testfirstname");
        anotheruser.setLastname("testlastname");
        userDAO.create(anotheruser);
        System.out.println("User successfully created: " + anotheruser);
        
        
        
	}
	
	private static void TestUser(DAOFactory testTaw){

        // Obtain UserDAO.
        UserDAO userDAO = testTaw.getUserDAO();
        System.out.println("UserDAO successfully obtained: " + userDAO);

        // Create user.
        User user = new User();
        user.setEmail("foo@bar.com");
        user.setPassword("password");
        userDAO.create(user);
        System.out.println("User successfully created: " + user);

        // Create another user.
        User anotherUser = new User();
        anotherUser.setEmail("bar@foo.com");
        anotherUser.setPassword("anotherPassword");
        anotherUser.setFirstname("Bar");
        anotherUser.setLastname("Foo");
        userDAO.create(anotherUser);
        System.out.println("Another user successfully created: " + anotherUser);

        // Update user.
        user.setFirstname("Foo");
        user.setLastname("Bar");
        userDAO.update(user);
        System.out.println("User successfully updated: " + user);

        // Update user.
        user.setFirstname("Foo");
        user.setLastname("Bar");
        userDAO.update(user);
        System.out.println("User successfully updated: " + user);

        // List all users.
        List<User> users = userDAO.list();
        System.out.println("List of users successfully queried: " + users);
        System.out.println("Thus, amount of users in database is: " + users.size());

        // Delete user.
        userDAO.delete(user);
        System.out.println("User successfully deleted: " + user);

        // Check if email exists.
        boolean exist = userDAO.existEmail("foo@bar.com");
        System.out.println("This email should not exist anymore, so this should print false: " + exist);

        // Change password.
        anotherUser.setPassword("newAnotherPassword");
        userDAO.changePassword(anotherUser);
        System.out.println("Another user's password successfully changed: " + anotherUser);

        // Get another user by email and password.
        User foundAnotherUser = userDAO.find("bar@foo.com", "newAnotherPassword");
        System.out.println("Another user successfully queried with new password: " + foundAnotherUser);

        // Delete another user.
        userDAO.delete(foundAnotherUser);
        System.out.println("Another user successfully deleted: " + foundAnotherUser);

        // List all users again.
        users = userDAO.list();
        System.out.println("List of users successfully queried: " + users);
        System.out.println("Thus, amount of users in database is: " + users.size());
	}
	
	private static void TestManga(DAOFactory testTaw){
		// Obtain MangaDAO.
		MangaDAO mangaDAO = testTaw.getMangaDAO();
		System.out.println("MangaDAO successfully obtained: " + mangaDAO);
		
		
		/*
		// List last 20 mangas.
        List<Manga> mangas = mangaDAO.listlast20();
        System.out.println("List of users successfully queried: " + mangas);
        System.out.println("Thus, amount of users in database is: " + mangas.size());
        
        for(Manga manga:mangas){
        	System.out.println(manga.toString());
        }
        
		
		*/
		
		//CREAZIONE MANGA
		System.out.println("Creo manga:");
		Manga manga = new Manga();
		manga.setIDManga("test");
		manga.setAlias("manga-test");
		manga.setTitle("manga prova");
		
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
