/**
 * SecurityCompare class
 * 
 * Purpose: Comparator to compare customer numbers in order to sort 
 *          in ascending order
 * 
 * Programmer: Dontez Wherry
 */

import java.util.Comparator;

public class SecurityCompare implements Comparator<Security> {
  
  // recieve customer number objects from Security class for comparison
  public int compare (Security cust1, Security cust2)
  {
    return cust1.getCustNumber().compareTo(cust2.getCustNumber());
    
  } // end method compare
  
} // end class SecurityCompare
