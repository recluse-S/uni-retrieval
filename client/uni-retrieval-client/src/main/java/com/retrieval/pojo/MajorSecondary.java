package com.retrieval.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("major_secondary")
public class MajorSecondary {
    private Integer id;
    private String major;
    private String code;
    private Integer academic;
    private Integer father;
}
