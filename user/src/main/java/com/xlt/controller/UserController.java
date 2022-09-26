package com.xlt.controller;

import com.xlt.auth.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.logs.OperationLog;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.response.PageDataResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.UpdatePwdUserVo;
import com.xlt.model.vo.UserVo;
import com.xlt.service.api.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@Api("user management")
public class UserController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("login")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.UPDATE, operateDesc = "User login")
    public DataResponse<Object> userLogin(@RequestBody UserVo userVo)  throws NoSuchAlgorithmException {
        return iUserService.userLogin(userVo);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("logout")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.UPDATE, operateDesc = "User logout")
    public BasicResponse userLogout(@RequestBody UserVo userVo) {
        return iUserService.userLogout(userVo);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query user list")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.READ, operateDesc = "query user list")
    @OperatePermission(resourceName = "UserController",operateCode =OperateConstant.READ, operateDesc = "query user list")
    public DataResponse<List<UserVo>> queryUserList(@QueryParam("") UserVo userVo) {
        return iUserService.queryUserList(userVo);
    }

    @RequestMapping(value = "/query/cache", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query user info and cache")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.READ, operateDesc = "query user info and put in redis cache")
    @OperatePermission(resourceName = "UserController",operateCode =OperateConstant.READ, operateDesc = "query user info and put in redis cache")
    public DataResponse<Map<Long,UserVo>> queryUserInfoAndCache(@RequestBody Set<Long> userIdList) {
        Map<Long, UserVo> userVoMap = iUserService.fetchUserInfo(userIdList);
        return new DataResponse<>(userVoMap);
    }

    @RequestMapping(value = "/query/page/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query user list")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.READ, operateDesc = "query user paged list")
    @OperatePermission(resourceName = "UserController",operateCode =OperateConstant.READ, operateDesc = "query user list")
    public PageDataResponse<UserVo> queryUserPageList(@QueryParam("") UserVo userVo,
                                                      @PathVariable("pageSize") int pageSize,
                                                      @PathVariable("curPage") int curPage) {
        PageVo pageVo = PageVo.builder()
                .pageSize(pageSize)
                .currentPage(curPage)
                .build();
        return iUserService.queryUserPageList(userVo, pageVo);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("add user")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.CREATE, operateDesc = "add new user")
    @OperatePermission(resourceName = "UserController",operateCode =OperateConstant.CREATE, operateDesc = "add new user")
    public BasicResponse addUser(@RequestBody UserVo userVo) {
        return iUserService.addUser(userVo);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("update user")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.UPDATE, operateDesc = "update user info")
    @OperatePermission(resourceName = "UserController",operateCode =OperateConstant.UPDATE, operateDesc = "update user info")
    public BasicResponse updateUser(@RequestBody UserVo userVo) {
        return iUserService.updateUser(userVo);
    }

    @RequestMapping(value = "/update/pwd", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("update user password")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.UPDATE, operateDesc = "update user password")
    @OperatePermission(resourceName = "UserController",operateCode =OperateConstant.UPDATE, operateDesc = "update user password")
    public BasicResponse updateUserPassword(@RequestBody UpdatePwdUserVo updatePwdUserVo) {
        return iUserService.updateUserPwd(updatePwdUserVo);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("delete user")
    @OperationLog(operateModule = "UserController", operateType = OperateConstant.DELETE, operateDesc = "delete a user")
    @OperatePermission(resourceName = "UserController",operateCode =OperateConstant.DELETE, operateDesc = "delete a user")
    public BasicResponse deleteUser(@PathVariable("userId") Long userId) {
        return iUserService.deleteUserById(userId);
    }
}
