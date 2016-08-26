package org.oregonstate.droidperm.fdroidmining;

import org.oregonstate.droidperm.fdroidmining.in.Fdroid;
import org.oregonstate.droidperm.fdroidmining.out.OutFdroid;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
public class FdroidMinerMain {

    public static void main(final String[] args) throws JAXBException, IOException {

        //Unmarshall the incoming xml
        Fdroid fdroid = FdroidMiner.unmarshallXML(new File(args[0]));

        //Create a new outgoing data object and store the converted data in it
        OutFdroid outFdroid = new OutFdroid();
        outFdroid.setApplications(FdroidMiner.InAppstoOutApps(fdroid.getInApplications()));

        //Get the statistics and print them
        FdroidMiner.countAndPrintStatistics(outFdroid.getApplications());

        //Marshal the JAXB objects into an xml document
        FdroidMiner.marshallXML(outFdroid, new File(args[1]));
    }
}
