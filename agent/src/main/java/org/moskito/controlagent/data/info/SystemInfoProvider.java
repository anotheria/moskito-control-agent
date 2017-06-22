package org.moskito.controlagent.data.info;

import org.apache.commons.lang.StringUtils;

import java.lang.management.ManagementFactory;
import java.util.Map;

/**
 * TODO : COMMENT THIS CLASS
 */
public class SystemInfoProvider {

    private static final SystemInfoProvider INSTANCE = new SystemInfoProvider();

    private SystemInfo initialInfo;

    private SystemInfoProvider(){
        initialInfo = buildInitialSystemInfo();
    }

    public static SystemInfoProvider getInstance(){
        return INSTANCE;
    }

    private SystemInfo buildInitialSystemInfo(){

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

        return initialInfo;

    }

    public SystemInfo getSystemInfo(){
        SystemInfo info = new SystemInfo(initialInfo);
        info.setUptime(getUptime());
        return info;
    }

    private String getStartCommand(){

        String command;
        String classpath;
        String arguments;

        try{

            try {
                command = System.getProperty("sun.java.command");
            }
            catch (SecurityException| IllegalArgumentException e){
                command = "";
            }

            classpath = "-cp " + System.getProperty("java.class.path");
            arguments = StringUtils.join(
                            ManagementFactory.getRuntimeMXBean().getInputArguments(),
                            " "
                        );
        }
        catch (SecurityException| IllegalArgumentException e){
            return "Failed to get run command info due " + e.getClass().getName()
                    + ": " + e.getMessage();
        }

        return command + " " + classpath + " " + arguments;
    }

    private String getMachineName()
    {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if(env.containsKey("MACHINENAME"))
            return env.get("MACHINENAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }

    private String getUptime(){

        long uptimeTimestamp = ManagementFactory.getRuntimeMXBean().getUptime();
        StringBuilder uptime = new StringBuilder();

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
