package com.retrieval.Mapper;

import com.retrieval.pojo.*;
import com.retrieval.pojo.Vo.DetailVo;
import com.retrieval.pojo.Vo.LikeVo;
import com.retrieval.pojo.Vo.UniVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IRMapper {

//    @Select("SELECT major, id FROM major_pro;")
    List<MajorPro> getFirstSub();

    List<MajorSecondary> getSecondSub(@Param("id") Integer id, @Param("academic") Integer academic);

    List<MajorPrimary> getThirdlySub(Integer father);

    List<UniVo> getResult(@Param("major") String major,
                          @Param("input") String input,
                          @Param("req") String req,
                          @Param("location") String location,
                          @Param("postName") String postName,
                          @Param("isUnder") String isUnder,
                          @Param("postGrade") String postGrade,
                          @Param("remark") String remark,
                          @Param("releaseTime") String releaseTime,
                          @Param("pageSize") Integer pageSize
    );

    DetailVo getDetail(Integer id);

//    Long selectCount(LambdaQueryWrapper<Like> queryWrapper);

    Integer isLiked(@Param("userId")Integer userId, @Param("formId")Integer formId);

    Integer isLiked(@Param("userId")String userId, @Param("formId")Integer formId);

    Integer getUserId(String name);

    void like(@Param("userId")Integer userId, @Param("id")Integer id);

    void unLike(@Param("userId")Integer userId, @Param("id")Integer id);

    List<LikeVo> getLike(Integer id);

    List<UniVo> getAll();

    List<String> allSelect(@Param("code") Integer code,@Param("academic") Integer academic);

    List<UniRetrieval> getAllInUniversity(@Param("institution") String institution, @Param("releaseTime") String year);

    User getUserInfo(Integer userId);

    void updatePassword(@Param("userId")Integer userId, @Param("password")String password);

    List<UniVo> recommend(@Param("day")Integer day);

    void incrementView(Integer id);

    boolean isTextExists(Integer id);

    DetailVo getDetailDynamic(@Param("id")Integer id, @Param("textExists")boolean textExists);

    void addView(@Param("userId")Integer userId, @Param("id")Integer id);

    void addView(@Param("userId")String userId, @Param("id")Integer id);

    List<MajorPrimary> getAllMajors();

//    @MapKey("id")
//    Map<String, Object> getOpenIdByToken(@Param("token") String token);

    String getOpenIdByToken(@Param("token") String token);

    void deleteUsableTimes(Integer userId);

    void updateUpTime(Integer userId);

//    void updateVips(Integer userId);

    void increaseVips(@Param("userId")Integer userId, @Param("tradeNo")String tradeNo);

    void deleteOldVips(Integer userId);

    void increaseVipsSucc(Integer userId);

    Integer getHotId();

    List<UniVo> getHot(Integer hotId);

    List<UniVo> getHots();

    Integer getAmount();

    void updateVipsSucc(@Param("userId")Integer userId, @Param("tradeNo")String tradeNo);
}
