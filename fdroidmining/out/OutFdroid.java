package org.oregonstate.droidperm.fdroidmining.out;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
@XmlRootElement(name = "fdroid-apps")
public class OutFdroid {
    private List<Application> applications = new ArrayList<>();

    @XmlElement(name = "application")
    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
