import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOException;
import dao.DAOFactory;
import dao.MangaDAO;
import dao.UserDAO;
import dao.UserSessionDAO;
import model.Manga;
import model.User;
import model.UserSession;
import dao.User_MangaDAO;

/**
 * Servlet implementation class MainCore
 */
@WebServlet("/MainCore")
public class MainCore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String Home = "/WEB-INF/HomePage.jsp";
	private static String myManga = "/WEB-INF/MyManga.jsp";
	private static String LogIn = "/WEB-INF/LogIn.jsp";
	private static String SignUp = "/WEB-INF/SignUp.jsp";
	private static String Error = "/WEB-INF/Error.jsp";
	private static String Error500 = "/WEB-INF/Error500.jsp";
	private static String Settings = "/WEB-INF/Settings.jsp";
	private static String AllMangaList = "/WEB-INF/AllMangaList.jsp";
	// Obtain DAOFactory.
	DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
	//System.out.println("DAOFactory successfully obtained: " + testTaw);
	        
	// Obtain mangaDAO.
	MangaDAO mangaDAO = testTaw.getMangaDAO();
	//System.out.println("MangaDAO successfully obtained: " + mangaDAO);
	
	UserDAO userDAO = testTaw.getUserDAO();
	UserSessionDAO usersessionDAO = testTaw.getUserSessionDAO();
	
	User_MangaDAO umDAO = testTaw.getUser_MangaDAO();
	
	User loggedUser=null;

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainCore() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		loggedUser=null;
        PrintWriter out= response.getWriter();
		
		//String url = request.getRequestURI();
		String userPath = request.getServletPath();
		boolean logged =false;
		if(!(("/Error500".equalsIgnoreCase(userPath))||("/Error404".equalsIgnoreCase(userPath)))){
			logged =false;
			try{
				logged = CheckLogin(request);
			}catch (DAOException e){
				response.sendRedirect("/WebSite/Error500");
				return;
			}
		}
		
		if("/myManga".equalsIgnoreCase(userPath)){
			if(logged){
				request.setAttribute("loggedUser", loggedUser);
				List<Manga> umList = umDAO.listLast20MangaOfUser(loggedUser);
				request.setAttribute("listOfUserMangaLikes", umList);
			}
			request.getRequestDispatcher(myManga).forward(request, response);
			
			//quello sotto serve nel caso voglia usare out
			//RequestDispatcher rd = getServletContext().getRequestDispatcher(myManga);
            //rd.include(request, response);
			
		}else if("/LogIn".equalsIgnoreCase(userPath)){
			if(logged){
				response.sendRedirect("/WebSite/myManga");
			}else{
				request.getRequestDispatcher(LogIn).forward(request, response);
			}
			
		}else if("/SignUp".equalsIgnoreCase(userPath)){
			if(logged){
				response.sendRedirect("/WebSite/myManga");
			}else{
				request.getRequestDispatcher(SignUp).forward(request, response);
			}
			
		}if("/editData".equalsIgnoreCase(userPath)){
			if(!logged){
				response.sendRedirect("/WebSite/myManga");
			}else{
				request.setAttribute("TODO", "editData");
				request.setAttribute("loggedUser", loggedUser);
				request.getRequestDispatcher(Settings).forward(request, response);
			}
			
		}if("/editPwd".equalsIgnoreCase(userPath)){
			if(!logged){
				response.sendRedirect("/WebSite/myManga");
			}else{
				request.setAttribute("TODO", "editPwd");
				request.getRequestDispatcher(Settings).forward(request, response);
			}
			
		}if("/deleteUser".equalsIgnoreCase(userPath)){
			if(!logged){
				response.sendRedirect("/WebSite/myManga");
			}else{
				request.setAttribute("TODO", "deleteUser");
				request.setAttribute("loggedUser", loggedUser);
				request.getRequestDispatcher(Settings).forward(request, response);
			}
			
		}if("/listManga".equalsIgnoreCase(userPath)){
			if(logged){
				request.setAttribute("loggedUser", loggedUser);
				List<Manga> allList = mangaDAO.list();
				request.setAttribute("allList", allList);
			}
			request.getRequestDispatcher(AllMangaList).forward(request, response);
			
		}else if("/HomePage".equalsIgnoreCase(userPath)){
			if(logged){
				request.setAttribute("loggedUser", loggedUser);
				List<Manga> userLikesManga = umDAO.listMangaOfUser(loggedUser);
				request.setAttribute("allMangaOfUser", userLikesManga);
			}
			List<Manga> mangas20 = mangaDAO.listlast20();
	        request.setAttribute("mangas20", mangas20); // Will be available as ${mangas20} in JSP
	        request.getRequestDispatcher(Home).forward(request, response);
	        
		}else if("/LogInError".equalsIgnoreCase(userPath)){
			String message = "<div data-alert class='alert-box warning round'>Email o password errati.<a href='#' class='close'>&times;</a></div>";
			request.setAttribute("message", message);
			request.getRequestDispatcher(LogIn).forward(request, response);
            
		}else if("/SignUpError".equalsIgnoreCase(userPath)){
			String message = "<div data-alert class='alert-box warning round'>La email &egrave; gi&agrave; presente in DB, provare con una mail differente!<a href='#' class='close'>&times;</a></div>";
			request.setAttribute("message", message);
			request.getRequestDispatcher(SignUp).forward(request, response);
            
		}else if("/SignUpError2".equalsIgnoreCase(userPath)){
			String message = "<div data-alert class='alert-box warning round'>I campi email e password non possono essere vuoti!<a href='#' class='close'>&times;</a></div>";
			request.setAttribute("message", message);
			request.getRequestDispatcher(SignUp).forward(request, response);
			
		}else if("/MailExist".equalsIgnoreCase(userPath)){
			if(logged){
				String message = "La email è già presente in DB, provare con una mail differente!";
				request.setAttribute("TODO", "editData");
				request.setAttribute("loggedUser", loggedUser);
				request.setAttribute("message", message);
				request.getRequestDispatcher(Settings).forward(request, response);
			}else{
				request.getRequestDispatcher(LogIn).forward(request, response);
			}
		}else if("/conflictPwd".equalsIgnoreCase(userPath)){
			if(logged){
				String message = "<div data-alert class='alert-box warning round'>La nuova password non corrisponde alla password di verifica.<a href='#' class='close'>&times;</a></div>";
				request.setAttribute("message", message);
				request.setAttribute("TODO", "editPwd");
				request.getRequestDispatcher(Settings).forward(request, response);
			}else{
				request.getRequestDispatcher(LogIn).forward(request, response);
			}
		}else if("/WrongPwd".equalsIgnoreCase(userPath)){
			if(logged){
				String message = "<div data-alert class='alert-box warning round'>La password inserita non corrisponde a quella salvata in DB.<a href='#' class='close'>&times;</a></div>";
				request.setAttribute("message", message);
				request.setAttribute("TODO", "editPwd");
				request.getRequestDispatcher(Settings).forward(request, response);
			}else{
				request.getRequestDispatcher(LogIn).forward(request, response);
			}
		}else if("/AddFavoritesError".equalsIgnoreCase(userPath)){
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher(Home);
			out.println("<font color=red>Errore nell'aggiunta del manga</font>");
			rd.include(request, response);
			
		}else if("/Error404".equalsIgnoreCase(userPath)){
			request.getRequestDispatcher(Error).forward(request, response);
		}else if("/Error500".equalsIgnoreCase(userPath)){
			request.getRequestDispatcher(Error500).forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	// Helpers ------------------------------------------------------------------------------------
	/**
	 * Controlla se son presente cookie, e se corrispondono ad una UserSession ritorna true
	 * dopo aver aumentato Hit di 1. 
	 * Popola loggedUser con l'utente corrispondente alla UserSession
	 * @param request - dove controllare i cookie
	 * @param countHit - Se true aggiunge un hit alla sessione utente
	 * @return true cookie valido, false se non son presenti cookie o non corrispondono ad una
	 * UserSession valida
	 * @throws DAOException se ci sono problemi con il DB
	 */
	private boolean CheckLogin(HttpServletRequest request) throws DAOException{
		//controllo la connessione al DB
		try{
			usersessionDAO.find("asd");
		}catch (DAOException e){throw new DAOException(e);}
		
		Cookie[] cookies = request.getCookies();
		String cookieId = null;
		if(cookies != null){
			for(Cookie cookie : cookies){
			    if("mySessionId".equals(cookie.getName())){
			    	cookieId = cookie.getValue();
			    }
			}
			if (cookieId!=null){
				UserSession userSession = usersessionDAO.find(cookieId);
				if(userSession!=null){
					loggedUser = userSession.getUser();
					return true;
				}
			}
		}
		return false;
	}

}
