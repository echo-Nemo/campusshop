$(function () {
    // 从URL里获取awardId参数的值
    var awardId = getQueryString('awardId');
    // 通过awardId获取商品信息的URL
    var infoUrl = '../shop/getawardbyid?awardId=' + awardId;
    // 更新商品信息的URL
    var awardPostUrl = '../shop/modifyaward';
    // 由于商品添加和编辑使用的是同一个页面，
    // 该标识符用来标明本次是添加还是编辑操作
    var isEdit = false;

    if (awardId) {
        // 若有awardId则为编辑操作
        getInfo(awardId);
        isEdit = true;
    } else {
        // getCategory();
        awardPostUrl = '../shop/addaward';
    }

    // 获取需要编辑的商品的商品信息，并赋值给表单
    function getInfo(awardId) {
        $.getJSON(
            infoUrl,
            function (data) {
                if (data.status) {
                    // 从返回的JSON当中获取award对象的信息，并赋值给表单
                    var award = data.award;
                    $('#award-name').val(award.awardName);
                    $('#award-desc').val(award.awardDesc);
                    $('#priority').val(award.priority);
                    $('#point').val(award.point);
                }
            });
    }

    // 提交按钮的事件响应，分别对商品添加和编辑操作做不同响应
    $('#submit').click(
        function () {
            // 创建商品json对象，并从表单里面获取对应的属性值
            var award = {};
            award.awardName = $('#award-name').val();
            award.awardDesc = $('#award-desc').val();
            award.priority = $('#priority').val();
            award.point = $('#point').val();
            award.awardId = awardId ? awardId : '';

            // 获取缩略图文件流
            var thumbnail = $('#small-img')[0].files[0];
            // 生成表单对象，用于接收参数并传递给后台
            var formData = new FormData();
            formData.append('thumbnail', thumbnail);

            // 将award json对象转成字符流保存至表单对象key为awardStr的的键值对里
            formData.append('awardStr', JSON.stringify(award));
            // 获取表单里输入的验证码
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            // 将数据提交至后台处理相关操作
            $.ajax({
                url: awardPostUrl,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.status) {
                        $.toast('提交成功！');
                        $('#captcha_img').click();
                    } else {
                        $.toast('提交失败！');
                        $('#captcha_img').click();
                    }
                }
            });
        });
});