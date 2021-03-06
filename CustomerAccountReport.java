/*
 *  Read in TWO serialized files of objects: 
 *     Customer
 *     Security
 *  Store objects in collections - each file/type in a separate collection
 *  Sort each collection into ASCENDING ORDER by custNumber
 *  
 *  Process match/merge report:
 *      All Security objects
 *          By Customer
 * 
 * (c) 2017, Terri Davis
 * 
 */

/******************************************************************************************************************
  *****************************************************************************************************************
  ******************          ADD NECESSARY IMPORT STATEMENTS HERE
  *****************************************************************************************************************
  ******************************************************************************************************************/ 

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.bind.JAXB;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class CustomerAccountReport
{
  /******************************************************************************************************************
    *****************************************************************************************************************
    ******************          ADD DECLARATIONS FOR INPUT FILES AND COLLECTIONS TO STORE OBJECTS
    *****************************************************************************************************************
    ******************************************************************************************************************/ 
  
  private static BufferedReader inputCust; // input for custFile
  private static BufferedReader inputSecurity; // input for securityFile
  private static List<Customer> customerList = new ArrayList<Customer>();
  private static List<Security> securityList = new ArrayList<Security>();
  private static ListIterator<Security> equity;
  private static Double managedAssets = 0.0;
  
  /**************************************************************************************************************
    *  main method 
    *************************************************************************************************************/
  public static void main( String[] args)
  {
    /*
     *  The try block executes the following processes:
     *     Open input files
     *     create and output report entries
     *     close input files
     */   
    try
    {
      openFiles();
      processReport();
      closeFiles();
    }  // end try
    catch( IOException ioErr )                       // If there is any error in opening,reading, or closing files
    {
      System.err.println( "Ending process." );
    } // end catch
    catch( Exception err )                           // Any/all other exceptions passed up to main
    {
      System.err.printf( "Serious error. Ending proces.  %s",
                        err.getClass( ).getName( ) );
      err.printStackTrace( );
    } 
    
  }// end main
  
  /***************************************************************************************************************
    *  Open the input files
    **************************************************************************************************************/   
  /*
   *  Locate the physical file; define it as an input stream; define the input stream as an object stream.
   *  Prepare to deseralize objects from the file.
   */
  public static void openFiles( )
    throws IOException
  {
    /******************************************************************************************************************
      *****************************************************************************************************************
      ******************          ADD CODE TO OPEN INPUT FILES, INCLUDED NECESSARY EXCPETION HANDLING
      *****************************************************************************************************************
      ******************************************************************************************************************/
    
    try
    {
      inputCust = Files.newBufferedReader(Paths.get("customerFile.xml")); // open custfile.xml 
      inputSecurity = Files.newBufferedReader(Paths.get("securityFile.xml")); // open securityFile.xml
      
    } // end try
    
    catch (IOException e)
    {
      System.err.println("Error opening either custFile.xml or securityFile.xml.");
      
    } // end catch
    
  } // end method openFile
  
  
  /***************************************************************************************************************
    *  Manage the actual reporting process
    **************************************************************************************************************/   
  /*
   * Deserialize Customer file; store objects in collection. Sort collection when file is completely deserialized.
   * 
   * Deserialize Security file; store objects in collection. Sort collection when file is completely deserialized.
   * 
   * Process match/merge based on Customer.
   *  For each Customer:
   *       Output Customer information.
   *       Output Security information
   *       Output CostBasis for Security 
   *  
   */
  public static void processReport( )
    throws Exception
  {
    try
    {
      prepCustomerCollection( );
      prepSecurityCollection( );
      reportEquityPositions( );
    }
    catch( Exception err )
    {
      System.err.printf( "Serious error. Ending proces.  %s",
                        err.getClass( ).getName( ) );
      err.printStackTrace( );
    }
    
  } // end method processRecords
  
  /***************************************************************************************************************
    *  Close the input files
    **************************************************************************************************************/
  public static void closeFiles( )
    throws IOException
  {
    /******************************************************************************************************************
      *****************************************************************************************************************
      ******************          ADD CODE TO CLOSE INPUT FILES, INCLUDED NECESSARY EXCPETION HANDLING
      *****************************************************************************************************************
      ******************************************************************************************************************/
    try{
      
      inputCust.close(); // close custFile.xml
      inputSecurity.close();// close securityFile.xml
      
    } // end try
    
    catch (IOException e)
    {
      // error message if unable to close xml files
      System.err.printf("Error closing BufferedReader for either custFile.xml or securityFile.xml.");
      throw e;
    } // end catch
    
  } // end method closeFiles
  
  
  /***************************************************************************************************************
    *  Prepare the Customer collection
    **************************************************************************************************************/   
  /*
   *  Deserialize all objects from serialized Customer file
   *  As each object is deserialized (read), add it to the collection holding Customer objects
   *  Once all Customer objects have been stored in the collection,
   *  Sort the collection in ascending order, based on custNumber value of each object.
   */
  public static void prepCustomerCollection( )
    throws ClassNotFoundException,
    IOException
  {
    /******************************************************************************************************************
      *****************************************************************************************************************
      ******************          ADD CODE TO: 
      ******************                        DESERIALIZE CUSTOMER OBJECTS  
      ******************                        ADD OBJECTS TO COLLECTION  
      ******************                        HANDLE NECESSARY EXCEPTIONS  
      ******************                        SORT COMPLETED COLLECTION INTO ASCENDING ORDER BY custNumber
      *****************************************************************************************************************
      ******************************************************************************************************************/
    
    Customers customers = JAXB.unmarshal(inputCust, Customers.class);
    customerList.addAll(customers.getCustomers());
    Collections.sort(customerList, new CustCompare());
    
  } // end prepCustomerCollection
  
  
  /***************************************************************************************************************
    *  Prepare the Security collection
    **************************************************************************************************************/   
  /*
   *  Deserialize all objects from serialized Security file
   *  As each object is deserialized (read), add it to the collection holding Security objects
   *  Once all Security objects have been stored in the collection,
   *  Sort the collection in ascending order, based on custNumber value of each object.
   */
  public static void prepSecurityCollection( )
    throws ClassNotFoundException,
    IOException
  {
    /******************************************************************************************************************
      *****************************************************************************************************************
      ******************          ADD CODE TO: 
      ******************                        DESERIALIZE SECURITY OBJECTS  
      ******************                        ADD OBJECTS TO COLLECTION  
      ******************                        HANDLE NECESSARY EXCEPTIONS  
      ******************                        SORT COMPLETED COLLECTION INTO ASCENDING ORDER BY custNumber
      *****************************************************************************************************************
      ******************************************************************************************************************/ 
    
    Securities securities = JAXB.unmarshal(inputSecurity, Securities.class);
    securityList.addAll(securities.getSecurities());
    Collections.sort(securityList, new SecurityCompare());
    
    
  } // end prepSecurityCollection
  
  /***************************************************************************************************************
    *  Report Equity Positions
    **************************************************************************************************************/   
  /*
   *    Once both input files have been read, both collections built, and both sorted,
   *    Use a matching algorithm to report equity positions for all Customers 
   */
  public static void reportEquityPositions( )
  { 
    /*
     * Declare an object to use to move through the collection of Security objects
     * Using that object, pick up the 'next' Security object available, assign it the name �equity�
     * Retrieve the custNumber value from that object and store it in a variable named 'holdEquity'
     */ 
    
    /******************************************************************************************************************
      *****************************************************************************************************************
      ******************          ADD THE FOLLOWING CODE =ABOVE= THE CALL TO findCustomer:
      ******************              Declare an iterator for use with the Security collection
      ******************              Use that iterator to retrieve a Security object; name the object retrieved "equity"
      ******************              Retrieve the custNumber of the equity object; store it as "holdEquity"
      *****************************************************************************************************************
      ******************************************************************************************************************/ 
    
    Iterator<Security> securityIterator = securityList.iterator(); 
    Security equity = securityIterator.next();
    String holdEquity = equity.getCustNumber();
    
    
    findCustomer( holdEquity );                      // Pass the custNumber value retrieved from the Security
    
    try
    {
      while( true )
      {
        while( equity.getCustNumber( ).matches( holdEquity ) )
        {
          // Output a line describing this Security
          System.out.printf( "\t%-10s %-5s carries a cost basis of %15s%n",
                            equity.getClass( ).getName( ),
                            equity.getSymbol( ),
                            String.format( "$%,.2f", equity.calcCost( ) ) );
          // Move to the next Security
          managedAssets += equity.getPurchPrc() * equity.getShares();
          equity = securityIterator.next( );
        }  // end INNER while
        // This is just an indication that we're done with this Customer's Security objects
        System.out.printf( "=========================================================================%n%n" );
        holdEquity = equity.getCustNumber( );
        findCustomer( holdEquity );
      }  // end OUTER while
    }
    catch( NoSuchElementException endOfCollection )
    {
      /*
       * This Exception is EXPECTED to be thrown when we 'run out' of Security or Customer objects
       */
      System.out.printf( "%n%n\t\t\t ***** END OF EQUITY REPORT *****%n" );
    }  // end catch NoSuchElementException  
    
  } // end reportEquityPositions
  
  /***************************************************************************************************************
    *  Find Customer
    **************************************************************************************************************/   
  /*
   *  Using the target custNumber passed into the method from the calling code...
   *     Search the sotred and sorted Customer objects for the one matching the provided target
   *     Assign the located Customer object to the reference found
   *     Using the Customer reference found, produce the report header
   */
  public static void findCustomer( String findCust )
  {
    Customer target;                               // 'template' for search process
    Customer found;                                // 'holding space' for the located Customer object
    
    try
    {
      /*
       *  Set up for Customer search. 
       *  The "important" piece is the custNumber -> findCust.All other information is essentially filler and
       *    will not be used in the search process.
       */ 
      target = new Customer( findCust,
                            "000000000",
                            "blank",
                            "blank",
                            0,
                            false,
                            false );
      
      
      
      /******************************************************************************************************************
        *****************************************************************************************************************
        ******************          ADD CODE TO SEARCH CUSTOMER COLLECTION FOR DESIRED CUSTOMER OBJECT,
        ******************          AND TO THEN RETRIEVE THE LOCATED OBJECT. NAME THE RETRIEVED OBJECT "found"
        *****************************************************************************************************************
        ******************************************************************************************************************/ 
      
      int foundCustomer = Collections.binarySearch(customerList, target, new CustCompare());
      found = customerList.get(foundCustomer);
      
      // Print the first report break header
      System.out.printf( "%n%-20s %-20s Customer %s, TIN %s%n",   
                        found.getFirst( ),
                        found.getLast( ),
                        found.getCustNumber( ),
                        found.getTin( ) );
    }
    catch( CustomerException err )
    {
      System.err.printf( "Error searching Customer List%n" );
    }  // end catch
  }  // end findCustomer
  
} // end class CustomerAccountReport
