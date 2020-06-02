/**
 *
 */

$(function () {
    var formData = new FormData();
    var shopId = getQueryString('shopId');
    var isEdit = shopId ? true : false;

    var initUrl = '../shop/getshopinitfo';

    var registerShopUrl = '../shop/registershop';

    var shopInfoUrl = "../shop/getshopbyid?shopId=" + shopId;

    var editShopUrl = '../shop/modifyshop';


    if (!isEdit) {
        getShopInitInfo();
    } else {
        getShopInfo(shopId);
    }


    // 通过店铺Id获取店铺信息
    function getShopInfo(shopId) {

        $.getJSON(shopInfoUrl, function (data) {

            if (data.status) {
                // 若访问成功，则依据后台传递过来的店铺信息为表单元素赋值
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                // $('#shop-img').val(shop.shopImg);
                // 给店铺类别选定原先的店铺类别值
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                // 初始化区域列表
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(shopCategory);
                // 不允许选择店铺类别
                $('#shop-category').attr('disabled', 'disabled');
                $('#area').html(tempAreaHtml);
                // 给店铺选定原先的所属的区域
                $("#area option[data-id='" + shop.area.areaId + "']").attr(
                    "selected", "selected");
            }
        });
    }

    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            if (data.status) {
                var tempShopCategoryHtml = '';
                var tempAreaHtml = '';

                data.shopCategoryList.map(function (item, index) {
                    tempShopCategoryHtml += '<option data-id="'
                        + item.shopCategoryId + '">'
                        + item.shopCategoryName + '</option>';
                });

                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });

                $("#shop-category").html(tempShopCategoryHtml);
                $("#area").html(tempAreaHtml);
            }
        });
    }

    $("#submit").click(function () {

        var shop = {};

        if (isEdit) {
            shop.shopId = shopId;
        }

        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();

        shop.shopCategory = {
            shopCategoryId: $('#shop-category').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };

        shop.area = {
            areaId: $('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };

        var shopImg = $('#shop-img')[0].files[0];

        // var formData = new FormData();
        formData.append('shopImg', shopImg);
        formData.append('shopStr', JSON.stringify(shop));

        var verifyCodeActual = $('#j_captcha').val();

        if (!verifyCodeActual) {
            $.toast('请输入验证码');
            return;
        }

        formData.append('verifyCodeActual', verifyCodeActual);

        $.ajax({
            url: (isEdit ? editShopUrl : registerShopUrl),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                //alert("success");
                if (data.status) {
                   // alert("提交成功");
                    window.location.href='../shop/shoplist';
                } else {
                    alert('提交失败了！' + data.errMsg);
                }
                $('#captcha_img').click();
            }, error: function () {
                alert("error function");
            }
        });

    });
});