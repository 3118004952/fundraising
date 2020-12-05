package com.cy.fundraising.service;

import com.cy.fundraising.dto.LoginResult;
import com.cy.fundraising.dto.ReadListResult;
import com.cy.fundraising.entities.GiftTblEntity;
import com.cy.fundraising.entities.ProjectTblEntity;
import com.cy.fundraising.entities.UserTblEntity;
import com.cy.fundraising.exception.*;
import com.cy.fundraising.mapper.UserMapper;
import com.cy.fundraising.util.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

@Service
public class UserService {

    @Value("${static.photo}")
    String photo;

    @Value("${static.url}")
    String url;

    @Resource
    UserMapper userMapper;
    public boolean judgePhoneExist(String phone){
        return userMapper.selectUserByPhone(phone) != 0;
    }

    public void register(UserTblEntity userTblEntity){
        userMapper.register(userTblEntity);
    }

    public LoginResult login(UserTblEntity userTblEntity) throws BaseException, UnsupportedEncodingException {
        LoginResult loginResult = userMapper.login(userTblEntity);
        if(loginResult != null){
            if("root".equals(userTblEntity.getUserPhone())){
                loginResult.setUserManage(1);
            }
            String token = TokenUtil.getToken(userTblEntity);
            loginResult.setToken(token);
            userTblEntity.setUserToken(token);
            userMapper.updateToken(userTblEntity);
            return loginResult;
        }else{
            throw new LoginException(400, "手机号码或账号密码出错！");
        }
    }

    public UserTblEntity selectUserByToken(String token){
        return userMapper.selectUserByToken(token);
    }

    public ProjectTblEntity launch(String token, ProjectTblEntity projectTblEntity) throws BaseException {
        UserTblEntity userTblEntity = userMapper.selectUserByToken(token);
        if(userTblEntity != null){
            try {
                projectTblEntity.setUserId(userTblEntity.getUserId());
                projectTblEntity.setProjectId(UUID.randomUUID().toString());
                projectTblEntity.setProjectMoneyTarget(-1);
                userMapper.launch(projectTblEntity);
                return projectTblEntity;
            }
            catch (Exception e){
                throw new LaunchException(400, "请求字段出错！");
            }
        }
        else{
            throw new LaunchException(400, "token认证失败！");
        }
    }
    public Map uploadPhoto(String token, MultipartFile file, String projectId) throws BaseException {
        UserTblEntity userTblEntity = userMapper.selectUserByToken(token);
        if(userTblEntity == null){
            throw new UploadPhotoException(400, "token认证失败！");
        }
        if (!file.isEmpty()) {
            BufferedOutputStream out = null;
            try {
                /*
                 * 这段代码执行完毕之后，图片上传到了工程的跟路径； 大家自己扩散下思维，如果我们想把图片上传到
                 * d:/files大家是否能实现呢？ 等等;
                 * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如： 1、文件路径； 2、文件名；
                 * 3、文件格式; 4、文件大小的限制;
                 */
                String strPath = this.photo + UUID.randomUUID() + '.' + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);;
                File file2 = new File(strPath);
                if (!file2.getParentFile().exists()) {
                    boolean result = file2.getParentFile().mkdirs();
                    if (!result) {
                        throw new UploadPhotoException(400, "服务器文件操作失败！");
                    }
                }
                out = new BufferedOutputStream(
                        new FileOutputStream(file2));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
                String add = this.url + strPath;
                Map<String, String> ret = new HashMap<>();
                ret.put("url", add);
                out.close();
                if(0 != userMapper.updatePhoto(userTblEntity.getUserId(), projectId,add)){
                    return ret;
                }
                else{
                    throw new UploadPhotoException(400,"项目未知！");
                }


            }
            catch (UploadPhotoException e){
                throw e;
            }
            catch (Exception e){
                throw new UploadPhotoException(400,"上传图片失败！");
            }
        }
        throw new UploadPhotoException(400,"上传的图片为空！");
    }

    public Map readList(int pageIndex , int pageSize){
        int totalPage = userMapper.projectCount();
        if(totalPage % pageSize == 0){
            totalPage /= pageSize;
        }
        else{
            totalPage /= pageSize;
            totalPage++;
        }
        List<ReadListResult> project = userMapper.readList(pageIndex, pageSize);
        Map<String, Object> res = new HashMap<>();
        res.put("totalPage", totalPage);
        res.put("project", project);
        return res;
    }

    public ProjectTblEntity readDetail(String projectId){
        return userMapper.readDetail(projectId);
    }

    @Transactional
    public Map contribution(String token, String projectId, int money) throws BaseException {
        UserTblEntity userTblEntity = userMapper.selectUserByToken(token);
        if(money <= 0)
            throw new ContributionException(400,"捐款不能为负！");
        if(userTblEntity != null){
            GiftTblEntity gift = new GiftTblEntity();
            gift.setGiftMoney(money);
            gift.setGiftTime(new Date());
            gift.setProjectId(projectId);
            gift.setUserId(userTblEntity.getUserId());
            gift.setGiftId(UUID.randomUUID().toString());
            if(userMapper.contributionUpdateProject(gift.getGiftMoney(),gift.getProjectId()) != 1){
                throw new ContributionException(400, "项目未找到或项目不在募捐状态！");
            }
            userMapper.contributionUpdateGiftTbl(gift);
            Map<String, Object> ret = new HashMap<>();
            ret.put("money", gift.getGiftMoney());
            return ret;
        }
        else{
            throw new ContributionException(400, "token认证失败！");
        }
    }
}
