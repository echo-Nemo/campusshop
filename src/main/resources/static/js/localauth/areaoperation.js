$(function () {
    //var addUrl = "../shop/addarea";

    $('#submit').click(
        function () {
            // 创建商品json对象，并从表单里面获取对应的属性值
            var area = {};

            area.areaName = $('#area-name').val();
            area.priority = $('#priority').val();

            var formData = new FormData();


            // 将product json对象转成字符流保存至表单对象key为productStr的的键值对里
            formData.append('areaStr', JSON.stringify(area));

            // 获取表单里输入的验证码
            var verifyCodeActual = $('#j_captcha').val();

            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }

            formData.append("verifyCodeActual", verifyCodeActual);

            // 将数据提交至后台处理相关操作
            $.ajax({
                url: "../shop/addarea",
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                cache: false,
                success: function (data) {
                    if (data.status) {
                      //  alert("success");
                        $.toast('添加成功！');
                        $('#captcha_img').click();
                    } else {
                        $.toast('添加失败！');
                        $('#captcha_img').click();
                    }
                }, error: function () {
                    $.toast('error function');
                }
            });
        });

});