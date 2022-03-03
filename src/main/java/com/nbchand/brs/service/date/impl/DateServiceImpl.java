package com.nbchand.brs.service.date.impl;

import com.nbchand.brs.service.date.DateService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-27
 */
@Service
public class DateServiceImpl implements DateService {

    SimpleDateFormat FORMAT = new SimpleDateFormat("YYYY-MM-dd");

    @Override
    public String getTodayDateString() {
        return new String(FORMAT.format(new Date()));
    }

    @Override
    public String getDateString(Date date) {
        return new String(FORMAT.format(date));
    }

    @Override
    public Integer findDifferenceInDays(Date oldDate, Date newDate) {
        LocalDate oldOne = oldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate newOne = newDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Integer days = Math.toIntExact(ChronoUnit.DAYS.between(newOne, oldOne));
        return days;
    }

    @Override
    public Date findToDate(Date fromDate, Integer noOfDays) {
        LocalDate localFrom = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localTo = localFrom.plusDays(noOfDays);
        Date toDate = Date.from(localTo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return toDate;
    }
}
