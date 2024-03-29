package io.github.romadhonbyar.movie.helper;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FormatData {
    public static String minuteToFullTime(long minute) {
        return timeUnitToFullTime(minute);
    }

    @SuppressLint("DefaultLocale")
    private static String timeUnitToFullTime(long time) {
        long day = TimeUnit.MINUTES.toDays(time);
        long hour = TimeUnit.MINUTES.toHours(time) % 24;
        long minute = TimeUnit.MINUTES.toMinutes(time) % 60;
        long second = TimeUnit.MINUTES.toSeconds(time) % 60;
        if (day > 0) {
            return String.format("%dday:%02d:%02d", day, hour, minute);
        } else if (hour > 0) {
            return String.format("%d:%02d", hour, minute);
        } else if (minute > 0) {
            return String.format("%d", minute);
        } else {
            return String.format("%02d", second);
        }
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(amount));
    }

    public static String dateFormat(){
        Date nowDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(nowDate);
    }
}
