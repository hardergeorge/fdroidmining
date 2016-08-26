package org.oregonstate.droidperm.fdroidmining.out;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
@XmlRootElement(name = "package")
public class OutPackage {

    private int targetSdkVersion;

    private String date;
    private List<String> permissions = new ArrayList<>();

    @XmlElement
    private boolean usesMethod = false;
    @XmlElement
    private boolean usesURI = false;
    @XmlElement
    private boolean usesMixed = false;
    @XmlElement
    private boolean usesStorage = false;

    @XmlElement
    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    @XmlElement
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean usesMethod() {
        return usesMethod;
    }

    public void setUsesMethod(boolean usesMethod) {
        this.usesMethod = usesMethod;
    }

    public boolean usesMixed() {
        return usesMixed;
    }

    public void setUsesMixed(boolean usesMixed) {
        this.usesMixed = usesMixed;
    }

    public boolean usesStorage() {
        return usesStorage;
    }

    public void setUsesStorage(boolean usesStorage) {
        this.usesStorage = usesStorage;
    }

    public boolean usesURI() {
        return usesURI;
    }

    public void setUsesURI(boolean usesURI) {
        this.usesURI = usesURI;
    }

    @XmlElement(name = "permission")
    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
