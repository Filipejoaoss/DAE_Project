package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDateTime implements Serializable {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public MyDateTime(){
        year = 0;
        month = 0;
        day = 0;
        hour = 0;
        minute = 0;
        second = 0;
    }

    public MyDateTime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public MyDateTime(boolean justTime, int hourDay, int minuteMonth, int secondYear){
        super();
        if(justTime){
            hour = hourDay;
            minute = minuteMonth;
            second = secondYear;
        }else{
            day = hourDay;
            month = minuteMonth;
            year = secondYear;
        }
    }

    public MyDateTime(String str){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        if(m.find()){
            hour = Integer.parseInt(m.group());
        }
        if(m.find()){
            minute = Integer.parseInt(m.group());
        }
        if(m.find()){
            second = Integer.parseInt(m.group());
        }
        if(m.find()){
            day = Integer.parseInt(m.group());
        }
        if(m.find()){
            month = Integer.parseInt(m.group());
        }
        if(m.find()){
            year = Integer.parseInt(m.group());
        }
    }



    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getDate(){
        return day + "/" + month + '/' +year;
    }

    public String getTime(){
        return hour + ":" + minute + ":" +second;
    }

    public String getDateTime(){
        return this.toString();
    }

    public static MyDateTime now(){
        var ldt = LocalDateTime.now();
        return new MyDateTime(ldt.getYear(),ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
    }

    public static boolean todayInBetween(MyDateTime startDate, MyDateTime endDate) {
        var now = MyDateTime.now();
        int startNow = now.weightedString().compareTo(startDate.weightedString());
        int endNow = now.weightedString().compareTo(endDate.weightedString());
        return startNow > 0 && endNow < 0;
    }

    public boolean equalsDate(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDateTime that = (MyDateTime) o;
        return year == that.year && month == that.month && day == that.day;
    }

    public boolean equalsTime(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDateTime that = (MyDateTime) o;
        return hour == that.hour && minute == that.minute && second == that.second;
    }

    private String weightedString(){
        return String.format("%04d", year) + ":" + String.format("%02d", month) + ":" + String.format("%02d", day) + " - " + String.format("%02d", hour) + "/" + String.format("%02d", minute) + '/' + String.format("%02d", second);
    }

    @Override
    public String toString() {
        return hour + ":" + minute + ":" +second + " - " + day + "/" + month + '/' +year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyDateTime that = (MyDateTime) o;
        return year == that.year && month == that.month && day == that.day && hour == that.hour && minute == that.minute && second == that.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, hour, minute, second);
    }
}
