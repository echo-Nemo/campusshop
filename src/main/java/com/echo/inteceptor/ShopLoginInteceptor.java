package com.echo.inteceptor;

import com.echo.dataobject.OwnerDO;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/*
店铺管理系统的验证拦截
只有先登入
 */
public class ShopLoginInteceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中获取用户的幸喜
        Object userObj = request.getSession().getAttribute("user");

        if (userObj != null) {
            //将userObj对象转化为PersonInfo对象
            OwnerDO personInfo = (OwnerDO) userObj;
            if (personInfo != null && personInfo.getUserId() != null && personInfo.getEnableStatus() == 1) {
                return true;
            }
        }

        // 若不满足登录验证，则直接跳转到帐号登录页面
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath() + "/localurl/login?usertype=2','_self')");
        out.println("</script>");
        out.println("</html>");
        return false;
    }
}
