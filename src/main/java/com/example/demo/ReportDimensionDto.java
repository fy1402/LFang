package com.example.demo;

import java.util.List;

/**
 * Created by i-feng on 2018/7/5.
 */
public class ReportDimensionDto {

    private String reportName;		//报表名称
    private String xDimension;		//x轴维度
    private String[] yDimension;	//y轴维度
    private String titile;			//标题
    private List parkingPoint;		//停车点（资源）

    public String getReportName() {
        return reportName;
    }
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
    public String getxDimension() {
        return xDimension;
    }
    public void setxDimension(String xDimension) {
        this.xDimension = xDimension;
    }
    public String[] getyDimension() {
        return yDimension;
    }
    public void setyDimension(String[] yDimension) {
        this.yDimension = yDimension;
    }
    public String getTitile() {
        return titile;
    }
    public void setTitile(String titile) {
        this.titile = titile;
    }
    public List getParkingPoint() {
        return parkingPoint;
    }
    public void setParkingPoint(List parkingPoint) {
        this.parkingPoint = parkingPoint;
    }

}
