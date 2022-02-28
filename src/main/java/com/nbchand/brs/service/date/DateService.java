package com.nbchand.brs.service.date;

import java.util.Date;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
public interface DateService {
    public String getTodayDateString();

    public String getDateString(Date date);
}
