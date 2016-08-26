package org.oregonstate.droidperm.fdroidmining.out;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
@XmlRootElement(name = "application")
public class Application {

    private String name;
    private OutPackage aOutPackage;
    private int quarterMigrated = 0;

    @XmlElement(name = "package")
    public OutPackage getaOutPackage() {
        return aOutPackage;
    }

    public void setaOutPackage(OutPackage aOutPackage) {
        this.aOutPackage = aOutPackage;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuarterMigrated() {
        return quarterMigrated;
    }

    public void setQuarterMigrated(int quarterMigrated) {
        this.quarterMigrated = quarterMigrated;
    }
}
