/**
 * CustCompare class
 * 
 * Purpose: Comparator to compare customer numbers in order to sort 
 *          in ascending order
 * 
 * Programmer: Dontez Wherry
 */

import java.util.Comparator;

public class CustCompare implements Comparator<Customer>{
  
  // recieve customer number objects from Customer class for comparison
  public int compare (Customer cust1, Customer cust2)
  {
    return cust1.getCustNumber().compareTo(cust2.getCustNumber());
    
  } // end method compare
  
} // end class Comparator
