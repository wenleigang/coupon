$(function () {
    $("#coupon_div").hide();
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
                    for(var i = 0; i < miniImageList.length; i++) {
                        imgTemplate += "<div class='swiper-slide'><img src='"+ miniImageList[i] +"'></div>";
                    }
                    $("#swiperInfo").html(imgTemplate);
                    $("#zkFinalPriceInfo").html(data.zkFinalPrice);
                    $("#catNameInfo").html(data.catLeafName);
                    $("#catLeafNameInfo").html(data.catName);
                    $("#reservePriceInfo").html(data.reservePrice);
                    $("#nickInfo").html(data.nick);
                    $("#titleInfo").html(data.title);
                    $("#provcityInfo").html(data.provcity);
                    $("#volumeInfo").html(data.volume);
                    $("#cccc").attr("src", data.pictUrl);
                    if(data.hasCoupon) {
                        $("#coupon_div").show();
                        $("#coupon_div_bottom").show();
                        $("#couponInfo").html(data.couponInfo);
                        $("#couponRemainCountInfo").html(data.couponRemainCount);
                        $("#couponTotalCountInfo").html(data.couponTotalCount);
                        $("#couponStartTimeInfo").html(data.couponStartTime);
                        $("#couponEndTimeInfo").html(data.couponEndTime);
                    }
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
        //$("#modalContent").html(document.getElementById("modalContent").offsetHeight);
    })

})

function doShare() {
    $(document.body).css({
        "overflow-x":"hidden",
        "overflow-y":"hidden"
    });
    $('#myModal').modal('show');
}

function doCloseShareImg() {
    $(document.body).css({
        "overflow-x":"auto",
        "overflow-y":"auto"
    });
    $('#myModal').modal('hide');
}
