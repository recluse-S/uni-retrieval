package com.retrieval.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("like")
public class Like {
    Integer id;
    Integer userId;
    String formId;
    LocalDateTime createTime;
}
