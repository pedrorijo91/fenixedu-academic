// Generated by impart OJB Generator
// www.impart.ch matthias.roth@impart.ch
// created at 19 Sep 2003 15:50:58 GMT

package middleware.middlewareDomain;


public class MWBranch extends MWDomainObject
{

  private Integer branchcode;
  private Integer degreecode;
  private String description;
  private Integer orientationcode;
  private Integer idInternal;



  public Integer getBranchcode()
  {
     return this.branchcode;
  }
  public void setBranchcode(Integer param)
  {
    this.branchcode = param;
  }


  public Integer getDegreecode()
  {
     return this.degreecode;
  }
  public void setDegreecode(Integer param)
  {
    this.degreecode = param;
  }


  public String getDescription()
  {
     return this.description;
  }
  public void setDescription(String param)
  {
    this.description = param;
  }


  public Integer getOrientationcode()
  {
     return this.orientationcode;
  }
  public void setOrientationcode(Integer param)
  {
    this.orientationcode = param;
  }


  public String toString(){
    return  " [branchCode] " + branchcode + " [degreeCode] " + degreecode + " [description] " + description + " [orientationCode] " + orientationcode;

  }
/**
 * @return
 */
public Integer getIdInternal() {
	return idInternal;
}

/**
 * @param idInternal
 */
public void setIdInternal(Integer idInternal) {
	this.idInternal = idInternal;
}

}
