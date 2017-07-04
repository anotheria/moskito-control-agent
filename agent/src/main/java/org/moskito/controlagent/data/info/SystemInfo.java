package org.moskito.controlagent.data.info;

import java.io.Serializable;

/**
 * Contains information about monitored app  and its environment.
 */
public class SystemInfo implements Serializable{

    private static final long serialVersionUID = -4103365897826888898L;

    /**
     * Version of java
     */
    private String javaVersion;
    /**
     * Run command of monitored app
     */
    private String startCommand;
    /**
     * Name of machine, where monitored app is launched
     */
    private String machineName;
    /**
     * Monitored app uptime
     */
    private String uptime;

    SystemInfo(){}

    public String getJavaVersion() {
        return javaVersion;
    }

    public String getStartCommand() {
        return startCommand;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getUptime() {
        return uptime;
    }

    void setJavaVersion(String javaVersion) {
        this.javaVersion = javaVersion;
    }

    void setStartCommand(String startCommand) {
        this.startCommand = startCommand;
    }

    void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String toString(){
        return "{" +
                    "javaVersion: "  + javaVersion  + "," +
                    "startCommand: " + startCommand + "," +
                    "machineName: "  + machineName  + "," +
                    "uptime: "       + uptime +
               "}";
    }

}
