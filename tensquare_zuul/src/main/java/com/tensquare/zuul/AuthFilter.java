package com.tensquare.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证过滤器
 */
@Component
public class AuthFilter extends ZuulFilter{
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Object run() throws ZuulException {
        //1.获取token信息

        RequestContext requestContext = RequestContext.getCurrentContext();

        HttpServletRequest request = requestContext.getRequest();

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        //把管理员登录请求忽略
        if(request.getRequestURI().contains("/admin/login")){
            return null;
        }

        String token = request.getHeader("token");
        System.out.println("token===="+token);
        if(token!=null){

            Claims body = jwtUtil.parseJWT(token);

            if(body!=null){
                if(body.get("roles").equals("admin")){

                    //2.合法，继续访问微服务
                    return null;
                }
            }


        }

        //3.不合法，拒绝访问
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseBody("404拒绝访问");
        requestContext.getResponse().setContentType("text/html;charset=utf-8");

        return null;
    }
}
