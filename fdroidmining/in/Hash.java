package org.oregonstate.droidperm.fdroidmining.in;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
@XmlRootElement(name = "hash")
public class Hash {

    private String type;
    private String hashValue;

    public Hash() {
    }

    @XmlAttribute
    public String getType () {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement
    public String getHashValue () {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }
}
