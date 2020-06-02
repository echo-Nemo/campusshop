package com.echo.util.execution;

import com.echo.dataobject.ProductDO;

import java.util.List;

public class ProductExecution {
    //商品的个数
    private int count;
    //商品的状态
    private int status;
    //商品列表
    private List<ProductDO> productDList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ProductDO> getProductDList() {
        return productDList;
    }

    public void setProductDList(List<ProductDO> productDList) {
        this.productDList = productDList;
    }
}
