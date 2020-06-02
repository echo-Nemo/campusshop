$(function () {

    var shopId = getQueryString('shopId');
   // alert(shopId);

    //controller层shopmanagement的url
    var shopInfoUrl = '../shop/shopmanagementinfo?shopId=' + shopId;

    $.getJSON(shopInfoUrl, function (data) {
        if (data.redirect) {
            window.location.href = data.url;
        } else {
            if (data.shopId != undefined && data.shopId != null) {
                shopId = data.shopId;
            }
            //跳转到shop修改的页面
            $('#shopInfo').attr('href', '../shop/shopoperation?shopId=' + shopId);

        }
    });

});