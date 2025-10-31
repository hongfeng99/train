package com.ikun.train.business.req;

import com.ikun.train.common.req.PageReq;

public class TrainCarriageQueryReq extends PageReq {

    private String trainCode;



    public String getTrainCode() {
        return trainCode;
    }

    @Override
    public String toString() {
        return "TrainCarriageQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

}
