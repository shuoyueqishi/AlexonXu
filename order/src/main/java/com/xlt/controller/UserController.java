package com.xlt.controller;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.vo.UserVo;
import com.xlt.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    BasicResponse addUser(@RequestBody UserVo userVo) {
        return userService.addUser(userVo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    BasicResponse deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
    BasicResponse updateUser(@RequestBody UserVo userVo) {
        return userService.updateUser(userVo);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.GET, produces = "application/json")
    BasicResponse queryUserList(@QueryParam("userVo") UserVo userVo) {
        return userService.queryUserList(userVo);
    }
}

