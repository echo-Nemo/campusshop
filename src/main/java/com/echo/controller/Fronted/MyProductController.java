package com.echo.controller.Fronted;

import com.alipay.api.domain.Product;
import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.ProductDO;
import com.echo.dataobject.UserProductMapDO;
import com.echo.service.UserProductMapService;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.UserProductMapExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "frontend")
public class MyProductController {

    @Autowired
    UserProductMapService userProductMapService;

    //我的消费记录
    @RequestMapping(value = "listuserproductmap", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listUserProductmapByCustomer(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        int pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        int pageSize = HttpServletUtils.getInteger(request, "pageSize");

        //判断用户的信息
        OwnerDO user = (OwnerDO) request.getSession().getAttribute("user");
        if (pageIndex > -1 && pageSize > -1 && user != null && user.getUserId() != null) {

            UserProductMapDO userProductMap = new UserProductMapDO();

            userProductMap.setUser(user);

            String productName = HttpServletUtils.getString(request, "productName");

            if (productName != null) {
                ProductDO product = new ProductDO();
                product.setProductName(productName);
                userProductMap.setProduct(product);
            }

            try {
                UserProductMapExecution userProductMapExecution = userProductMapService.getUserProductList(userProductMap, pageIndex, pageSize);
                modelMap.put("userProductMapList", userProductMapExecution.getUserProductMapList());
                modelMap.put("count", userProductMapExecution.getCount());
                modelMap.put("status", true);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }

        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "传入的参数不合法");
        }
        return modelMap;
    }
}
