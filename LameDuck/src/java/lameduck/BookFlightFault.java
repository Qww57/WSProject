
package lameduck;

import javax.xml.ws.WebFault;


/**
 * @Author Daniel Brand, Ali C
 * 
 */
@WebFault(name = "FaultMessage", targetNamespace = "http://j2ee.netbeans.org/wsdl/LameDuckWS/LameDuckWS/LameDuck")
public class BookFlightFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private String faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public BookFlightFault(String message, String faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public BookFlightFault(String message, String faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: java.lang.String
     */
    public String getFaultInfo() {
        return faultInfo;
    }

}