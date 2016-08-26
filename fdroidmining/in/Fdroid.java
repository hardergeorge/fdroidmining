package org.oregonstate.droidperm.fdroidmining.in;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
@XmlRootElement(name = "fdroid")
public class Fdroid {

    private Repo repo;
    private List<InApplication> inApplications = new ArrayList<>();

    @XmlElement
    public Repo getRepo() {
        return repo;
    }

    public void setRepo(Repo repo) {
        this.repo = repo;
    }

    @XmlElement(name = "application")
    public List<InApplication> getInApplications() {
        return inApplications;
    }

    public void setInApplications(List<InApplication> inApplications) {
        this.inApplications = inApplications;
    }
}
