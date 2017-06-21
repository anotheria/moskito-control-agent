package org.moskito.controlagent.data.info;

import java.io.Serializable;

public class SystemInfo implements Serializable{

    private static final long serialVersionUID = 666L; // TODO : GENERATE ADEQUATE UID

    private String javaVersion;
    private String startCommand;
    private String machineName;
    private long uptime;

    public SystemInfo(String javaVersion, String startCommand, String machineName, long uptime) {
        this.javaVersion = javaVersion;
        this.startCommand = startCommand;
        this.machineName = machineName;
        this.uptime = uptime;
    }

    public String getJavaVersion() {
        return javaVersion;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public String getMachineName() {
        return machineName;
    }

    public long getUptime() {
        return uptime;
    }

}
