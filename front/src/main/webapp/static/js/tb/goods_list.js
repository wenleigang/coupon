/*初始化拉去数据页码*/
var pageNum = 1;

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
    setTimeout(function() {
        initData(pageNum);
    }, 1000);
}

/*下拉刷新触发事件*/
function pulldownRefresh() {
    setTimeout(function() {
        mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
        mui('#pullrefresh').pullRefresh().scrollTo(0,0);
        $(".mui-table-view").empty();
        pageNum = 1;
        initData(pageNum);
    }, 1000);
}

/*加载数据具体操作*/
function initData(pageno) {
    /*获取搜索框文字*/
    var text = $("#keyword").val();//获取搜索框输入的文字
    if(text == undefined || text == "") {//如果第一次进来或者未输入任何文字
        text = ".";
    }
    /*获取分页数据并回显*/
    $.ajax({
        url: ctx + "/tb/searchGoodsList?keyword="+text+"&pageno="+pageno,
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
                }
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
                pageNum++;
                /*如果返回一个数据,不代表没有了,需要主动修改pageNum+1再次调用一次接口*/
                if(dataList != null && dataList.length == 1) {
                    initData(pageNum);
                }
            }
        }
    })
}

/*点击item跳转到商品详情页面触发事件*/
mui("body").on("tap", ".c_itemBox", function (obj) {
    var itemId = $(this).attr("id");
    window.location.href = ctx + "/tb/goodsInfoUi/"+itemId;
})

mui("body").on("tap", ".mui-control-item", function (obj) {
    var arr = [];
    $(".mui-control-item").each(function (index, obj) {
        if($(this).hasClass("mui-active")) {
            arr.push($(this));
        }
    })
    $(this).addClass("mui-active");
    for(var i = 0; i < arr.length; i++) {
        arr[i].addClass("mui-active");
    }
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
            pulldownRefresh();
            document.activeElement.blur();  // 关闭软键盘
        }
    });
})