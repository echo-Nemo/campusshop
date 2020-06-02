$(function () {
    // 获取此店铺下的商品列表的URL
    var listUrl = '../shop/getareas';

    getList();

    /**
     *区域信息的获取

     * @returns
     */
    function getList() {
        // 从后台获取此店铺的商品列表
        $.getJSON(listUrl, function (data) {
           // alert(JSON.stringify(data));

            if (data.status) {
               // alert("===ok===");
                var arealist = data.arealist;
                var tempHtml = '';
                // 遍历每条商品信息，拼接成一行显示，列信息包括：
                // 商品名称，优先级，上架\下架(含productId)，编辑按钮(含productId)
                // 预览(含productId)
                arealist.map(function (item, index) {

                   // alert(JSON.stringify(item));

                    // 拼接每件商品的行信息
                    tempHtml += '' + '<div class="row row-area">'
                        + '<div class="col-40">'
                        + item.areaName
                        + '</div>'
                        + '<div class="col-40">'
                        + item.priority
                        + '</div>'
                        + '</div>';
                });

                // 将拼接好的信息赋值进html控件中
                $('.area-wrap').html(tempHtml);
            }
        });
    }

});