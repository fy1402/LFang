package com.example.demo;

import java.util.Date;

/**
 * Created by i-feng on 2018/7/3.
 */
public class Card{

//    private static final long serialVersionUID = -1393277892650455805L;

    private Long id;

    private String name;

    /*部门*/
    private String department;

    /*打卡日期*/
    private String dateStr;

    /*打卡开始时间*/
    private Date startDate;
    private String startDateStr;

    /*打卡结束时间*/
    private Date endDate;
    private String endDateStr;

    /*打卡次数*/
    private String count;

    /*请假类别（研发／非研发）*/
    private String leaveType;

    /*请假时长*/
    private float timeLength;

    /*描述*/
    private String description;

    /*请假（补休）开始时间*/
    private String timeOutStart;

    /*请假结束时间*/
    private String timeOutEnd;

    /*请假（补休）开始日期*/
    private String timeOutDateStart;

    /*请假结束日期*/
    private String timeOutDateEnd;

    /*请假状态*/
    private String timeOutStatus;

    public String getTimeOutStatus() {
        return timeOutStatus;
    }

    public void setTimeOutStatus(String timeOutStatus) {
        this.timeOutStatus = timeOutStatus;
    }

    public String getTimeOutEnd() {
        return timeOutEnd;
    }

    public void setTimeOutEnd(String timeOutEnd) {
        this.timeOutEnd = timeOutEnd;
    }

    public String getTimeOutDateStart() {
        return timeOutDateStart;
    }

    public void setTimeOutDateStart(String timeOutDateStart) {
        this.timeOutDateStart = timeOutDateStart;
    }

    public String getTimeOutDateEnd() {
        return timeOutDateEnd;
    }

    public void setTimeOutDateEnd(String timeOutDateEnd) {
        this.timeOutDateEnd = timeOutDateEnd;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public float getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(float timeLength) {
        this.timeLength = timeLength;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeOutStart() {
        return timeOutStart;
    }

    public void setTimeOutStart(String timeOutStart) {
        this.timeOutStart = timeOutStart;
    }

    @Override
    public String toString() {
        return "\n" +
                this.getDepartment().toString() + "\n" +
                this.getName() + "\n" +
                this.getStartDateStr() + "\n" +
                this.getEndDateStr() + "\n" +
                this.getTimeOutDateStart() + "\n" +
                this.getTimeOutEnd() + "\n" +
                this.getDescription() + "\n" +
                this.getStartDate() + "\n" +
                this.getEndDate() + "\n" +
                this.getDateStr() + "\n" +
                "______________";
    }
}
