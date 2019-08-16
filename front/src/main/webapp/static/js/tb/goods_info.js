var shortUrl;
var shareUrl;
$(function () {
    $("#coupon_div").hide();
    $("#red_div").hide();
    $("#coupon_div_bottom").hide();
    $.ajax({
        url: ctx + "/tb/goodsInfo/"+goodsId,
        type: "GET",
        dataType: "JSON",
        success: function (result) {
            if(result.code == 200) {
                var data = result.data;
                if(data != null) {
                    var imgTemplate = "<div class='swiper-slide'><img src='"+ data.pictUrl +"'></div>";
                    var miniImageList = data.imageList;
                    var infoListTemplate = "";
                    for(var i = 0; i < miniImageList.length; i++) {
                        imgTemplate += "<div class='swiper-slide'><img src='"+ miniImageList[i] +"'></div>";
                        infoListTemplate += "<img width='100%' src='"+ miniImageList[i] +"'>";
                    }
                    $("#swiperInfo").html(imgTemplate);
                    $("#itemInfoList").html(infoListTemplate);
                    $("#reservePriceInfo").html(data.reservePrice);
                    $("#catNameInfo").html(data.catLeafName);
                    $("#catLeafNameInfo").html(data.catName);
                    $("#nickInfo").html(data.nick);
                    $("#titleInfo").html(data.title);
                    $("#provcityInfo").html(data.provcity);
                    $("#volumeInfo").html(data.volume);
                    $("#postImg").attr("src", data.pictUrl);
                    $("#postTitle").html(data.title);
                    $("#postReservePrice").html(data.reservePrice);
                    $("#copyItemUrl").attr("src", data.pictUrl);
                    shortUrl = data.shortUrl;
                    shareUrl = "http://www.findcoupon.top/tb/goodsInfoUi/"+data.itemId;
                    if(data.userType == 0) {
                        $("#storeType").attr("src", ctx+"/static/image/icon/taobao_logo.png");
                    }
                    if(data.userType == 1) {
                        $("#storeType").attr("src", ctx+"/static/image/icon/tmall_logo.png");
                    }
                    var zkFinalPrice = parseFloat(data.zkFinalPrice);
                    var reservePrice = parseFloat(data.reservePrice);
                    var quanlimit = parseFloat(data.quanlimit);
                    var youhuiquan = parseFloat(data.youhuiquan);
                    var minPrice;
                    var truePrice;
                    if(zkFinalPrice < reservePrice) {//折扣价小于商品原价
                        minPrice = zkFinalPrice;
                    }else {
                        minPrice = reservePrice;
                    }
                    if(minPrice >= quanlimit) {//是否满足优惠条件
                        truePrice = parseFloat(zkFinalPrice - youhuiquan).toFixed(2);
                        $("#nowPriceTag").html("券后价");
                        $("#postNowPriceTag").html("券后价");

                    }else {
                        truePrice = zkFinalPrice.toFixed(2);
                        $("#nowPriceTag").html("现价");
                        $("#postNowPriceTag").html("现价");
                    }
                    $("#nowPrice").html("￥"+truePrice);
                    $("#postNowPrice").html("￥"+truePrice);
                    $("#red_info").html(data.rebatePrice+"元");
                    $("#red_div").show();
                    $("#postRedInfo").html("最高"+data.rebatePrice+"元现金红包");
                    if(data.hasCoupon) {
                        $("#twpCodeInfo").html("復·制"+data.tpwd+"这段描述后到淘♂寳♀,领取优惠券并下单;获得红包返利!");
                        $("#couponInfo").html(data.couponInfo);
                        $("#couponRemainCountInfo").html(data.couponRemainCount);
                        $("#couponTotalCountInfo").html(data.couponTotalCount);
                        $("#couponStartTimeInfo").html(data.couponStartTime);
                        $("#couponEndTimeInfo").html(data.couponEndTime);
                        $("#postCouponInfo").html(data.couponInfo);
                        $("#coupon_div").show();
                        $("#coupon_div_bottom").show();
                        $("#postCoupon").show();
                    }else {
                        $("#twpCodeInfo").html("復·制"+data.tpwd+"这段描述后到淘♂寳♀,并下单;获得红包返利!");
                        $("#topbar").html("优惠券已抢光,联系客服帮您留意下一波哟~");
                        $("#topbar").show();
                    }
                }else {
                    $("#bottombar").html("商品已下架,请移步【首页】获取更多优惠~");
                    $("#bottombar").show();
                }
            }
        },
        complete: function () {
            var swiper = new Swiper('.swiper-container', {
                loop : true,//循环播放
                autoHeight: true, //自适应高度
                centeredSlides: true,//活动块会居中
                initialSlide :0,
                observer:true,//修改swiper自己或子元素时，自动初始化swiper
                observeParents:true,//修改swiper的父元素时，自动初始化swiper
                autoplay: {//自动切换
                    delay: 2500,
                    disableOnInteraction: false,//触碰完成后自动切换继续
                },
                pagination: {//分页
                    el: '.swiper-pagination',
                    type: 'fraction',
                }
            });
        }
    })

    $('#myModal').on('shown.bs.modal', function () {
        var img = new Image();
        img.src = $("#postImage").attr("src");
        img.onload = function() {
            //获取dom宽高
            var modalHeight = document.getElementById("myModal").offsetHeight; //高度
            var contentHeight = document.getElementById("modalContent").offsetHeight; //高度
            $("#modalContent").css("margin-top", ((modalHeight - contentHeight)/2 -20) + "px");
        }
    })

    $('#copyModal').on('shown.bs.modal', function () {
        var modalHeight = document.getElementById("copyModal").offsetHeight; //高度
        var contentHeight = document.getElementById("copyModalContent").offsetHeight; //高度
        $("#copyModalContent").css("margin-top", ((modalHeight - contentHeight)/2 -20) + "px");
    })

    $('#myModal').on('hide.bs.modal', function () {
        $(document.body).css({
            "overflow-x":"auto",
            "overflow-y":"auto"
        });
    })

    $('#copyModal').on('hide.bs.modal', function () {
        $("#copyHelpInfo").show();
        $("#copySuccessInfo").hide();
        $("#copyTip").hide();
    })

    var clipboard = new Clipboard('#copyBtn', {
        target: function() {
            return document.querySelector('#twpCodeInfo');
        }
    });

    clipboard.on('success', function(e) {
        $("#copyHelpInfo").hide();
        $("#copySuccessInfo").show();
        $("#copyTip span").html("复制成功");
        $("#copyTip").fadeIn(1000);
        setTimeout(function () {
            $("#copyTip").fadeOut(1000);
        }, 3000)

        if (browser.versions.ios) {
            window.location.href = "taobao://";
            /*setTimeout(function(){
                window.location.href = "http://android.taobao.com/";
                window.location.href = "你的app在商店中的地址";  //为什么要加两遍我下面会说到
            },2000)*/
        }else if (browser.versions.android){
            window.location.href = "taobao://**";
            /*setTimeout(function(){
                window.location.href = "http://android.taobao.com/";
            },2000)*/
        }
    });

    clipboard.on('error', function(e) {
        $("#copyHelpInfo").show();
        $("#copySuccessInfo").hide();
        $("#copyTip span").html("复制失败");
        $("#copyTip").fadeIn(1000);
        setTimeout(function () {
            $("#copyTip").fadeOut(1000);
        }, 3000)
    });

})

function doShare() {
    if($("#bottombar").is(':visible')) {
        return;
    }
    $(document.body).css({
        "overflow-x":"hidden",
        "overflow-y":"hidden"
    });
    $("#canvasToImageDiv").show();

    var width = $('#postInfo').width();
    var height = $('#postInfo').height();
    var codeWidth = $("#qrcodeDiv").width() - 20;
    $('#qrcodeDiv').qrcode({
        render: 'canvas',
        width: codeWidth,
        height: codeWidth,
        text: shareUrl
    });
    var type = "png";
    var canvas2 = document.createElement('canvas');
    var context = canvas2.getContext('2d');
    var ratio = getPixelRatio(context);
    canvas2.width = width * ratio;
    canvas2.height = height * ratio;
    canvas2.style.width = width + 'px';
    canvas2.style.height = height + 'px';
    context.scale(ratio, ratio);
    context.mozImageSmoothingEnabled = false;
    context.webkitImageSmoothingEnabled = false;
    context.msImageSmoothingEnabled = false;
    context.imageSmoothingEnabled = false;
    html2canvas($('#postInfo')[0], {
        canvas: canvas2,
        async: false,
        useCORS: true, // 【重要】开启跨域配置
        onrendered: function (canvas) {
            $("#imageInfo").append(Canvas2Image.convertToImage(canvas, width, height, type));
            $("#imageInfo img").css("width", '100%').css("height", "auto");
            $("#postImage").attr("src", $("#imageInfo img").attr("src"));
            $("#canvasToImageDiv").hide();
        }
    });
    $('#myModal').modal('show');
}

function getPixelRatio(context) {
    var backingStore = context.backingStorePixelRatio ||
        context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio || 1;
    return (window.devicePixelRatio || 1) / backingStore;
};

function doCopyModal() {
    $('#copyModal').modal('show');
}

function doBrowse() {
    window.location.href = shortUrl;
}

function doHome() {
    /*$("#bottombar").html("攻城狮绘制中...敬请期待!");
    $("#bottombar").fadeIn(1000);
    setTimeout(function () {
        $("#bottombar").fadeOut(1000);
    }, 3000)*/
    window.location.href = ctx + "/tb/goodsListUi/list";
}

var browser = {
    versions: function() {
        var u = navigator.userAgent,
            app = navigator.appVersion;
        return {
            trident: u.indexOf('Trident') > -1,
            presto: u.indexOf('Presto') > -1,
            webKit: u.indexOf('AppleWebKit') > -1,
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,
            mobile: !!u.match(/AppleWebKit.*Mobile.*/),
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1,
            iPhone: u.indexOf('iPhone') > -1,
            iPad: u.indexOf('iPad') > -1,
            webApp: u.indexOf('Safari') == -1,
            souyue: u.indexOf('souyue') > -1,
            superapp: u.indexOf('superapp') > -1,
            weixin:u.toLowerCase().indexOf('micromessenger') > -1,
            Safari:u.indexOf('Safari') > -1
        };
    }(),
    language: (navigator.browserLanguage || navigator.language).toLowerCase()
};


//这里用到两个判断，一个ios一个安卓
if (browser.versions.ios) {

    //你的代码

}else if (browser.versions.android){

    //你的代码

}

