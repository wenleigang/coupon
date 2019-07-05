<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>商品详情</title>
        <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <script type="text/javascript">var ctx = '${ctx}'</script>
        <script type="text/javascript" src="${ctx}/static/js/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="${ctx}/static/plugins/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${ctx}/static/plugins/swiper-4.5.0/js/swiper.min.js"></script>
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/bootstrap-3.3.7-dist/css/bootstrap.css"/>
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/bootstrap-3.3.7-dist/css/bootstrap-theme.css"/>
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/font-awesome-4.7.0/css/font-awesome.css"/>
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/swiper-4.5.0/css/swiper.min.css">
        <style type="text/css">
            * {
                margin: 0;
                padding: 0;
            }
            .c_container {
                height: 100%;
                overflow: auto;
                background-color: #efeff4;
            }
            .c_content {
                margin-bottom: 60px;
            }
            .c_toolbar {
                z-index: 999;
                height: 60px;
                position: fixed;
                background-color: #ffffff;
                bottom: 0;
                padding: 5px 5px;
                width: 100%;
                text-align: center;
                line-height: 50px;
            }
            .c_toolbar_home {
                float: left;
                width: 18%;
                cursor: pointer;
            }
            .c_toolbar_share {
                float: left;
                width: 18%;
                cursor: pointer;
            }
            .c_toolbar_browser {
                float: left;
                width: 32%;
                cursor: pointer;
                color: #ffffff;
                font-weight: bolder;
                border-top-left-radius: 25px;
                border-bottom-left-radius: 25px;
                background: linear-gradient(left, #f7cc45, #f09937);
                background: -ms-linear-gradient(left, #f7cc45, #f09937);
                background: -webkit-linear-gradient(left, #f7cc45, #f09937);
                background: -moz-linear-gradient(left, #f7cc45, #f09937);
            }
            .c_toolbar_buy {
                float: left;
                width: 32%;
                cursor: pointer;
                color: #ffffff;
                font-weight: bolder;
                border-top-right-radius: 25px;
                border-bottom-right-radius: 25px;
                background: linear-gradient(left, #ed7e30, #e95727);
                background: -ms-linear-gradient(left, #ed7e30, #e95727);
                background: -webkit-linear-gradient(left, #ed7e30, #e95727);
                background: -moz-linear-gradient(left, #ed7e30, #e95727);}
            .swiper-slide img {
                width: 100%;
            }
            .c_active_price {
                color: #ed6d27;
                font-size: 25px;
            }
            .c_info_text {
                color: #8c8c8c;
                font-size: 12px;
            }
            .c_info_disable{
                text-decoration:line-through;
            }
            .c_xs_tag {
                background-color: #fdf6f2;
                padding: 2px 10px;
                border-radius: 10px;
                color: #ea521c;
                font-size: 12px;
            }
            .c_sm_tag {
                background-color: #fdf6f2;
                padding: 5px 10px;
                border-radius: 12px;
                color: #ea521c;
                font-size: 15px;
            }
            .c_center_text {
                text-align: center;
            }
            .c_info_content {
                width: 100%;
                padding: 10px;
                overflow:auto;
                background-color: #ffffff;
            }
            .c_info_title {
                font-size: medium;
                font-weight: bolder;
            }
            .c_icon {
                width: 20px;
                height: 20px;
            }
            .c_div_10px {
                height: 10px;
            }
            .c_coupon_content {
                border-radius: 8px;
                overflow: auto;
                padding: 10px 5px;
                background: linear-gradient(left, #f09937, #f7cc45);
                background: -ms-linear-gradient(left, #f09937, #f7cc45);
                background: -webkit-linear-gradient(left, #f09937, #f7cc45);
                background: -moz-linear-gradient(left, #f09937, #f7cc45);
            }
            .c_coupon_clear {
                float: left;
                width: 100%;
                height: 5px;
            }
            .c_coupon_info {
                float: left;
                width: 60%;
                text-align: left;
            }
            .c_coupon_num {
                float: left;
                width: 40%;
                text-align: right;
            }
            .c_coupon_time {
                float: left;
                width: 100%;
                text-align: left;
            }
            .c_coupon_text {
                color: #ffffff;
                font-size: 12px;
            }
            .c_goods_info_col {
                float: left;
                width: 50%;
                font-size: 12px;
                text-align: center;
                color: #8c8c8c;
            }
            .c_font_size_12 {
                font-size: 12px;
            }
            .c_info_color_white {
                color: #ffffff;
            }
        </style>
    </head>
    <body class="c_container">
        <div class="c_content">
            <div class="swiper-container">
                <div class="swiper-wrapper" id="swiperInfo"></div>
                <div class="swiper-pagination c_info_color_white"></div>
            </div>
            <div class="c_info_content">
                <span class="c_active_price">￥</span>
                <span class="c_active_price" id="zkFinalPriceInfo"></span>&nbsp;&nbsp;
                <span class="c_info_text">原价￥</span>
                <span class="c_info_text c_info_disable" id="reservePriceInfo"></span><br/>
                <span class="c_xs_tag" id="catNameInfo"></span>
                <span class="c_xs_tag" id="catLeafNameInfo"></span>
            </div>
            <div class="c_div_10px"></div>
            <div id="coupon_div" class="c_info_content">
                <div class="c_coupon_content">
                    <div class="c_coupon_info">
                        <i class="fa fa-ticket c_info_color_white" aria-hidden="true"></i>&nbsp;
                        <span class="c_sm_tag" id="couponInfo"></span>
                    </div>
                    <div class="c_coupon_num">
                        <span class="c_coupon_text">
                            <i class="fa fa-gift" aria-hidden="true"></i>&nbsp;劵:
                            <span id="couponRemainCountInfo"></span>&nbsp;&nbsp;/
                            <span id="couponTotalCountInfo"></span>张
                        </span>
                    </div>
                    <div class="c_coupon_clear"></div>
                    <div class="c_coupon_time">
                        <span class="c_coupon_text">
                            <i class="fa fa-clock-o" aria-hidden="true"></i>&nbsp;优惠券有效期:
                            <span id="couponStartTimeInfo"></span>&nbsp;&nbsp;~
                            <span id="couponEndTimeInfo"></span>
                        </span>
                    </div>
                </div>
            </div>
            <div id="coupon_div_bottom" class="c_div_10px"></div>
            <div class="c_info_content">
                <img class="c_icon" src="${ctx}/static/image/icon/taobao_logo.png">
                <span class="c_font_size_12" id="nickInfo"></span><br/>
                <span class="c_info_title" id="titleInfo"></span>
            </div>
            <div class="c_info_content">
                <div class="c_goods_info_col"><i class="fa fa-map-marker fa-lg" aria-hidden="true"></i>&nbsp;发货:
                    <span id="provcityInfo"></span>
                </div>
                <div class="c_goods_info_col"><i class="fa fa-signal fa-lg" aria-hidden="true"></i>&nbsp;月销:
                    <span id="volumeInfo"></span>
                </div>
            </div>
            <div class="c_div_10px"></div>
            <div class="c_info_text c_center_text">-----图文详情参见淘宝商品详情-----</div>
        </div>
        <div class="c_toolbar">
            <div class="c_toolbar_home"><i class="fa fa-home fa-lg" aria-hidden="true"></i>&nbsp;首页</div>
            <div class="c_toolbar_share" onclick="doShare()"><i class="fa fa-share-alt fa-lg" aria-hidden="true"></i>&nbsp;分享</div>
            <div class="c_toolbar_browser">浏览器打开</div>
            <div class="c_toolbar_buy">立即购买</div>
        </div>

        <!-- 模态框（Modal） -->
        <div class="modal fade" style="padding-left: 0px;" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div id="modalContent" class="modal-dialog" style="height: 100%; width: 100%; margin: 0; overflow: auto;">
                <div style="margin: 30px auto; padding: 10px; min-width: 300px; max-width: 500px;">
                    <div>
                        <img id="cccc" width="100%">
                    </div>
                    <div style="overflow: auto; background-color: #ffffff;">
                        <div style="float: left; width: calc(100% - 120px); padding: 10px;">
                            <div style="display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 2; overflow: hidden; margin-bottom: 10px;">[可可屋] 日本进口石塚硝子可口可乐玻璃杯水杯啤酒杯果汁饮料杯</div>
                            <span style="color: #ed6d27; font-size: 20px;">￥</span>
                            <span style="color: #ed6d27; font-size: 20px;">100</span>&nbsp;&nbsp;
                            <span style="color: #8c8c8c; font-size: 12px;">原价￥</span>
                            <span style="color: #8c8c8c; font-size: 12px;" class="c_info_disable">200</span><br/>
                            <span style="background-color: #ea521c; color: #ffffff; padding: 2px 5px; margin: 0; display:inline-block;">劵</span>
                            <span style="background-color: #eeeeee; color: #ea521c; padding: 2px 5px; margin: 0; display:inline-block;">满50减30</span>
                        </div>
                        <div style="float: left; width: 120px; padding: 10px;">
                            <img style="width: 100px; height: 100px;" src="${ctx}/static/image/jon-flobrant-229726-unsplash.jpg">
                        </div>
                    </div>
                    <div style="text-align: center; padding: 5px;">
                        <img onclick="doCloseShareImg()" style="width: 50px; cursor: pointer;" src="${ctx}/static/image/icon/close_icon.png">
                    </div>
                </div>
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

    </body>
    <script type="text/javascript" src="${ctx}/static/js/tb/goods_info.js"></script>
    <script type="text/javascript">
        var goodsId = ${goodsId};
    </script>
</html>