package com.retrieval.pojo;

import lombok.Data;

@Data
public class SelectParam {
    private String major;
    private String input;
    private String req;
    private String location;
    private String postName;
    private String isUnder;
    private String postGrade;
    private String remark;
    private String releaseTime;
    private Integer pageNum;
    private Integer pageSize;
}
