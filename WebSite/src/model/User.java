package model;

import java.io.Serializable;

public class User implements Serializable {
	// Constants ----------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Properties ---------------------------------------------------------------------------------

    private Long id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;

    // Getters/setters ----------------------------------------------------------------------------

    public Long getId() {return id;}
    public String getEmail() {return email;}
	public String getPassword() {return password;}
	public String getFirstname() {return firstname;}
	public String getLastname() {return lastname;}
	
	public void setId(Long id) {this.id = id;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public void setLastname(String lastname) {this.lastname = lastname;}

    // Object overrides ---------------------------------------------------------------------------

 // Object overrides ---------------------------------------------------------------------------

    /**
     * L'id utente è unico. Quindi basta comparare l'ID
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof User) && (id != null)
             ? id.equals(((User) other).id)
             : (other == this);
    }

    /**
     * L'id utente è unico. Quindi basta comparare ritornare l'hashcode dell'id
     * The user ID is unique for each User. So User with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null) 
             ? (this.getClass().hashCode() + id.hashCode()) 
             : super.hashCode();
    }

    /**
     * Ritorna la Stringa per questo utente, eccetto la password
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("User[id=%d,email=%s,firstname=%s,lastname=%s]", 
            id, email, firstname, lastname);
    }

    
}
