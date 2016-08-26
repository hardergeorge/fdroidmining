package org.oregonstate.droidperm.fdroidmining.in;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
@XmlRootElement(name = "application")
public class InApplication {

    private List<InPackage> inPackages = new ArrayList<>();

    private int marketVerCode;

    private String id;
    private String added;
    private String lastUpdated;
    private String name;
    private String summary;
    private String icon;
    private String desc;
    private String license;
    private String categories;
    private String category;
    private String web;
    private String source;
    private String tracker;
    private String marketVersion;
    private String requirements;

    @XmlElement(name = "package")
    public List<InPackage> getInPackages() {
        return inPackages;
    }

    public void setInPackages(List<InPackage> inPackages) {
        this.inPackages = inPackages;
    }

    @XmlElement
    public int getMarketVerCode() {
        return marketVerCode;
    }

    public void setMarketVerCode(int marketVerCode) {
        this.marketVerCode = marketVerCode;
    }

    @XmlElement
    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    @XmlElement
    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    @XmlElement
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @XmlElement
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement
    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @XmlElement
    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    @XmlElement
    public String getMarketVersion() {
        return marketVersion;
    }

    public void setMarketVersion(String marketVersion) {
        this.marketVersion = marketVersion;
    }

    @XmlElement
    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    @XmlElement
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @XmlElement
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @XmlElement
    public String getTracker() {
        return tracker;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    @XmlElement
    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
}