/**
 * Customers class
 * 
 * Purpose: XML helper class to support deserialization of customerFile.XML
 * 
 * Programmer: Dontez Wherry
 */

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class Customers {
  
   // @XMLElment specifies XML element name for each object in the List
  @XmlElement(type = Customer.class, name = "customer")
  
  private List<Customer> customers = new ArrayList<>(); // stores Customers
  
  //return the List<Customer>
  public List<Customer> getCustomers() 
  {
    return customers;
    
  } // end list<Customer>
  
  
} // end class Customers
