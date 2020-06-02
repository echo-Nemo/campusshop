$(function () {
    var listUrl = '../product/queryproductcategorylist';
    var addUrl = '../product/addproductcategorys';
    var deleteUrl = '../product/removeproductcategory';
    getList();

    function getList() {
        $.getJSON(
            listUrl,
            function (data) {
                if (data.status) {
                    var dataList = data.data;
                    $('.category-wrap').html('');
                    var tempHtml = '';
                    dataList.map(function (item, index) {
                        tempHtml += ''
                            + '<div class="row row-product-category now">'
                            + '<div class="col-25 product-category-name">'
                            + item.productCategoryName
                            + '</div>'
                            + '<div class="col-25 product-category-desc">'
                            + item.productCategoryDesc
                            + '</div>'
                            + '<div class="col-25">'
                            + item.priority
                            + '</div>'
                            + '<div class="col-25"><a href="#" class="button delete" data-id="'
                            + item.productCategoryId
                            + '">删除</a></div>'
                            + '</div>';
                    });
                    $('.category-wrap').append(tempHtml);
                }
            });
    }

    $('#new').click(
        function () {
            var tempHtml = '<div class="row row-product-category temp">'
                + '<div class="col-25"><input class="category-input category" type="text" placeholder="分类名"></div>'
                + '<div class="col-25"><input class="category-input desc" type="text" placeholder="评价"></div>'
                + '<div class="col-25"><input class="category-input priority" type="number" placeholder="优先级"></div>'
                + '<div class="col-25"><a href="#" class="button delete">删除</a></div>'
                + '</div>';
            $('.category-wrap').append(tempHtml);
        });

    $('#submit').click(function () {
        var tempArr = $('.temp');
        var productCategoryList = [];
        tempArr.map(function (index, item) {
            var tempObj = {};
            tempObj.productCategoryName = $(item).find('.category').val();
            tempObj.productCategoryDesc = $(item).find('.desc').val();
            tempObj.priority = $(item).find('.priority').val();
            if (tempObj.productCategoryName && tempObj.priority && tempObj.productCategoryDesc) {
                productCategoryList.push(tempObj);
            }
        });

        $.ajax({
            url: addUrl,
            type: 'POST',
            data: JSON.stringify(productCategoryList),
            contentType: 'application/json',
            xhrFields: {
                withCredentials: true
            },
            success: function (data) {
                if (data.status) {
                    $.toast('提交成功！');
                    getList();
                } else {
                    $.toast('提交失败！');
                }
            }, error: function () {
                $.toast("error");
            }
        });
    });


//删除新增的列
    $('.category-wrap').on('click', '.row-product-category.temp .delete',
        function (e) {
            console.log($(this).parent().parent());
            $(this).parent().parent().remove();

        });

    //删除数据库中的数据
    $('.category-wrap').on('click', '.row-product-category.now .delete',
        function (e) {
            var target = e.currentTarget;
            $.confirm('确定么?', function () {
                $.ajax({
                    url: deleteUrl,
                    type: 'POST',
                    data: {
                        productCategoryId: target.dataset.id
                    },
                    dataType: 'json',
                    xhrFields: {
                        withCredentials: true
                    },
                    success: function (data) {
                        if (data.status) {
                            $.toast('删除成功！');
                            getList();
                        } else {
                            $.toast('删除失败！');
                        }
                    }, error: function () {
                        $.toast("error");
                    }
                });
            });
        });
});