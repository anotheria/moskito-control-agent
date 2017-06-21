package org.moskito.controlagent.data.info;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Map;

public class SystemInfoProvider {

    private static final SystemInfoProvider INSTANCE = new SystemInfoProvider();

    private SystemInfoProvider(){}

    public static SystemInfoProvider getInstance(){
        return INSTANCE;
    }

    public SystemInfo getSystemInfo(){

        String javaVersion = System.getProperty("java.version");
        String machineName = getComputerName();
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();

        return new SystemInfo(
                javaVersion, getRunCommand(), machineName, uptime
        );
    }

    private String getRunCommand(){
        return "java -Dsosi=pisos";
    }

    private String getComputerName()
    {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }

}
