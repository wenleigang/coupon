/*初始化拉去数据页码*/
var pageNum = 1;
var loadingText = "<i class='fa fa-refresh fa-spin fa-x'></i><span style='font-size: medium'>&nbsp;&nbsp;加载中......</span>";
/*上拉下拉刷新列表参数初始化*/
mui.init({
    pullRefresh: {
        container: '#pullrefresh',
        down: {
            style:'circle',
            callback: pulldownRefresh
        },
        up: {
            auto:true,
            contentrefresh: '正在加载',
            callback: pullupRefresh
        }
    }
});

/*上滑操作触发事件*/
function pullupRefresh() {
    loading(loadingText);
    setTimeout(function() {
        initData(pageNum);
    }, 1000);
}

/*下拉刷新触发事件*/
function pulldownRefresh() {
    loading(loadingText);
    mui('#pullrefresh').pullRefresh().scrollTo(0,0);
    $(".mui-table-view").empty();
    //mui('#pullrefresh').pullRefresh().pulldownLoading();
    setTimeout(function() {
        mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
        pageNum = 1;
        initData(pageNum);
    }, 1000);
}

/*加载数据具体操作*/
function initData(pageno) {
    if(tag != "") {
        $("#keyword").val(tag);
    }
    /*获取搜索框文字*/
    var text = $("#keyword").val();//获取搜索框输入的文字
    if(text == undefined || text == "") {//如果第一次进来或者未输入任何文字
        text = ".";
    }
    //组装其他查询条件
    var defaultTag = $("#item_default").attr("value");
    var priceTag = $("#item_price").attr("value");
    var numberTag = $("#item_number").attr("value");
    var tmalltTag = $("#item_tmall").attr("value");
    var couponTag = $("#item_coupon").attr("value");
    /*获取分页数据并回显*/
    $.ajax({
        url: ctx + "/tb/searchGoodsList?keyword="+text+"&pageno="+pageno+"&defaultTag="+defaultTag+"&priceTag="+priceTag+"&numberTag="+numberTag+"&tmalltTag="+tmalltTag+"&couponTag="+couponTag,
        type: "GET",
        dataType: "JSON",
        async:false,
        success: function (result) {
            if(result.code == 200) {
                var dataList = result.data;
                if(dataList == null || dataList.length == 0) {
                    mui('#pullrefresh').pullRefresh().endPullupToRefresh((true));//参数为true代表没有更多数据了。
                }else{
                    mui('#pullrefresh').pullRefresh().endPullupToRefresh((false));//参数为false代表有更多数据了
                    var table = document.body.querySelector('.mui-table-view');
                    for (var i = 0; i < dataList.length; i++) {
                        var zkFinalPrice = parseFloat(dataList[i].zkFinalPrice);
                        var quanlimit = parseFloat(dataList[i].quanlimit);
                        var youhuiquan = parseFloat(dataList[i].youhuiquan);
                        var truePrice;
                        if(zkFinalPrice >= quanlimit) {//是否满足优惠条件
                            truePrice = parseFloat(zkFinalPrice - youhuiquan).toFixed(2);
                        }else {
                            truePrice = zkFinalPrice.toFixed(2);
                        }
                        var div = document.createElement('div');
                        div.className = 'c_itemBox';
                        div.id = dataList[i].numIid;
                        div.innerHTML = '<div class="c_itemDom">\n' +
                            '<div class="c_itemImage"><img src="'+ dataList[i].pictUrl +'"/></div>\n' +
                            '<div class="c_itemInfo">\n' +
                            '<div>\n' +
                            '<span class="c_infoTitle">'+ dataList[i].title +'</span>\n' +
                            '</div>\n' +
                            '<div>\n' +
                            '<span class="c_infoReservePrice">原价￥'+ dataList[i].zkFinalPrice +'元</span><br/>\n' +
                            '<span class="c_infoNowPrice">￥'+ truePrice +'元</span>\n' +
                            '<span class="c_infoVolume">'+ dataList[i].volume +'人付款</span>\n' +
                            '</div>\n' +
                            '</div>\n' +
                            '</div>';
                        table.appendChild(div);
                    }
                }
                pageNum++;
                /*如果返回一个数据,不代表没有了,需要主动修改pageNum+1再次调用一次接口*/
                if(dataList != null && dataList.length == 1) {
                    initData(pageNum);
                }
            }else {
                $("#mask_info").html("查询失败!");
            }
        },
        error: function () {
            $("#mask_info").html("查询失败!");
        },
        complete: function(XMLHttpRequest, textStatus) {
            $("#mask").hide();
        }
    })
}

/*点击item跳转到商品详情页面触发事件*/
mui("body").on("tap", ".c_itemBox", function (obj) {
    var itemId = $(this).attr("id");
    window.location.href = ctx + "/tb/goodsInfoUi/"+itemId;
})

/*点击跳转帮主页面触发事件*/
function toJdHelpUi() {
    window.location.href = ctx + "/docs/tbAppHelp";
}

/*监听搜索框触发事件*/
$(function () {
    $("#keyword").on('keypress',function(e) {
        var keycode = e.keyCode;
        if(keycode=='13') {
            document.activeElement.blur();  // 关闭软键盘
            pulldownRefresh();
        }
    });
})

$("#keyword").on("input onpropertychange", function () {
    var text = $("#keyword").val();
    if(text != null && text != "") {
        $("#clearBtn").show();
    }else {
        $("#clearBtn").hide();
    }
})

/*清空输入框内容按钮触发事件*/
function clearText() {
    $("#keyword").val("");
    $("#clearBtn").hide();
}

/*点击默认按钮触发事件*/
$("#item_default").on("click", function (obj) {
    var type = $("#item_default").attr("value");
    if(type == 0) {
        //初始化价格和销量的选择
        $("#item_price img").attr("src", ctx+"/static/image/icon/up_down.png");
        $("#item_price").removeClass("c_active_item")
        $("#item_number img").attr("src", ctx+"/static/image/icon/up_down.png");
        $("#item_number").removeClass("c_active_item")
        //选中默认标签
        $("#item_default").attr("value", 1);
        $("#item_default").addClass("c_active_item");
        //执行刷新操作
        pulldownRefresh();
    }
})

/*点击价格按钮触发事件*/
$("#item_price").on("click", function (obj) {
    //type 0 标识未排序,设置为降序 1
    //type 1 标识降序排列,设置为升序 2
    //type 2 标识升序排列,设置为降序 1
    var priceType = $("#item_price").attr("value");
    var numberType = $("#item_number").attr("value");
    var defaultType = $("#item_default").attr("value");
    if(defaultType == 1) {
        $("#item_default").removeClass("c_active_item");
        $("#item_default").attr("value", 0);
    }
    if(numberType != 0) {
        $("#item_number").removeClass("c_active_item");
        $("#item_number img").attr("src", ctx+"/static/image/icon/up_down.png");
        $("#item_number").attr("value", 0);
    }
    if(priceType == 0 || priceType == 2) {//
        $("#item_price img").attr("src", ctx+"/static/image/icon/down_select.png");
        $("#item_price").attr("value", 1);
    }
    if(priceType == 1) {
        $("#item_price img").attr("src", ctx+"/static/image/icon/up_select.png");
        $("#item_price").attr("value", 2);
    }
    $("#item_price").addClass("c_active_item")
    //执行刷新操作
    pulldownRefresh();
})

/*点击销量按钮触发事件*/
$("#item_number").on("click", function (obj) {
    //type 0 标识未排序,设置为降序 1
    //type 1 标识降序排列,设置为升序 2
    //type 2 标识升序排列,设置为降序 1
    var priceType = $("#item_price").attr("value");
    var numberType = $("#item_number").attr("value");
    var defaultType = $("#item_default").attr("value");
    if(defaultType == 1) {
        $("#item_default").removeClass("c_active_item");
        $("#item_default").attr("value", 0);
    }
    if(priceType != 0) {
        $("#item_price").removeClass("c_active_item");
        $("#item_price img").attr("src", ctx+"/static/image/icon/up_down.png");
        $("#item_price").attr("value", 0);
    }
    if(numberType == 0 || numberType == 2) {//
        $("#item_number img").attr("src", ctx+"/static/image/icon/down_select.png");
        $("#item_number").attr("value", 1);
    }
    if(numberType == 1) {
        $("#item_number img").attr("src", ctx+"/static/image/icon/up_select.png");
        $("#item_number").attr("value", 2);
    }
    $("#item_number").addClass("c_active_item")
    //执行刷新操作
    pulldownRefresh();
})

/*点击天猫按钮触发事件*/
$("#item_tmall").on("click", function (obj) {
    var tmallType = $("#item_tmall").attr("value");
    if(tmallType == 0) {
        $("#item_tmall img").attr("src", ctx+"/static/image/icon/select.png");
        $("#item_tmall").attr("value", 1);
        $("#item_tmall").addClass("c_active_item");
    }
    if(tmallType == 1) {
        $("#item_tmall img").attr("src", ctx+"/static/image/icon/unselect.png");
        $("#item_tmall").attr("value", 0);
        $("#item_tmall").removeClass("c_active_item");
    }
    //执行刷新操作
    pulldownRefresh();
})

/*点击优惠券按钮触发事件*/
$("#item_coupon").on("click", function (obj) {
    var couponType = $("#item_coupon").attr("value");
    if(couponType == 0) {
        $("#item_coupon img").attr("src", ctx+"/static/image/icon/select.png");
        $("#item_coupon").attr("value", 1);
        $("#item_coupon").addClass("c_active_item");
    }
    if(couponType == 1) {
        $("#item_coupon img").attr("src", ctx+"/static/image/icon/unselect.png");
        $("#item_coupon").attr("value", 0);
        $("#item_coupon").removeClass("c_active_item");
    }
    //执行刷新操作
    pulldownRefresh();
})

function loading(text) {
    $("#mask").show();
    var height = document.getElementById("mask").offsetHeight;
    $("#mask_info").css("margin-top", ((height - 100)/2) + "px");
    $("#mask_info").html(text);
}