package com.nbchand.brs.service.date.impl;

import com.nbchand.brs.service.date.DateService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
}
