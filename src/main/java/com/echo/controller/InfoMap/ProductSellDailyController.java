package com.echo.controller.InfoMap;

import com.echo.dataobject.ProductDO;
import com.echo.dataobject.ProductSellDailyDO;
import com.echo.dataobject.ShopDO;
import com.echo.dataobject.UserProductMapDO;
import com.echo.dto.EchartSeries;
import com.echo.dto.EchartXAxis;
import com.echo.service.ProductSellDailyService;
import com.echo.service.UserProductMapService;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.UserProductMapExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("product")
public class ProductSellDailyController {

    @Autowired
    ProductSellDailyService productSellDailyService;

    @Autowired
    UserProductMapService userProductMapService;

    //用户商品信息列表
    @RequestMapping(value = "listusershopmapbyshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listUserShopMapByshop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //获取分页信息
        int pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        int pageSize = HttpServletUtils.getInteger(request, "pageSize");

        //获取当前店铺的信息
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");

        if (currentShop != null && pageIndex > 0 && pageSize > 0) {
            //是否根据商品名查询
            UserProductMapDO userProductMapCondition = new UserProductMapDO();
            userProductMapCondition.setShop(currentShop);

            String productName = HttpServletUtils.getString(request, "productName");

            if (productName != null) {
                //若前端进行按productName进行模糊查询
                ProductDO product = new ProductDO();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }
            try {
                UserProductMapExecution execution = userProductMapService.getUserProductList(userProductMapCondition, pageIndex, pageSize);
                modelMap.put("userProductMapList", execution.getUserProductMapList());
                modelMap.put("count", execution.getCount());
                modelMap.put("status", true);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
            }

        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "传入的参数值不能为空");
        }
        return modelMap;
    }


    //统计某个店铺的日销售量
    @RequestMapping(value = "listproductselldailybyshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listProductSellDailyByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(4);
        // 获取当前的店铺信息
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
        // 空值校验，确保shopId不为空
        if (currentShop != null && currentShop.getShopId() != null) {
            // 添加查询条件
            ProductSellDailyDO productSellDailyCondition = new ProductSellDailyDO();
            productSellDailyCondition.setShop(currentShop);

            Calendar calendar = Calendar.getInstance();
            // 获取昨天的日期
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            // 获取七天前的日期
            calendar.add(Calendar.DATE, -6);
            Date beginTime = calendar.getTime();
            // 根据传入的查询条件获取该店铺的商品销售情况
            List<ProductSellDailyDO> productSellDailyList = productSellDailyService.getProductSellDailyList(productSellDailyCondition, beginTime, endTime);

            // 指定日期的格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 商品名列表，保证唯一性
            HashSet<String> legendData = new HashSet<>();
            // x轴数据
            TreeSet<String> xData = new TreeSet<>();
            // 定义 series
            List<EchartSeries> series = new ArrayList<>();
            // 日销量列表
            List<Integer> totalList = new ArrayList<>();

            // 当前商品名，默认为空
            String currentProductName = "";
            for (int i = 0; i < productSellDailyList.size(); i++) {
                ProductSellDailyDO productSellDaily = productSellDailyList.get(i);
                // 自动去重
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));

                // 判断当前商品名是否与上一个商品名一致
                if (!currentProductName.equals(productSellDaily.getProduct().getProductName()) &&
                        !currentProductName.isEmpty()) {
                    // 如果 currentProductName 不等于获取的商品名，或者已遍历到列表的末尾，且 currentProductName 不为空
                    // 则是遍历到下一个商品的日销量信息了，将前一轮遍历的信息放入 series 中，
                    // 包括了商品名以及与商品对应的统计日期以及当日销量
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);

                    // 重置 totalList
                    totalList = new ArrayList<>();
                    // 变换下 currentProductId 为当前的 productId
                    currentProductName = productSellDaily.getProduct().getProductName();
                    // 继续添加新的值
                    totalList.add(productSellDaily.getTotal());
                } else {
                    // 如果还是当前的 productId 则继续添加新值
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }

                // 队列末尾，需要将最后的一个商品销量信息也添加上
                if (i == productSellDailyList.size() - 1) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series", series);
            modelMap.put("legendData", legendData);

            // 拼接出 xAis
            List<EchartXAxis> xAxis = new ArrayList<>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis", xAxis);
            modelMap.put("status", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("status", "Empty shopId");
        }
        return modelMap;
    }
}

