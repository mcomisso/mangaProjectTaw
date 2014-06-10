package model;

import java.io.Serializable;
import java.util.Date;

public class UserSession implements Serializable {
	private static final long serialVersionUID = 1L;
	// Properties ---------------------------------------------------------------------------------

    private String cookieId;
    private User user;
    private Date creationDate;
    private Date lastVisit;
    private int hits;

    // Constructors -------------------------------------------------------------------------------
    /**
     * Default constructor. 
     */
    public UserSession() {
        // Keep it alive.
    }
    
    /**
     * Construct new usersession with given cookie ID. 
     */
    public UserSession(String cookieId) {
        this.cookieId = cookieId;
        this.creationDate = new Date();
        this.lastVisit = new Date();
    }
    
    public void setCookieID(String cookieId){this.cookieId=cookieId;}
    public void setUser(User user){this.user=user;}
    public void setCreationDate(Date creationDate){this.creationDate=creationDate;}
    public void setLastVisit(Date lastVisit){this.lastVisit=lastVisit;}
    public void setHits(int hits){this.hits=hits;}
    
    public String getCookieID(){return this.cookieId;}
    public User getUser(){return this.user;}
    public Date getCreationDate(){return this.creationDate;}
    public Date getLastVisit(){return this.lastVisit;}
    public int getHits(){return this.hits;}


    // Helpers ------------------------------------------------------------------------------------

    /**
     * Add hit (pageview) to the UserSession. Not necessary, but nice for stats.
     */
    public void addHit() {
        this.hits++;
        this.lastVisit = new Date();
    }

    /**
     * A convenience method to check if User is logged in.
     */
    public boolean isLoggedIn() {
        return user != null;
    }
}
