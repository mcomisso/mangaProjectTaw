

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOFactory;
import dao.UserDAO;
import dao.UserSessionDAO;
import model.User;
import model.UserSession;

/**
 * Servlet implementation class CheckUser
 */
public class CheckUser extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userPath = request.getServletPath();
		if("/LogOut".equalsIgnoreCase(userPath)){
			Cookie loginCookie = null;
	        Cookie[] cookies = request.getCookies();
	        if(cookies != null){
		        for(Cookie cookie : cookies){
		            if(cookie.getName().equals("mySessionId")){
		                loginCookie = cookie;
		                break;
		            }
		        }
	        }
	        if(loginCookie != null){
	            loginCookie.setMaxAge(0);
	            response.addCookie(loginCookie);
	        }
	        response.sendRedirect("/WebSite/myManga");
		}else {
			response.sendRedirect("/WebSite/Error404");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userPath = request.getServletPath();
		DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		UserDAO userDAO = testTaw.getUserDAO();
		UserSessionDAO usersessionDAO = testTaw.getUserSessionDAO();
		if("/Logging".equalsIgnoreCase(userPath)){
			String email = request.getParameter("email");
			String pwd = request.getParameter("pwd");
			
			User user = userDAO.find(email, pwd);
			
			if(user!=null){
				String cookieId = null;
				UserSession userSession = usersessionDAO.find(user);
				if(userSession==null){
					cookieId = UUID.randomUUID().toString();
					userSession = new UserSession(cookieId);
					userSession.setUser(user);
					usersessionDAO.create(userSession);
				}
				cookieId = userSession.getCookieID();
				Cookie loginCookie = new Cookie("mySessionId",cookieId);
				loginCookie.setMaxAge(365*24*60*60);//i cookie durano 365 giorni
				response.addCookie(loginCookie);
		        response.sendRedirect("/WebSite/myManga"); //carica myManga (non passa parametri)
			}else{
				response.sendRedirect("/WebSite/LogInError");//di fatto interviene MainCore che ricarica la pagina	
			}
		}else if("/Signupping".equalsIgnoreCase(userPath)){
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String email = request.getParameter("email");
			String pwd = request.getParameter("pwd");
			if(!(email.equalsIgnoreCase("") || pwd.equalsIgnoreCase(""))){
				boolean exist = userDAO.existEmail(email);
				if(!exist){
					User user = new User();
			        user.setEmail(email);
			        user.setPassword(pwd);
			        user.setFirstname(firstname);
			        user.setLastname(lastname);
			        userDAO.create(user);
			        
			        user = userDAO.find(email, pwd);
			        if(user!=null){
			        	String cookieId = null;
						UserSession userSession = usersessionDAO.find(user);
						if(userSession==null){
							cookieId = UUID.randomUUID().toString();
							userSession = new UserSession(cookieId);
							userSession.setUser(user);
							usersessionDAO.create(userSession);
						}
						cookieId = userSession.getCookieID();
						Cookie loginCookie = new Cookie("mySessionId",cookieId);
						loginCookie.setMaxAge(365*24*60*60);//i cookie durano 365 giorni
						response.addCookie(loginCookie);
				        response.sendRedirect("/WebSite/myManga");
			        }else{
						response.sendRedirect("/WebSite/Error404");//di fatto interviene MainCore che ricarica la pagina	
					}
				}else{
					response.sendRedirect("/WebSite/SignUpError");
				}
			}else{
				response.sendRedirect("/WebSite/SignUpError2");
			}
			
		}else if("/editingData".equalsIgnoreCase(userPath)){
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String email = request.getParameter("email");
			User user = userFromCookie(request, usersessionDAO);
			if(user == null){
					response.sendRedirect("/WebSite/Error404");// da cambiare in utente non loggato
					return;
				}
			if(user.getEmail().equalsIgnoreCase(email)){
				//devo aggiornare solo firstname & lastname
				user.setFirstname(firstname);
				user.setLastname(lastname);
			}else if(!userDAO.existEmail(email)){
				user.setFirstname(firstname);
				user.setLastname(lastname);
				user.setEmail(email);
				}else{
					//esiste già una mail uguale, non è quella dell'utente stesso.
					response.sendRedirect("/WebSite/MailExist");
					return;
				}
			userDAO.update(user);
			response.sendRedirect("/WebSite/myManga");
			//manca far notare che gli aggiornamenti son riusciti
			
		}else if("/editingPwd".equalsIgnoreCase(userPath)){
			String oldPwd = request.getParameter("oldPwd");
			String newPwd = request.getParameter("newPwd");
			String verifyNewPwd = request.getParameter("verifyNewPwd");
			User user = userFromCookie(request, usersessionDAO); //errore in caso nn trovi cookie, stampare a video
			if(user!=null){
				if(userDAO.find(user.getEmail(), oldPwd)!=null){
					if(newPwd.equals(verifyNewPwd)){
						user.setPassword(newPwd);
						userDAO.changePassword(user);//eccezione da gestire
						response.sendRedirect("/WebSite/myManga");//manca far notare che gli aggiornamenti son riusciti
						return;
					}else{
						//errore newPwd != verifyNewPwd
						response.sendRedirect("/WebSite/conflictPwd");
					}
				}else{
					//errore oldPwd != user.getPassword
					response.sendRedirect("/WebSite/WrongPwd");
				}
			}else{
				response.sendRedirect("/WebSite/Error404");
			}
		}else if("/deletingUser".equalsIgnoreCase(userPath)){
			User user = userFromCookie(request, usersessionDAO);
			if(user!=null){
				userDAO.delete(user);
				response.sendRedirect("/WebSite/HomePage");
			}else{
				response.sendRedirect("/WebSite/Error404"); //utente non presente in cookie quindi impossibile che arrivi qua ERRORE
			}
		}else{
			response.sendRedirect("/WebSite/Error404");
		}
	}
	
	private User userFromCookie(HttpServletRequest request, UserSessionDAO usersessionDAO){
		Cookie[] cookies = request.getCookies();
		String cookieId = null;
		User user = null;
		if(cookies != null){
			for(Cookie cookie : cookies){
			    if("mySessionId".equals(cookie.getName())){
			    	cookieId = cookie.getValue();
			    }
			}
			if (cookieId!=null){
				if(usersessionDAO.find(cookieId)!=null){
					UserSession userSession = usersessionDAO.find(cookieId);
					user = userSession.getUser();
				}
			}else {
				return user;// da cambiare in utente non loggato
			}
		}
		return user;
	}
}
