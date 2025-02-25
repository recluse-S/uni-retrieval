package com.retrieval.Server.impl;

import com.retrieval.Mapper.IRMapper;
import com.retrieval.Server.IRServer;
import com.retrieval.pojo.*;
import com.retrieval.pojo.Vo.DetailVo;
import com.retrieval.pojo.Vo.LikeVo;
import com.retrieval.pojo.Vo.UniVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IRServerImpl implements IRServer {

    @Autowired
    private IRMapper irMapper;

    @Override
    public List<MajorPro> getFirstSub() {
        List<MajorPro> proMajors = irMapper.getFirstSub();
        return proMajors;
    }

    @Override
    public List<MajorSecondary> getSecondSub(Integer id, Integer academic) {
        List<MajorSecondary> secMajors = irMapper.getSecondSub(id, academic);
        return secMajors;
    }

    @Override
    public List<MajorPrimary> getThirdlySub(Integer father) {
        List<MajorPrimary> priMajors = irMapper.getThirdlySub(father);
        return priMajors;
    }

    @Override
    public DetailVo getDetail(int userId, Integer id) {
        irMapper.incrementView(id);
        irMapper.addView(userId, id);
        DetailVo result = irMapper.getDetail(id);
//        boolean textExists = irMapper.isTextExists(id);
//        DetailVo result = irMapper.getDetailDynamic(id, textExists);
        return result;
    }

    @Override
    public DetailVo getDetail(String userId, Integer id) {
        irMapper.incrementView(id);
        irMapper.addView(userId, id);
        DetailVo result = irMapper.getDetail(id);
        return result;
    }

    @Override
    public boolean isLike(int userId, Integer formId) {
        Integer count = irMapper.isLiked(userId ,formId);
        return count > 0;
    }

    @Override
    public boolean isLike(String userId, Integer formId) {
        Integer count = irMapper.isLiked(userId ,formId);
        return count > 0;
    }

    @Override
    public void like(Integer userId, Integer id) {
        irMapper.like(userId ,id);
    }

    @Override
    public void unLike(Integer userId, Integer id) {
        irMapper.unLike(userId ,id);
    }

    @Override
    public List<LikeVo> getLike(Integer id) {
        List<LikeVo> result = irMapper.getLike(id);
        return result;
    }

    @Override
    public List<UniVo> getResult(String major, String input, String req, String location, String postName, String isUnder,
                                 String postGrade, String remark, String releaseTime, Integer pageSize) {
        List<UniVo> result = irMapper.getResult(major ,input, req, location, postName, isUnder, postGrade, remark, releaseTime, pageSize);
        return result;
    }

    @Override
    public List<UniVo> getAll() {
        List<UniVo> result = irMapper.getAll();
        return result;
    }

    @Override
    public List<String> allSelect(Integer code, Integer academic) {
        return irMapper.allSelect(code,academic);
    }

    @Override
    public List<UniRetrieval> getAllInUniversity(String institution, String year) {
        return irMapper.getAllInUniversity(institution, year);
    }

    @Override
    public User getUserInfo(int userId) {
        return irMapper.getUserInfo(userId);
    }

    @Override
    public void updatePassword(int userId, String password) {
        irMapper.updatePassword(userId, password);
    }

    @Override
    public List<UniVo> recommend(Integer day) {
        List<UniVo> result = irMapper.recommend(day);
        return result;
    }

    @Override
    public List<MajorPrimary> getAllMajors() {
        List<MajorPrimary> priMajors = irMapper.getAllMajors();
        return priMajors;
    }

    @Override
    public void deleteUsableTimes(int userId) {
        irMapper.deleteUsableTimes(userId);
    }

    @Override
    public void updateUpTime(int userId) {
        irMapper.updateUpTime(userId);
    }

    @Override
    public void updateVips(int userId) {
//        irMapper.updateVips(userId);
    }

    @Override
    public void deleteOldVips(int userId) {
        irMapper.deleteOldVips(userId);
    }

    @Override
    public void increaseVipsSucc(int userId) {
        irMapper.increaseVipsSucc(userId);
    }

    @Override
    public Integer getHotId() {
        Integer hotId = irMapper.getHotId();
        return hotId;
    }

    @Override
    public List<UniVo> getHot(Integer hotId) {
        List<UniVo> result = irMapper.getHot(hotId);
        return result;
    }

    @Override
    public List<UniVo> getHots() {
        List<UniVo> result = irMapper.getHots();
        return result;
    }

    @Override
    public void updateVipsSucc(int userId, String tradeNo) {
        irMapper.updateVipsSucc(userId, tradeNo);
    }

}
