package com.fms.controller;

import com.fms.dto.ResultTO;
import com.fms.exception.CommonException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ResourceController
 *
 * @author ZhangXinJie
 * @DATE 2017/4/10
 */
@RestController
public class ResourceController {


    private static final String FILE_FORMAT = "gif,jpg,jpeg,png,bmp,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg," +
            "asf,rm,rmvb,doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,mp4";


    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ResultTO uploadResource(HttpServletRequest req,HttpServletResponse res,
                                   @RequestParam("file")CommonsMultipartFile file)throws CommonException,Exception{
        ResultTO result=new ResultTO();
        HttpSession session=req.getSession();
        if(null == session.getAttribute("loginUser")){
            throw new CommonException("用户未登录！");
        }

        try {
            if(null == file){
                throw new CommonException("请选择文件！");
            }
            System.out.println(file.getOriginalFilename());

        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }

}
