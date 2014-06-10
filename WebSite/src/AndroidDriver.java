

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dao.*;
import model.*;

/**
 * Servlet implementation class AndroidDriver
 */

public class AndroidDriver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidDriver() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Date oggi = new Date();
		
		System.out.println("[AndroidDriver]["+oggi.getTime()+"] [START]");
		// Obtain DAOFactory.
		DAOFactory testTaw = DAOFactory.getInstance("testTaw.jdbc");
		//System.out.println("DAOFactory successfully obtained: " + testTaw);
		        
		// Obtain mangaDAO.
		MangaDAO mangaDAO = testTaw.getMangaDAO();
		UserDAO userDAO = testTaw.getUserDAO();
		UserSessionDAO usersessionDAO = testTaw.getUserSessionDAO();
		User_MangaDAO umDAO = testTaw.getUser_MangaDAO();
		
		PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
        String opCode="";
        
        if(request.getParameter("opCode")!=null){
        	//Operation code coming from Android 
        	opCode = request.getParameter("opCode");
        }
        opCode= opCode.trim();
        JsonObject myObj = new JsonObject();
        Gson gson = new Gson();
        String error="unknown error";
        
        if(opCode.isEmpty() || opCode==null){
        	error="opCode null or embty";
        	JsonElement Jerror = gson.toJsonTree(error);
            myObj.addProperty("success", false);
            myObj.add("error", Jerror);
            System.out.println("[AndroidDriver] [ERROR] opCode: null or emty");
            
        }else if(opCode.equalsIgnoreCase("update")){//android richiede gli ultimi 20 manga aggiornati
        	System.out.println("[AndroidDriver] [START UPDATE] opCode: "+opCode);
        	
        	try{
            	List<Manga> mangas20 = mangaDAO.listlast20();
            	JsonElement Jmangas20 = gson.toJsonTree(mangas20);
                myObj.addProperty("success", true);
                myObj.add("Jmangas20", Jmangas20);
                System.out.println("[AndroidDriver] [END UPDATE POST] ["+oggi.getTime()+"]");
            }catch (DAOException e){
            	error="DB ERROR";
            	JsonElement Jerror = gson.toJsonTree(error);
            	myObj.addProperty("success", false);
            	myObj.add("error", Jerror);
            	System.out.println("[AndroidDriver][UPDATE] DB ERROR");
            }
        }else if(opCode.equalsIgnoreCase("sessionId")){//android richiede sessione utente inviando email e pwd, ritorno JsessionId
        	System.out.println("[AndroidDriver] [START SESSION] opCode: "+opCode);
        	String email = request.getParameter("email");
        	String pwd = request.getParameter("pwd");
        	if(email!=null || pwd!=null){
        		email=email.trim();
        		pwd=pwd.trim();
            	System.out.println("[AndroidDriver][SESSION] recived email: "+email);
            	System.out.println("[AndroidDriver][SESSION] recived pwd: "+pwd);
            	try{
            		User user = null;
                	user = userDAO.find(email, pwd);
                	
                	if(user!=null){//utente trovato
                		UserSession userSession = usersessionDAO.find(user);
                		String sessionId;
                		if(userSession==null){
                			sessionId = UUID.randomUUID().toString();
                			userSession = new UserSession(sessionId);
        					userSession.setUser(user);
        					usersessionDAO.create(userSession);
                		}else{
                			sessionId = userSession.getCookieID();
                		}
                		JsonElement JsessionId = gson.toJsonTree(sessionId);
                		myObj.addProperty("success", true);
                		myObj.add("JsessionId", JsessionId);
                	}else{//utente non trovato
                		error="wrong/inexistent user";
                    	JsonElement Jerror = gson.toJsonTree(error);
                    	myObj.addProperty("success", false);
                    	myObj.add("error", Jerror);
                        System.out.println("[AndroidDriver][SESSION] wrong/inexistent user");
                	}
            	}catch (DAOException e){
            		error="DB ERROR";
                	JsonElement Jerror = gson.toJsonTree(error);
                	myObj.addProperty("success", false);
                	myObj.add("error", Jerror);
                	System.out.println("[AndroidDriver][SESSION] DB ERROR");
                }
        	}else{
        		error="nullerror";
            	JsonElement Jerror = gson.toJsonTree(error);
            	myObj.addProperty("success", false);
            	myObj.add("error", Jerror);
            	System.out.println("[AndroidDriver][LAST 20 MANGA] DB ERROR");
        	}
        	System.out.println("[AndroidDriver] [END SESSION POST] ["+oggi.getTime()+"]");
        	
        }else if(opCode.equalsIgnoreCase("user")){//android richiede utente inviando sessionId, ritorno utente
        	System.out.println("[AndroidDriver] [START USER] opCode: "+opCode);
        	User user = null;
        	String sessionId = request.getParameter("sessionId");
        	if(sessionId!=null){
        		sessionId=sessionId.trim();
            	try{
                	UserSession userSession = usersessionDAO.find(sessionId);
                	if(userSession!=null){
                		System.out.println("[AndroidDriver][USER] recived sessionId: "+sessionId);
                		user = userSession.getUser();
                		if(user!=null){
                			JsonElement Juser = gson.toJsonTree(user);
                			myObj.addProperty("success", true);
                			myObj.add("Juser", Juser);
                		}
                	}else{
            			System.out.println("[AndroidDriver][USER] wrong/inexistent session or user");
            			error="wrong/inexistent session or user";
                    	JsonElement Jerror = gson.toJsonTree(error);
                    	myObj.addProperty("success", false);
                    	myObj.add("error", Jerror);
                	}
            	}catch (DAOException e){
            		error="DB ERROR";
                	JsonElement Jerror = gson.toJsonTree(error);
                	myObj.addProperty("success", false);
                	myObj.add("error", Jerror);
                	System.out.println("[AndroidDriver][USER] DB ERROR");
                }
        	}else{
        		error="nullerror";
            	JsonElement Jerror = gson.toJsonTree(error);
            	myObj.addProperty("success", false);
            	myObj.add("error", Jerror);
            	System.out.println("[AndroidDriver][LAST 20 MANGA] DB ERROR");
        	}
        	System.out.println("[AndroidDriver] [END USER POST] ["+oggi.getTime()+"]");
        	
        }else if(opCode.equalsIgnoreCase("last20MangaOfUser")){//android richiede i 20 manga utente
        	System.out.println("[AndroidDriver] [START LAST 20 MANGA] opCode: "+opCode);
        	User user = null;
        	List<Manga> umList = null;
        	String sessionId = request.getParameter("sessionId");
        	if(sessionId!=null){
        		sessionId=sessionId.trim();
            	try{
                	UserSession userSession = usersessionDAO.find(sessionId);
                	if(userSession!=null){
                		user = userSession.getUser();
                		if(user!=null){
                    		umList = umDAO.listLast20MangaOfUser(user);
                    		JsonElement JumList = gson.toJsonTree(umList);
                    		myObj.addProperty("success", true);
                    		myObj.add("JumList", JumList);
                		}
                	}else{
                		System.out.println("[AndroidDriver][LAST 20 MANGA] wrong/inexistent session or user");
                		error="wrong/inexistent session or user";
                    	JsonElement Jerror = gson.toJsonTree(error);
                    	myObj.addProperty("success", false);
                    	myObj.add("error", Jerror);
                	}
            	}catch (DAOException e){
            		error="DB ERROR";
                	JsonElement Jerror = gson.toJsonTree(error);
                	myObj.addProperty("success", false);
                	myObj.add("error", Jerror);
                	System.out.println("[AndroidDriver][LAST 20 MANGA] DB ERROR");
                }
        	}else{
        		error="nullerror";
            	JsonElement Jerror = gson.toJsonTree(error);
            	myObj.addProperty("success", false);
            	myObj.add("error", Jerror);
            	System.out.println("[AndroidDriver][LAST 20 MANGA] DB ERROR");
        	}
        	System.out.println("[AndroidDriver] [END LAST 20 MANGA POST] ["+oggi.getTime()+"]");
        	
        }else if(opCode.equalsIgnoreCase("allMangaOfUser")){//android richiede tutti i manga utente
        	System.out.println("[AndroidDriver] [START ALL MANGA OF USER POST] ["+oggi.getTime()+"]");
        	User user=null;
        	List<Manga> umList = null;
        	String sessionId = request.getParameter("sessionId");
        	if(sessionId!=null){
            	sessionId=sessionId.trim();
            	try{
            		UserSession userSession = usersessionDAO.find(sessionId);
                	if(userSession!=null){
                		user = userSession.getUser();
                		if(user!=null){
                			umList = umDAO.listMangaOfUser(user);
                			JsonElement JumList = gson.toJsonTree(umList);
                    		myObj.addProperty("success", true);
                    		myObj.add("JumList", JumList);
                		}
                	}else{
                		System.out.println("[AndroidDriver][ALL MANGA OF USER] wrong/inexistent session or user");
                		error="wrong/inexistent session or user";
                    	JsonElement Jerror = gson.toJsonTree(error);
                    	myObj.addProperty("success", false);
                    	myObj.add("error", Jerror);
                	}
            	}catch (DAOException e){
            		error="DB ERROR";
                	JsonElement Jerror = gson.toJsonTree(error);
                	myObj.addProperty("success", false);
                	myObj.add("error", Jerror);
                	System.out.println("[AndroidDriver][ALL MANGA OF USER] DB ERROR");
                }
        	}else{
        		error="nullerror";
            	JsonElement Jerror = gson.toJsonTree(error);
            	myObj.addProperty("success", false);
            	myObj.add("error", Jerror);
            	System.out.println("[AndroidDriver][LAST 20 MANGA] DB ERROR");
        	}
        	System.out.println("[AndroidDriver] [END ALL MANGA OF USER POST] ["+oggi.getTime()+"]");
        	
        }else if(opCode.equalsIgnoreCase("allManga")){//android richiede tutti i manga del DB
        	System.out.println("[AndroidDriver] [START ALL MANGA] opCode: "+opCode);
        	try{
            	List<Manga> mangas = mangaDAO.list();
            	JsonElement Jmangas = gson.toJsonTree(mangas);
                myObj.addProperty("success", true);
                myObj.add("Jmangas", Jmangas);
                System.out.println("[AndroidDriver] [END ALL MANGA POST] ["+oggi.getTime()+"]");
            }catch (DAOException e){
            	error="DB ERROR";
            	JsonElement Jerror = gson.toJsonTree(error);
            	myObj.addProperty("success", false);
            	myObj.add("error", Jerror);
            	System.out.println("[AndroidDriver][ALL MANGA] DB ERROR");
            }
        	
        }else{//android chiede qualcosa che non dovrebbe chiedere
        	JsonElement Jerror = gson.toJsonTree(error);
        	myObj.addProperty("success", false);
        	myObj.add("error", Jerror);
        }
        
        
        out.println(myObj.toString());
        System.out.println("[AndroidDriver] [SENT] I sent this: "+ myObj.toString());
        System.out.println("[AndroidDriver]["+oggi.getTime()+"] [END]");
        out.close();
	}

}
