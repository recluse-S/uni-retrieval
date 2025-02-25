package com.retrieval.Server;

import com.retrieval.pojo.*;
import com.retrieval.pojo.Vo.DetailVo;
import com.retrieval.pojo.Vo.LikeVo;
import com.retrieval.pojo.Vo.UniVo;

import java.util.List;

public interface IRServer {

    List<MajorPro> getFirstSub();

    List<MajorSecondary> getSecondSub(Integer id, Integer academic);

    List<MajorPrimary> getThirdlySub(Integer father);

    DetailVo getDetail(int userId, Integer id);

    DetailVo getDetail(String userId, Integer id);

    boolean isLike(int userId, Integer id);

    boolean isLike(String userId, Integer id);

//    Integer getUserIdByUsername(String name);

    void like(Integer userId, Integer id);

    void unLike(Integer userId, Integer id);

    List<LikeVo> getLike(Integer id);

    List<UniVo> getResult(String major, String input, String req, String location, String postName, String isUnder,
                          String postGrade, String remark, String releaseTime, Integer pageSize);

    List<UniVo> getAll();

    List<String> allSelect(Integer code,Integer academic);

    List<UniRetrieval> getAllInUniversity(String institution,String year);

    User getUserInfo(int userId);

    void updatePassword(int userId, String password);

    List<UniVo> recommend(Integer day);

    List<MajorPrimary> getAllMajors();

    void deleteUsableTimes(int userId);

    void updateUpTime(int userId);

    void updateVips(int userId);

    void deleteOldVips(int userId);

    void increaseVipsSucc(int userId);

    Integer getHotId();

    List<UniVo> getHot(Integer hotId);

    List<UniVo> getHots();

    void updateVipsSucc(int userId, String tradeNo);
}
