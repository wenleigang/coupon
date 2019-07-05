<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>商品列表</title>
        <meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1,user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <script type="text/javascript">var ctx = '${ctx}'</script>
        <script type="text/javascript" src="${ctx}/static/js/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="${ctx}/static/plugins/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${ctx}/static/plugins/swiper-4.5.0/js/swiper.min.js"></script>
        <script type="text/javascript" src="${ctx}/static/plugins/mui/js/mui.min.js"></script>
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/bootstrap-3.3.7-dist/css/bootstrap.css"/>
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/bootstrap-3.3.7-dist/css/bootstrap-theme.css"/>
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/font-awesome-4.7.0/css/font-awesome.css"/>
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/swiper-4.5.0/css/swiper.min.css">
        <link type="text/css" rel="stylesheet" href="${ctx}/static/plugins/mui/css/mui.min.css">
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
            .c_header {
                width: 100%;
                top: 0px;
                height: 54px;
                position: fixed;
                background-color: #ff5000;
                overflow: auto;
            }
            .c_search {
                width: 100%;
                height: 40px;
                margin-top: 7px;
                padding: 10px;
                border-radius: 10px;
                background-color: #efeff4;
                border: 1px solid #ffffff;
                margin-bottom: 0 !important;
            }
            .mui-content>.mui-table-view:first-child {
                margin-top: -1px;
            }
            .c_item {height: 130px; background-color: #ffffff; margin: 8px; border-radius: 8px; overflow: auto;}
            .c_header_icon {float: left; width: 13%; text-align: center;}
            .c_header_input {float: left; width: 74%;}
            .c_header_icon_logo {width: 40px; margin-top: 7px;}
            .c_header_icon_search {width: 36px; margin-top: 9px;}
            .c_content_empty {padding: 200px 0; margin-top: 54px;}
            .c_center {text-align: center;}
            .c_info_color {color: #9e9e9e;}
            .c_empty_img {width: 70px;}
        </style>
    </head>
    <body class="c_container">
        <div class="c_header">
            <div class="c_header_icon">
                <img class="c_header_icon_logo" src="${ctx}/static/image/icon/cash_coupon.png">
            </div>
            <div class="c_header_input">
                <input class="c_search" type="text" placeholder="请输入">
            </div>
            <div class="c_header_icon">
                <img class="c_header_icon_search" src="${ctx}/static/image/icon/search_fill.png">
            </div>
        </div>
        <div class="c_content_empty">
            <div class="c_center" style="text-align: center;">
                <img class="c_empty_img" src="${ctx}/static/image/icon/empty-box.png">
            </div>
            <div class="c_center c_info_color">没有找到相关的宝贝呦~</div>
        </div>
        <!--下拉刷新容器-->
        <div id="pullrefresh" class="mui-content mui-scroll-wrapper" style="display:none;">
            <div class="mui-scroll">
                <!--数据列表-->
                <ul class="mui-table-view mui-table-view-chevron" style="background-color: #efeff4;"></ul>
            </div>
        </div>
        <script>
            mui.init({
                pullRefresh: {
                    container: '#pullrefresh',
                    down: {
                        style:'circle',
                        callback: pulldownRefresh
                    },
                    up: {
                        auto:true,
                        contentrefresh: '正在加载...',
                        callback: pullupRefresh
                    }
                }
            });

            var count = 3;
            function pullupRefresh() {
                setTimeout(function() {
                    mui('#pullrefresh').pullRefresh().endPullupToRefresh((++count > 2)); //参数为true代表没有更多数据了。
                    var table = document.body.querySelector('.mui-table-view');
                    var cells = document.body.querySelectorAll('.mui-table-view-cell');
                    var newCount = cells.length>0?5:5;//首次加载5条，满屏
                    for (var i = cells.length, len = i + newCount; i < len; i++) {
                        var div = document.createElement('div');
                        div.className = 'c_item';
                        div.innerHTML = '<div style="float: left; width: 130px; padding: 5px; border-radius: 5px;">\n' +
                            '                        <img style="width: 120px; height: 120px;" src="${ctx}/static/image/icon/item_upload.png">\n' +
                            '                    </div>\n' +
                            '                    <div style="float: left; width: calc(100% - 130px); padding: 5px; border-radius: 5px;">\n' +
                            '                        2222\n' +
                            '                    </div>';
                        table.appendChild(div);
                    }
                }, 1500);
            }

            function addData() {
                var table = document.body.querySelector('.mui-table-view');
                var cells = document.body.querySelectorAll('.mui-table-view-cell');
                for(var i = cells.length, len = i + 5; i < len; i++) {
                    var div = document.createElement('div');
                    div.className = 'c_item';
                    div.innerHTML = '<div style="float: left; width: 130px; padding: 5px; border-radius: 5px;">\n' +
                        '                        <img style="width: 120px; height: 120px;" src="${ctx}/static/image/icon/item_upload.png">\n' +
                        '                    </div>\n' +
                        '                    <div style="float: left; width: calc(100% - 130px); padding: 5px; border-radius: 5px;">\n' +
                        '                        2222\n' +
                        '                    </div>';
                    //下拉刷新，新纪录插到最前面；
                    table.insertBefore(div, table.firstChild);
                }
            }
            /**
             * 下拉刷新具体业务实现
             */
            function pulldownRefresh() {
                setTimeout(function() {
                    addData();
                    mui('#pullrefresh').pullRefresh().endPulldownToRefresh();
                    mui.toast("为你推荐了5篇文章");
                }, 1500);
            }
        </script>
    </body>
</html>