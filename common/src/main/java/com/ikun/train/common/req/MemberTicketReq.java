package com.ikun.train.common.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class MemberTicketReq {

    /**
     * id
     */
    private Long id;

    /**
     * 会员id
     */
    @NotNull(message = "【会员id】不能为空")
    private Long memberId;

    /**
     * 乘客id
     */
    @NotNull(message = "【乘客id】不能为空")
    private Long passengerId;

    /**
     * 乘客姓名
     */
    private String passengerName;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "【日期】不能为空")
    private Date date;

    /**
     * 车次编号
     */
    @NotBlank(message = "【车次编号】不能为空")
    private String trainCode;

    /**
     * 箱序
     */
    @NotNull(message = "【箱序】不能为空")
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
    @NotBlank(message = "【排号】不能为空")
    private String row;

    /**
     * 列号|枚举[SeatColEnum]
     */
    @NotBlank(message = "【列号】不能为空")
    private String col;

    /**
     * 出发站
     */
    @NotBlank(message = "【出发站】不能为空")
    private String start;

    /**
     * 出发时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【出发时间】不能为空")
    private Date startTime;

    /**
     * 到达站
     */
    @NotBlank(message = "【到达站】不能为空")
    private String end;

    /**
     * 到站时间
     */
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
    @NotNull(message = "【到站时间】不能为空")
    private Date endTime;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    @NotBlank(message = "【座位类型】不能为空")
    private String seatType;

    /**
     * 新增时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public @NotBlank(message = "【列号】不能为空") String getCol() {
        return col;
    }

    public void setCol(@NotBlank(message = "【列号】不能为空") String col) {
        this.col = col;
    }

    public @NotBlank(message = "【排号】不能为空") String getRow() {
        return row;
    }

    public void setRow(@NotBlank(message = "【排号】不能为空") String row) {
        this.row = row;
    }

    public @NotNull(message = "【日期】不能为空") Date getDate() {
        return date;
    }

    public void setDate(@NotNull(message = "【日期】不能为空") Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }




    public @NotBlank(message = "【出发站】不能为空") String getStart() {
        return start;
    }

    public void setStart(@NotBlank(message = "【出发站】不能为空") String start) {
        this.start = start;
    }

    public @NotBlank(message = "【到达站】不能为空") String getEnd() {
        return end;
    }

    public void setEnd(@NotBlank(message = "【到达站】不能为空") String end) {
        this.end = end;
    }


    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public Integer getCarriageIndex() {
        return carriageIndex;
    }

    public void setCarriageIndex(Integer carriageIndex) {
        this.carriageIndex = carriageIndex;
    }




    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }




    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MemberTicketReq{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", passengerId=" + passengerId +
                ", passengerName='" + passengerName + '\'' +
                ", date=" + date +
                ", trainCode='" + trainCode + '\'' +
                ", carriageIndex=" + carriageIndex +
                ", row='" + row + '\'' +
                ", col='" + col + '\'' +
                ", start='" + start + '\'' +
                ", startTime=" + startTime +
                ", end='" + end + '\'' +
                ", endTime=" + endTime +
                ", seatType='" + seatType + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
