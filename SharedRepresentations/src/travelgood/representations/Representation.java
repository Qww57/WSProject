/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelgood.representations;

import java.util.List;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Link.JaxbAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Daniel
 */
public abstract class Representation {
    
    @XmlJavaTypeAdapter(JaxbAdapter.class)
    public List<Link> links;
    
    public Representation() {
        
    }
    
    public Link getLinkByRelation(String rel) {
        for (Link link : links) {
            if (link.getRel().equals(rel)) {
                return link;
            }
        }
        return null;
    }
}
