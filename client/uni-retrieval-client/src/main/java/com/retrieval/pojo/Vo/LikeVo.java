package com.retrieval.pojo.Vo;

import lombok.Data;

import java.util.Date;

@Data
public class LikeVo {

    /**
     * 点赞中间表
     */
    private Integer id;
    private Integer userId;
    private Integer formId;  //以招考信息作为主键id
    private Date createdTime;

    /**
     * 招考信息
     */
    private String institution;
    private String postName;
    private String postStatement;
    private String reqEducation;
    private String reqDegree;
    private String reqMajorGraduate;
}
