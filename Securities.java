/**
 * Securities class
 * 
 * Purpose: XML helper class to support deserialization of securityFile.XML
 * 
 * Programmer: Dontez Wherry
 */

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class Securities {
  
  // @XMLElment specifies XML element name for each object in the List
  @XmlElements({
    @XmlElement (type = Stock.class, name = "stock"),
    @XmlElement (type= MutualFund.class, name = "mutualFund")
  })
    
  private List<Security> securities = new ArrayList<Security>(); // stores Security
  
  //return the List<Security>
  
  public List<Security> getSecurities() 
  {
    return securities;
    
  } // end list<Security>
  
} // end class Securities
