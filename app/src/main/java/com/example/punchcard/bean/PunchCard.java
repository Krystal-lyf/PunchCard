package com.example.punchcard.bean;

import android.util.Log;
import android.widget.Toast;

import com.example.punchcard.ClockActivity;

/**
 * 基础表的bean类
 */
public class PunchCard {
    private String id;
    private String item;        //打卡项目
    private Integer times;      //打卡次数
    private Integer activity;  //0：未打卡，1：已经打卡
    private String date;       //保存记录的时间
    private Integer calID;      //手机日历id
    private Integer syear;      //开始年
    private Integer smonth;      //开始月
    private Integer sday;      //开始日
    private Integer shour;      //开始时
    private Integer sminute;      //开始分
    private long eventId;      //每次插入事件id
    private Integer ts;        //提醒天数
    private Integer iconId;

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public Integer getTs() {
        return ts;
    }

    public void setTs(Integer ts) {
        this.ts = ts;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Integer getCalID() {
        return calID;
    }

    public void setCalID(Integer calID) {
        this.calID = calID;
    }

    public Integer getSyear() {
        return syear;
    }

    public void setSyear(Integer syear) {
        this.syear = syear;
    }

    public Integer getSmonth() {
        return smonth;
    }

    public void setSmonth(Integer smonth) {
        this.smonth = smonth;
    }

    public Integer getSday() {
        return sday;
    }

    public void setSday(Integer sday) {
        this.sday = sday;
    }

    public Integer getShour() {
        return shour;
    }

    public void setShour(Integer shour) {
        this.shour = shour;
    }

    public Integer getSminute() {
        return sminute;
    }

    public void setSminute(Integer sminute) {
        this.sminute = sminute;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
