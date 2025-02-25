package com.retrieval.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("form")
public class UniRetrieval implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String institution;
    private String postName;
    private String postNature;
    private String postGrade;
    private String postStatement;
    private String postDescription;
    private String postCategory;
    private String postCode;
    private String postNumber;
    private String postClassification;
    private String fundsType;
    private String insType;
    private String insDepartment;
    private String goverType;
    private Integer recruitingNum;
    private String reqEducation;
    private String reqDegree;
    private String reqMajorGraduate;
    private String reqMajorUndergraduate;
    private String reqMajorJunior;
    private String reqDirection;
    private String reqElse;
    private String examForm;
    private String examRatio;
    private String examTypeRatio;
    private String examInterview;
    private String examWritten;
    private String serviceLength;
    private String contactInfo;
    private String contactPhone;
    private String contactEmail;
    private String contactPerson;
    private String workplace;
    private String reqAge;
    private String reqForeign;
    private String workTask;
    private String remark;
    private String createdTime;
    private String releaseTime;
    private String location;
    private String genRegulation;
    private String natureUnit;
    private String educationLevel;
    private Integer view;
}
