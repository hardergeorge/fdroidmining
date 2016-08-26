package org.oregonstate.droidperm.fdroidmining.in;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
@XmlRootElement(name = "package")
public class InPackage {

    private Hash hash;

    private int versionCode;
    private int size;
    private int sdkVer;
    private int targetSdkVersion;

    private String version;
    private String apkName;
    private String srcName;
    private String sig;
    private String added;
    private List<String> permissions = new ArrayList<>();

    @XmlElement
    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    @XmlElement
    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @XmlElement
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @XmlElement
    public int getSdkVer() {
        return sdkVer;
    }

    public void setSdkVer(int sdkVer) {
        this.sdkVer = sdkVer;
    }

    @XmlElement
    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(int targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
    }

    @XmlElement
    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    @XmlElement
    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    @XmlElement
    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    @XmlElement
    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    @XmlElement
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlElement
    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}