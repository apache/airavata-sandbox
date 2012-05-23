/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.airavata.tools.workflow.monitoring.util;

import org.apache.airavata.tools.workflow.monitoring.WorkflowMonitoringException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Util {
    public static final String DATE_FORMAT = "mm/dd/yyyy hh:mm:ss";

    /**
     * This will convert a give date time string in to the its equivalent time in milliseconds. This could have been
     * done easily using DateFormat class in java, but for some reason it was not working as expected.
     * 
     * @param timeString
     *            - the format must be mm/dd/yyyy hh:mm:ss.
     * @return
     */
    public static long getTime(String timeString) throws WorkflowMonitoringException {
        String[] dateAndTime = timeString.split(" ");

        if (dateAndTime.length == 2) {
            String[] dateComponents = dateAndTime[0].split("/");
            String[] timeComponents = dateAndTime[1].split(":");

            if (dateComponents.length == 3 && timeComponents.length == 3) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(dateComponents[2]), Integer.parseInt(dateComponents[0]) - 1,
                        Integer.parseInt(dateComponents[1]), Integer.parseInt(timeComponents[0]),
                        Integer.parseInt(timeComponents[1]), Integer.parseInt(timeComponents[2]));

                return calendar.getTime().getTime();
            } else {
                throw new WorkflowMonitoringException("Date time string should be of the format " + DATE_FORMAT);
            }
        } else {
            throw new WorkflowMonitoringException("Date time string should be of the format " + DATE_FORMAT);
        }

    }

    /**
     * This method will construct a formatted date and time string from a give date. The date is given as a string which
     * represents the time in milliseconds.
     * 
     * @param time
     *            - this is the string presentation of the time in milliseconds
     * @return
     */
    public static String getFormattedDateFromLongValue(String time) {
        Date date = new Date(Long.parseLong(time));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        return (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DATE) + "/"
                + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }

    public static String extractUsername(String userDN) {
        int lastIndex;
        if (userDN.lastIndexOf("/") == userDN.indexOf("/CN")) {
            lastIndex = userDN.length();
        } else {
            lastIndex = userDN.lastIndexOf("/");
        }

        return userDN.substring(userDN.indexOf("/CN=") + 4, lastIndex);
    }

    public static String extractShortTemplateId(String templateId) {
        return templateId.substring(templateId.lastIndexOf("/") + 1);
    }
}
