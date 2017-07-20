package org.moskito.controlagent.data.info;

import org.apache.commons.lang.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Singleton, that provides
 * information about monitored application.
 * Contains one public method {@link SystemInfoProvider#getSystemInfo()}
 * that returns {@link SystemInfo} instance with collected data.
 */
public class SystemInfoProvider {

    /**
     * Holds SystemInfoProvider instance
     */
    private static final SystemInfoProvider INSTANCE = new SystemInfoProvider();

    /**
     * Most of information fields is not changing
     * since app launch. This var holds {@link SystemInfo}
     * instance with this information.
     * Later copy of this object with
     */
    private SystemInfo initialInfo;

    /**
     * Constructor, that builds initial info
     * object, filling java version, app launch command and
     * machine name.
     */
    private SystemInfoProvider(){

        initialInfo = new SystemInfo();

        initialInfo.setJavaVersion(
                System.getProperty("java.version")
        );
        initialInfo.setMachineName(
                getMachineName()
        );
        initialInfo.setStartCommand(
                getStartCommand()
        );

    }

    public static SystemInfoProvider getInstance(){
        return INSTANCE;
    }

    /**
     * Returns {@link SystemInfo} object,
     * that contains data about this monitored app.
     *
     * Builds new {@link SystemInfo} from existing one:
     * this class singleton object has field with
     * {@link SystemInfo} instance, that already
     * contains some data occurred on class initialization.
     * Actually, only uptime updates here.
     *
     * @return {@link SystemInfo} filled with corresponding data
     */
    public SystemInfo getSystemInfo(){

        SystemInfo info = new SystemInfo();

        info.setJavaVersion(initialInfo.getJavaVersion());
        info.setMachineName(initialInfo.getMachineName());
        info.setStartCommand(initialInfo.getStartCommand());
        info.setUptime(getUptime());

        return info;

    }

    /**
     * Returns start command of this application.
     * Some data may not to be collected due
     * security settings and jvm  version.
     * TODO : IMPROVE THIS METHOD
     * @return start command of this app
     */
    private String getStartCommand(){

        String command;
        String classpath;
        String arguments;

        try{

            try { // trying to get start command using sun.java.command
                  // Work of this code may not to be guarantee on OpenJDK
                command = System.getProperty("sun.java.command");
            }
            catch (SecurityException| IllegalArgumentException e){
                command = "";
            }

            classpath = "-cp " + System.getProperty("java.class.path");

            // Getting start command arguments and joining it to single string
            arguments = StringUtils.join(
                            ManagementFactory.getRuntimeMXBean().getInputArguments(),
                            " "
                        );

        }
        catch (SecurityException| IllegalArgumentException e){
            return "Failed to get run command info due " + e.getClass().getName()
                    + ": " + e.getMessage();
        }

        return "java " + command + " " + classpath + " " + arguments;
    }

    /**
     * Returns machine name, where this app is launched
     * @return current machine name
     */
    private String getMachineName()
    {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if(env.containsKey("MACHINENAME"))
            return env.get("MACHINENAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else {

            try {
                return InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException | SecurityException e) {
                return "Unknown Computer";
            }

        }
    }

    /**
     * Returns string with
     * this application uptime in format:
     *
     * {DAYS},{HOURS},{MINUTES},{SECONDS}
     *
     * All numbers do not contain leading zeros
     *
     * @return string that represents this app uptime
     */
    private String getUptime(){

        // Retrieving milliseconds timestamp from java management framework
        long uptimeTimestamp = ManagementFactory.getRuntimeMXBean().getUptime();
        StringBuilder uptime = new StringBuilder();

        // builtin DateTime formatters do not
        // used here, because they return day of the year.
        // this may cause zeroing of days in the time string
        final long SECOND = 1000;
        final long MINUTE = SECOND * 60;
        final long HOUR = MINUTE * 60;
        final long DAY = HOUR * 24;

        uptime.append(
                Long.valueOf(uptimeTimestamp / DAY).toString()
        ).append(',');
        uptimeTimestamp = uptimeTimestamp % DAY;

        uptime.append(
                Long.valueOf(uptimeTimestamp / HOUR).toString()
        ).append(',');
        uptimeTimestamp = uptimeTimestamp % HOUR;

        uptime.append(
                Long.valueOf(uptimeTimestamp / MINUTE).toString()
        ).append(',');
        uptimeTimestamp = uptimeTimestamp % MINUTE;

        uptime.append(
                Long.valueOf(uptimeTimestamp / SECOND).toString()
        );

        return uptime.toString();

    }

}
