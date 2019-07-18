<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>二维码测试页面</title>
        <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no" />
        <script type="text/javascript" src="${ctx}/static/js/jquery-1.10.2.min.js"></script>
        <script type="text/javascript" src="https://cdn.bootcss.com/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
        <script type="text/javascript">var ctx = '${ctx}'</script>
    </head>
    <body>
        <div style="font-size: xx-large; font-weight: bolder; text-align: center">二维码</div>
        <div id="qrcode"></div>
    </body>
    <script type="text/javascript">
        $(function(){
            var url= "www.baidu.com";
            //默认使用Canvas生成，并显示到图片
            jQuery('#qrcode').qrcode({
                render: "canvas",
                width       : 100,     //设置宽度
                height      : 100,     //设置高度
                typeNumber  : -1,      //计算模式
                correctLevel:2,
                text: url
            });
        });
    </script>
</html>
