package com.echo.inteceptor;

import com.echo.dataobject.ShopDO;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/*
店铺的拦截
不是该店铺下的不能进行操作
 */
public class ShopPermissionInteceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session获取当前店铺
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");

        //获取session中的shopList
        List<ShopDO> shopList = (List<ShopDO>) request.getSession().getAttribute("shopList");

        if (currentShop != null && shopList != null) {
            for (ShopDO shop : shopList) {
                if (shop.getShopId() == currentShop.getShopId()) {
                    return true;
                }
            }
        }

        return false;
    }
}
