package org.moskito.controlagent.info;

import org.junit.Test;
import org.moskito.controlagent.data.info.SystemInfo;
import org.moskito.controlagent.data.info.SystemInfoProvider;

import static org.junit.Assert.*;

public class SystemInfoTest {

    private SystemInfo getInfo(){
        return SystemInfoProvider.getInstance().getSystemInfo();
    }

    private int getUptimeStringSeconds(String uptime){
        String[] uptimeComponents = uptime.split(",");
        return Integer.valueOf(uptimeComponents[3]);
    }

    @Test
    public void testMachineName(){

        String machineName = getInfo().getMachineName();

        // "Unknown Computer" means name not found.
        assertNotEquals("Unknown Computer", machineName);
        System.out.println("Machine name : " + machineName);

    }

    @Test
    public void testUptime() throws InterruptedException {

        final String uptimeRegexpPattern = "^\\d+,\\d+,\\d+,\\d+$";

        String firstUptime = getInfo().getUptime();
        Thread.sleep(1000);
        String secondUptime = getInfo().getUptime();

        // Testing uptime string format
        // Expected « {DAY},{HOUR},{MINUTE},{SECOND} »
        assertTrue(firstUptime.matches(uptimeRegexpPattern));
        // There was one second pause between first and second uptime get,
        // so they must not be equal
        assertNotEquals(firstUptime, secondUptime);
        // Asserting that uptimes differ at least on one second
        assertTrue(
                getUptimeStringSeconds(secondUptime) - getUptimeStringSeconds(firstUptime) >= 1
        );

        System.out.println("First uptime : " + firstUptime + ", second uptime after 1s sleep : " + secondUptime);

    }

}
