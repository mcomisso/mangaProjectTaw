package model;

import java.io.Serializable;


public class User_Manga implements Serializable {
	// Constants ----------------------------------------------------------------------------------
    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------
	private Long id;
	private Manga manga;
	private User user;
	
    // Getters/setters ----------------------------------------------------------------------------

	public Long getId() { return this.id; }
	public Manga getManga() { return this.manga; }
	public User getUser() { return this.user; }

	public void setId(Long id) { this.id = id; }
	public void setManga(Manga manga) { this.manga = manga; }
	public void setUser(User user) { this.user = user; }
	
    /**
     * Returns the String representation of this UserManga. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return String.format("userManga\n[id = %s,\n emailUser = %s,\n AliasManga = %s]",id, user.getEmail(), manga.getAlias());
    }
}
