

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Manga;
import model.User;
import model.User_Manga;
import dao.DAOFactory;
import dao.MangaDAO;
import dao.UserDAO;
import dao.User_MangaDAO;

/**
 * Servlet implementation class MangaManager
 */
@WebServlet("/MangaManager")
public class MangaManager extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MangaManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userPath = request.getServletPath();
		
		//Istanza testTaw per DB connection
		DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		
		//Importo DAO Objects
		User_MangaDAO umDAO = testTaw.getUser_MangaDAO();
		MangaDAO mangaDAO = testTaw.getMangaDAO();
		UserDAO userDAO = testTaw.getUserDAO();

		/*
		 * AddToFavorites: da riscrivere come ToggleFavorites!
		 * Se il manga è già nei favoriti -> rimuoverlo,
		 * se il manga non è tra i favoriti -> aggiungerlo
		 * */
		if("/AddToFavorites".equalsIgnoreCase(userPath))
		{
			
			//Imposto i parametri attraverso le request in arrivo
			String IDManga = request.getParameter("mangaID");
			Long IDUser = Long.valueOf((request.getParameter("userID")));
			String isMyMangaPage = "no";
			if(request.getParameter("isMyMangaPage") != null){
				isMyMangaPage = request.getParameter("isMyMangaPage");
			}
			
			//TODO: check if NULL
			Manga manga = mangaDAO.find(IDManga);
			User user = userDAO.find(IDUser);
			
			//Controllare se il manga è già nella lista dell'utente			
			List<Manga> uManga = umDAO.listMangaOfUser(user);
			
			if(uManga.contains(manga))
			{
				//Rimuovi manga dalla lista
				umDAO.deleteWithUserAndMangaID(user, manga);
				
				if(isMyMangaPage.equalsIgnoreCase("yes")){
					response.sendRedirect("/WebSite/myManga");
				}
				else
					{
						//Reindirizzo alla homepage
						response.sendRedirect("/WebSite");
					}
			}
			else{
				//set del nuovo oggetto User_Manga
				User_Manga userManga = new User_Manga();
				userManga.setManga(manga);
				userManga.setId(null);
				userManga.setUser(user);
				
				//Crea il nuovo 
				umDAO.create(userManga);
			if(isMyMangaPage.equalsIgnoreCase("yes")){
				response.sendRedirect("/WebSite/myManga");
			}
			else
				{
					//Reindirizzo alla homepage
					response.sendRedirect("/WebSite");
				}
			}
		}
		else if("/search".equalsIgnoreCase(userPath))
		{
			String word = request.getParameter("value").toLowerCase();
			String results = "";
			if(word.equals(""))
			{
				results = "";
			}
			else
			{
				results = mangaDAO.search(word);
			}
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.println(results);
			out.flush();
			out.close();
		}
	}

}
