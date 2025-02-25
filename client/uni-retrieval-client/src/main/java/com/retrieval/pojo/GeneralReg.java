package com.retrieval.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("general_regulations")
public class GeneralReg {
    private Integer id;
    private String title;
    private String school;
    private String text;
    private LocalDateTime createdTime;
    private String releaseTime;
    private String link;
    private Integer hot;
}
