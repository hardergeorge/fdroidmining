package org.oregonstate.droidperm.fdroidmining;

import org.oregonstate.droidperm.fdroidmining.in.Fdroid;
import org.oregonstate.droidperm.fdroidmining.in.InApplication;
import org.oregonstate.droidperm.fdroidmining.in.InPackage;
import org.oregonstate.droidperm.fdroidmining.out.Application;
import org.oregonstate.droidperm.fdroidmining.out.OutFdroid;
import org.oregonstate.droidperm.fdroidmining.out.OutPackage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author George Harder <harderg@oregonstate.edu> Created on 8/23/2016.
 */
public class FdroidMiner {
    //Method permission bucket
    public static final String[] METHOD_PERMS = { "AUTHENTICATE_ACCOUNTS",
                                                  "GET_ACCOUNTS",
                                                  "MANAGE_ACCOUNTS",
                                                  "USE_CREDENTIALS",
                                                  "AUTHENTICATE_ACCOUNTS",
                                                  "DISABLE_KEYGUARD",
                                                  "BLUETOOTH",
                                                  "BLUETOOTH_ADMIN",
                                                  "READ_SYNC_STATS",
                                                  "READ_SYNC_SETTINGS",
                                                  "WRITE_SYNC_SETTINGS",
                                                  "USE_FINGERPRINT",
                                                  "ACCESS_COARSE_LOCATION",
                                                  "ACCESS_FINE_LOCATION",
                                                  "ACCESS_WIFI_STATE",
                                                  "CHANGE_WIFI_STATE",
                                                  "CHANGE_WIFI_MULTICAST_STATE",
                                                  "NFC",
                                                  "MODIFY_PHONE_STATE",
                                                  "READ_PHONE_STATE",
                                                  "WRITE_SECURE_SETTINGS" };

    //URI permission bucket
    public static final String[] URI_PERMS = { "CALL_PHONE",
                                               "READ_CALENDAR",
                                               "WRITE_CALENDAR",
                                               "READ_CALL_LOG",
                                               "WRITE_CALL_LOG",
                                               "READ_USER_DICTIONARY",
                                               "WRITE_USER_DICTIONARY" };

    //Mixed URI/Method permission bucket
    public static final String[] MIXED_PERMS = { "READ_EPG_DATA",
                                                 "WRITE_EPG_DATA",
                                                 "READ_HISTORY_BOOKMARKS",
                                                 "WRITE_HISTORY_BOOKMARKS",
                                                 "CAMERA" };

    //Storage pemission bucket
    public static final String[] STORAGE_PERMS = { "WRITE_EXTERNAL_STORAGE", "READ_EXTERNAL_STORAGE" };

    /**
     * Unmarshalls Fdroid's index.xml metadata file into Jaxb objects
     * @param file metadata file
     * @return and Fdroid object, which represents the incoming metadata
     * @throws JAXBException
     */
    public static Fdroid unmarshallXML(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Fdroid.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return (Fdroid) unmarshaller.unmarshal(file);
    }

    /**
     * Marshall's the converted and parsed Jaxb objects into an xml file
     * @param data The outgoing objects to be marshalled
     * @param file the file where the datawill be saved
     * @throws JAXBException
     */
    public static void marshallXML (OutFdroid data, File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(OutFdroid.class);
        Marshaller jbMarshaller = jaxbContext.createMarshaller();

        jbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jbMarshaller.marshal(data, file);
    }

    /**
     * This method does the main work of converting the incoming applications to the more useable and simpler form.
     * @param inApplications The list of incoming applications to be analyzed
     * @return A list of simplified objects
     */
    public static List<Application> InAppstoOutApps (List<InApplication> inApplications) {
        List<Application> applicationList = new ArrayList<>();

        //Loop over each of the incoming applications
        for (InApplication inApplication : inApplications) {
            //instantiate the output type of application to be filled with the data we want
            Application application = new Application();
            OutPackage outPackage;

            application.setName(inApplication.getName());
            outPackage = getLastPackage(inApplication);
            application.setaOutPackage(outPackage);
            determinePermTypes(application);
            if (application.getaOutPackage().getTargetSdkVersion() > 22) {
                application.setQuarterMigrated(determineQuarterMigrated(inApplication));
            }

            applicationList.add(application);
        }

        calculateMigratedPerQuarter(inApplications);

        return applicationList;
    }

    /**
     * Method to produce the statistics needed for the formative study of the Fdroid corpus
     * @param applicationList List of applications who's statistics will be counted
     */
    public static void countAndPrintStatistics (List<Application> applicationList) {
        float numApps = 0;
        float numMigAPI23 = 0;
        float numMigLastYear = 0;
        float numMigSixMonths = 0;
        float numMigThreeMonths = 0;

        float usingMethodPerms = 0;
        float usingURIPerms = 0;
        float usingMixedPerms = 0;
        float usingStoragePerms = 0;

        float usingMethodPermsAPI23 = 0;
        float usingURIPermsAPI23 = 0;
        float usingMixedPermsAPI23 = 0;
        float usingStoragePermsAPI23 = 0;

        float numUpdated2016 = 0;
        float numUpdatedSixMonths = 0;
        float numUpdatedThreeMonths = 0;

        float q1API23Count = 0;
        float q2API23Count = 0;
        float q3API23Count = 0;
        float q4API23Count = 0;

        //loop over every application in the list
        for (Application application : applicationList) {
            numApps += 1;

            //this block of apps is counting which quarter the app was first migrated to API 23
            if (application.getQuarterMigrated() == 1) {
                q1API23Count += 1;
            }
            if (application.getQuarterMigrated() == 2) {
                q2API23Count += 1;
            }
            if (application.getQuarterMigrated() == 3) {
                q3API23Count += 1;
            }
            if (application.getQuarterMigrated() == 4) {
                q4API23Count += 1;
            }

            //this splits op the date field so we can extract when the app was last updated
            //the date's format before being split is YYYY-MM-DD
            String[] updatedTokens = application.getaOutPackage().getDate().split("-");

            //this cascade of if statements first checks if the app was updated in 2016
            //then it uses the ascii values of '2' and '5' corresponding to February and May respectively
            //to determine which bucket of time (3 or 6 months relative to August 2016) the last update occured.
            if (updatedTokens[0].contains("2016")) {
                numUpdated2016 += 1;
                if (updatedTokens[1].toCharArray()[1] > 50) {
                    numUpdatedSixMonths += 1;
                    if (updatedTokens[1].toCharArray()[1] > 53) {
                        numUpdatedThreeMonths += 1;
                    }
                }
            }

            //gets statistics on apps that target API 23
            if(application.getaOutPackage().getTargetSdkVersion() > 22) {
                numMigAPI23 += 1;

                //these if statements are used to count which type of permissions are being used among the apps
                //that have beein migrated to API 23
                if (application.getaOutPackage().usesMethod()) {
                    usingMethodPermsAPI23 += 1;
                }
                if (application.getaOutPackage().usesURI()) {
                    usingURIPermsAPI23 += 1;
                }
                if (application.getaOutPackage().usesMixed()) {
                    usingMixedPermsAPI23 += 1;
                }
                if (application.getaOutPackage().usesStorage()) {
                    usingStoragePermsAPI23 += 1;
                }

                //this works just like the previous date split
                String[] migratedTokens = application.getaOutPackage().getDate().split("-");

                //this works like the previous date determining if block
                if (migratedTokens[0].contains("2016")) {
                    numMigLastYear += 1;
                    if (migratedTokens[1].toCharArray()[1] > 50) {
                        numMigSixMonths += 1;
                        if (migratedTokens[1].toCharArray()[1] > 53) {
                            numMigThreeMonths += 1;
                        }
                    }
                }

            }

            //these if statements count which type of permissions are used for all of the apps, not just ones on API 23
            if (application.getaOutPackage().usesMethod()) {
                usingMethodPerms += 1;
            }
            if (application.getaOutPackage().usesURI()) {
                usingURIPerms += 1;
            }
            if (application.getaOutPackage().usesMixed()) {
                usingMixedPerms += 1;
            }
            if (application.getaOutPackage().usesStorage()) {
                usingStoragePerms += 1;
            }
        }

        //The following are all percent calculations for various statistics we need
        float percentMig23 = (numMigAPI23/numApps) * 100;
        float percentMigLastYear = (numMigLastYear/numUpdated2016) * 100;
        float percentMigSixMonths = (numMigSixMonths/numUpdatedSixMonths) * 100;
        float percentMigThreeMonths = (numMigThreeMonths/numUpdatedThreeMonths) * 100;
        float percentUsingMethodPerms = (usingMethodPerms/numApps) * 100;
        float percentUsingURIPerms = (usingURIPerms/numApps) * 100;
        float percentUsingMixedPerms = (usingMixedPerms/numApps) * 100;
        float percentUsingStoragePerms = (usingStoragePerms/numApps) * 100;

        //More percent calculations
        float percentUsingMethodPermsAPI23 = (usingMethodPermsAPI23/numMigAPI23) * 100;
        float percentUsingURIPermsAPI23 = (usingURIPermsAPI23/numMigAPI23) * 100;
        float percentUsingMixedPermsAPI23 = (usingMixedPermsAPI23/numMigAPI23) * 100;
        float percentUsingStoragePermsAPI23 = (usingStoragePermsAPI23/numMigAPI23) * 100;

        //Yet more percents
        float percentQ1API23 = (q1API23Count/numApps) * 100;
        float percentQ2API23 = (q2API23Count/numApps) * 100;
        float percentQ3API23 = (q3API23Count/numApps) * 100;
        float percentQ4API23 = (q4API23Count/numApps) * 100;

        System.out.println( "Number of apps: " + numApps +
                "\n\nNumber migrated to API23: " + numMigAPI23 +
                "\nNumber migrated in 2016: " + numMigLastYear +
                "\nNumber updated in 2016: " + numUpdated2016 +
                "\nNumber migrated in last six months: " + numMigSixMonths +
                "\nNumber updated in last six months: " + numUpdatedSixMonths +
                "\nNumber migrated in last three months: " + numMigThreeMonths +
                "\nNumber updated in last three months: " + numUpdatedThreeMonths +
                "\nNumber using method permissions: " + usingMethodPerms +
                "\nNumber using URI permissions: " + usingURIPerms +
                "\nNumber using mixed permissions: " + usingMixedPerms +
                "\nNumber using storage permissions: " + usingStoragePerms +
                "\nNumber using method permissions and API23: " + usingMethodPermsAPI23 +
                "\nNumber using URI permissions and API23: " + usingURIPermsAPI23 +
                "\nNumber using mixed permissions and API23: " + usingMixedPermsAPI23 +
                "\nNumber using storage permissions and API23: " + usingStoragePermsAPI23 +
                "\nNumber first updated to API 23 in Q4 2015: " + q4API23Count +
                "\nNumber first updated to API 23 in Q1 2016: " + q1API23Count +
                "\nNumber first updated to API 23 in Q2 2016: " + q2API23Count +
                "\nNumber first updated to API 23 in Q3 2016: " + q3API23Count);

        System.out.println( "\nPercent migrated to API23: " + percentMig23 +
                            "\nPercent migrated among updated in 2016: " + percentMigLastYear +
                            "\nPercent migrated among updated in last six months: " + percentMigSixMonths +
                            "\nPercent migrated among updated last three months: " + percentMigThreeMonths +
                            "\nPercent using method permissions: " + percentUsingMethodPerms +
                            "\nPercent using URI permissions: " + percentUsingURIPerms +
                            "\nPercent using mixed permissions: " + percentUsingMixedPerms +
                            "\nPercent using storage permissions: " + percentUsingStoragePerms +
                            "\nPercent using method permissions and API23: " + percentUsingMethodPermsAPI23 +
                            "\nPercent using URI permissions and API23: " + percentUsingURIPermsAPI23 +
                            "\nPercent using mixed permissions and API23: " + percentUsingMixedPermsAPI23 +
                            "\nPercent using storage permissions and API23: " + percentUsingStoragePermsAPI23 +
                            "\nPercent first updated to API 23 in Q4 2015: " + percentQ4API23 +
                            "\nPercent first updated to API 23 in Q1 2016: " + percentQ1API23 +
                            "\nPercent first updated to API 23 in Q2 2016: " + percentQ2API23 +
                            "\nPercent first updated to API 23 in Q3 2016: " + percentQ3API23);

    }

    /**
     * Method that sets the values of perm type for an applicatio
     * @param application Application who's perm types are being determined
     */
    private static void determinePermTypes (Application application) {
        List<String> methodPerms = permArrayToList(METHOD_PERMS);
        List<String> uriPerms = permArrayToList(URI_PERMS);
        List<String> mixedPerms = permArrayToList(MIXED_PERMS);
        List<String> storagePerms = permArrayToList(STORAGE_PERMS);

        for (String string : application.getaOutPackage().getPermissions()) {
            if (methodPerms.contains(string)) {
                application.getaOutPackage().setUsesMethod(true);
            } else if (uriPerms.contains(string)) {
                application.getaOutPackage().setUsesURI(true);
            } else if (mixedPerms.contains(string)) {
                application.getaOutPackage().setUsesMixed(true);
            } else if (storagePerms.contains(string)) {
                application.getaOutPackage().setUsesStorage(true);
            }
        }
    }

    /**
     * A small helper to convert the buckets of permission types to an ArrayList
     * @param permArray
     * @return
     */
    private static List<String> permArrayToList (String[] permArray) {
        List<String> permList = new ArrayList<>();

        for (String perm : permArray) {
            permList.add(perm);
        }

        return permList;
    }

    /**
     * This function takes in an application and determines which quarter of the year its first package targeting API23
     * entered the Fdroid marketplace
     * @param inApplication the application to be analyzed. NOTE!! This application must have a package that has been
     *                      migrated to API 23 or higher otherwise the output is garbage.
     * @return
     */
    private static int determineQuarterMigrated (InApplication inApplication) {
        int quarterMigrated;
        String date = new String();
        int counter = 0;

        //Loop over all of the packages in an application
        for (InPackage inPackage : inApplication.getInPackages()) {

            //We want the first one that is on API 23, so when we find the first one that isn't we grab the previous one
            if (inPackage.getTargetSdkVersion() < 23) {
                date = inApplication.getInPackages().get(counter - 1).getAdded();
                break;
            }
            counter += 1;
        }

        //Sometimes apps entered the marketplace on API 23, this accounts for this and grabs the oldest version
        if (date.isEmpty()) {
            date = inApplication.getInPackages().get(inApplication.getInPackages().size()-1).getAdded();
        }

        //Splits the date the package was added into an easier to analyse form
        //date initially has format YYYY-MM-DD
        String[] updatedTokens = date.split("-");

        //These ifs work similary to the ones in countAndPrintStatistics except in this case we are looking for
        //Q4(2015)[Oct-Dec or 10-12] Q1[Jan-Mar or 1-3] Q2[Apr-Jun or 4-6] and Q3[Jul-Sep or 7-9]
        //The decimal values being compared to the chars correspond to '3' or March and '6' or June
        //If the date wasn't in 2016 we count it in the Q4 2015 bucket.
        if (updatedTokens[0].contains("2016")) {
            if (updatedTokens[1].toCharArray()[1] > 51) {
                if (updatedTokens[1].toCharArray()[1] > 54) {
                    quarterMigrated = 3;
                } else {
                    quarterMigrated = 2;
                }
            } else {
                quarterMigrated = 1;
            }
        } else {
            quarterMigrated = 4;
        }

        return quarterMigrated;
    }

    private static void calculateMigratedPerQuarter (List<InApplication> inApplications) {
        float numAppsQ1 = 0;
        float numAppsQ2 = 0;
        float numAppsQ3 = 0;
        float numAppsQ4 = 0;

        float numAppsMigQ1 = 0;
        float numAppsMigQ2 = 0;
        float numAppsMigQ3 = 0;
        float numAppsMigQ4 = 0;

        for (InApplication inApplication: inApplications) {
            for (InPackage inPackage: inApplication.getInPackages()) {
                String date = inPackage.getAdded();

                String[] tokens = date.split("-");

                if (tokens[0].contains("2016")) {
                    if (tokens[1].toCharArray()[1] > 51) {
                        if (tokens[1].toCharArray()[1] > 54) {
                            numAppsQ3 += 1;
                            if (inPackage.getTargetSdkVersion() > 22) {
                                numAppsMigQ3 += 1;
                            }
                        } else {
                            numAppsQ2 += 1;
                            if (inPackage.getTargetSdkVersion() > 22) {
                                numAppsMigQ2 +=1;
                            }
                        }
                    } else {
                        numAppsQ1 += 1;
                        if (inPackage.getTargetSdkVersion() > 22) {
                            numAppsMigQ1 += 1;
                        }
                    }
                } else if (tokens[0].contains("2015")) {
                    if (tokens[1].equals("10") || tokens[1].equals("11") || tokens[1].equals("12")) {
                        numAppsQ4 += 1;
                        if (inPackage.getTargetSdkVersion() > 22) {
                            numAppsMigQ4 += 1;
                        }
                    }
                }
            }
        }



    }

    /**
     * Helper to get the most recently updated package from an app
     * @param application app to extract the package from
     * @return the extracted and converted package
     */
    private static OutPackage getLastPackage (InApplication application) {
        OutPackage aOutPackage = new OutPackage();
        InPackage inPackage = application.getInPackages().get(0);

        aOutPackage.setDate(inPackage.getAdded());
        aOutPackage.setTargetSdkVersion(inPackage.getTargetSdkVersion());
        aOutPackage.setPermissions(parsePermissions(inPackage));

        return aOutPackage;
    }

    /**
     * The incoming data has its permissions combined as one single long string so this method breaks up that string
     * and puts each individual permission in a list.
     * @param inPackage the package who's permissions are being parsed
     * @return the list of parsed permissions
     */
    private static List<String> parsePermissions(InPackage inPackage) {

        //A comma breaks up the permission string which is of the form: PERMISSION_ONE,PERMISSION_TWO,PERMISSION_THREE
        String delimiters = "[,]+";
        List<String> permissions = new ArrayList<>();

        //If the incoming permissions are not empty we parse them and build the new list
        if (!inPackage.getPermissions().isEmpty()) {
            String[] tokens = inPackage.getPermissions().get(0).split(delimiters);
            for(String token : tokens) {
                permissions.add(token);
            }
        }

        //The list returned may or may not be empty, it does not cause any problems for us down the road
        return permissions;
    }
}
