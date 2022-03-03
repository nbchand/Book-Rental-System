package com.nbchand.brs.service.date;

import java.util.Date;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
public interface DateService {
    String getTodayDateString();
    String getDateString(Date date);
    Integer findDifferenceInDays(Date date1, Date date2);
    Date findToDate(Date fromDate, Integer noOfDays);
}
