package com.retrieval.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("major_pro")
public class MajorPro {
    private Integer id;
    private String major;
    private String code;
}
