package com.frontier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static final int MILLIS_IN_DAY = 86400000;
    private static Scanner scan = new Scanner(System.in);
    private static Long before180;
    private static Long todayLong;

    public static void main(String[] args) {
        List<Date> dateList = new ArrayList<Date>();
        List<Long> longList = new ArrayList<Long>();

        Date today = new Date();
        before180 = before180(today);
        todayLong = dateToLong(today);
        System.out.println("Today is: " + today);
        System.out.println("To complete the entry, enter \"end\" or press Enter on an empty line.");
        while (true) {
            System.out.println("Enter a date \"day-month-year\": ");
            String s = scan.nextLine();
            Date d = null;
            if (s.equals("end") || s.equals("")) {
                break;
            }
            try {
                d = new SimpleDateFormat("d-M-y").parse(s);
            } catch (ParseException e) {
                System.out.println("********** The wrong date and time format. The value is not added. Please try again. **********");
            }
            if (d == null) {
                continue;
            }
            dateList.add(d);
            System.out.println("Successfully added: " + d);
        }

        Calendar cal = Calendar.getInstance();
        for (Date aDateList : dateList) {
            cal.setTime(aDateList);
            longList.add(cal.getTimeInMillis());
        }

        Collections.sort(longList);
        System.out.println("Added date: ");
        printDate(longList);

        List<Long> requiredList = getListOfRequiredLong(longList);

        int sum = 0;
        for (int i = 0; i < requiredList.size(); i = i + 2) {
            sum += calcDays(requiredList.get(i), requiredList.get(i + 1));
        }

        System.out.println("\nRequired date: ");
        printDate(requiredList);

        System.out.println("\nSum of days: " + sum + ".");
        int legalDays;
        Date date = new Date();
        if (sum < 90) {
            legalDays = 90 - sum;

            Calendar calendar = Calendar.getInstance();
            setTimeZero(calendar);
            calendar.set(Calendar.DAY_OF_YEAR, -legalDays);
            date.setTime(calendar.getTimeInMillis());
            System.out.println("\nThe person is in the country legally and he has " + legalDays + " days to be there. " +
                    "Date of end of term: " + date + ".");
        } else {
            if (sum >= 90) {
                legalDays = sum - 90;

                Calendar calendar = Calendar.getInstance();
                setTimeZero(calendar);
                calendar.set(Calendar.DAY_OF_YEAR, -legalDays);
                date.setTime(calendar.getTimeInMillis());
                System.out.println("\n!!! The person is illegally in the country for " + legalDays + " days. The tenure ended " + date + ". Person must be deported.");
            }
        }
    }

    public static int calcDays(long in, long out) {
        return (int) ((out - in) / MILLIS_IN_DAY);
    }

    public static long before180(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        setTimeZero(cal);
        cal.add(Calendar.DAY_OF_YEAR, -180);
        return cal.getTimeInMillis();
    }

    public static long dateToLong(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        setTimeZero(cal);
        return cal.getTimeInMillis();
    }

    public static List<Long> getListOfRequiredLong(List<Long> list) {
        List<Long> longs = new ArrayList<Long>();
        boolean needAdd = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) >= before180) {
                if (i % 2 != 0 && needAdd) {
                    longs.add(before180);
                    needAdd = false;
                }
                if (todayLong >= list.get(i)) {
                    longs.add(list.get(i));
                    needAdd = false;
                }
            }
        }

        if (longs.size() % 2 != 0) {
            longs.add(todayLong);
        }

        Collections.sort(longs);
        return longs;
    }

    public static void printDate(List<Long> list) {
        for (Long l : list) {
            Date d = new Date();
            d.setTime(l);
            System.out.println(d);
        }

    }

    public static void setTimeZero(Calendar c) {
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }
}
