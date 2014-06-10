package model;

import java.io.Serializable;
import java.util.Date;


public class Updates implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Double runDate;
	
	public Updates(){
		Date now = new Date();
		this.runDate = (double) now.getTime();
	}
	
	public void setId(Long id){this.id=id;}
	public void setRunDate(Double runDate){this.runDate=runDate;}
	
	public Long getId(){return this.id;}
	public Double getRunDate(){return this.runDate;}
	
	
    /**
     * Ritorna la Stringa per questo update
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Update[id=%d,runDate=%f]", 
            id, runDate);
    }

}
