<html xmlns="http://www.w3.org/1999/html">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="">
        <meta name="keywords" content="">
        <meta name="viewport"
              content="width=device-width, initial-scale=1">
        <title>Amaze UI Examples</title>

        <!-- Set render engine for 360 browser -->
        <meta name="renderer" content="webkit">

        <!-- No Baidu Siteapp-->
        <meta http-equiv="Cache-Control" content="no-siteapp"/>

        <!-- Add to homescreen for Chrome on Android -->
        <meta name="mobile-web-app-capable" content="yes">

        <!-- Add to homescreen for Safari on iOS -->
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="apple-mobile-web-app-title" content="Amaze UI"/>

        <!-- Tile icon for Win8 (144x144 + tile color) -->
        <meta name="msapplication-TileImage" content="${rc.contextPath}/assets/i/app-icon72x72@2x.png">
        <meta name="msapplication-TileColor" content="#0e90d2">

        <link rel="stylesheet" href="${rc.contextPath}/assets/css/amazeui.min.css">
        <link rel="stylesheet" href="${rc.contextPath}/assets/css/app.css">

        <script src="${rc.contextPath}/assets/js/jquery-2.0.0.js"></script>
        <script src="${rc.contextPath}/assets/js/amazeui.js"></script>
        <script src="${rc.contextPath}/js/base.js"></script>
        <script src="${rc.contextPath}/assets/js/jweixin-1.0.0.js"></script>

        <script type="text/javascript">
            $(document).ready(function(){
                wx.config({
                    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: '${weixinJDK.appId}', // 必填，公众号的唯一标识
                    timestamp: '${weixinJDK.timestamp}', // 必填，生成签名的时间戳
                    nonceStr:  '${weixinJDK.nonceStr}',// 必填，生成签名的随机串
                    signature: '${weixinJDK.signature}',// 必填，签名
                    jsApiList: ['chooseImage','uploadImage'] // 必填，需要使用的JS接口列表
                });

                wx.error(function(res){
                    console.log("微信jdk-api验证失败："+res);
                });
            })

            function replaceImg(picLocalIds){
                var panelImg = $('#panelImg');
                var iconSpan = panelImg.children("span");

                panelImg.removeAttr("style");
                panelImg.css({"width":"100%","height":"100%"});
                iconSpan.remove();

                var img = $("<img src='"+picLocalIds+"' style='width:100%;height:100%;'/>");
                panelImg.append(img);
            }

            function selectPic(){
                wx.chooseImage({
                    count: 1, // 默认9
                    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (res) {
                        var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        if(localIds == null){
                            alert("读取图片失败，请稍后再试！");
                        } else {
                            replaceImg(localIds);
                            $('#uploadButton').removeAttr("disabled");
                        }
                    }
                });
            }
            
            function uploadPic() {

            }
        </script>
    </head>
    <body>
        <div class="am-g am-g-collapse">
            <div class="am-panel-default" style="width:100%;height: 280px;" onclick="selectPic()">
                <div id="panelImg" style="left:46%; top:120px;position:absolute;">
                    <span class="am-icon-plus am-icon-lg"/>
                </div>
            </div>

            <div class="am-panel am-panel-primary">
                <div class="am-panel-hd">图片编辑</div>
                <div class="am-panel-bd">
                    <form action="return uploadPic()" class="am-form" data-am-validator>
                        <fieldset>
                            <div class="am-form-group">
                                <label for="doc-vld-name-2">名称：</label>
                                <input type="text" id="picName" minlength="3" placeholder="输入图片名（至少3个字符）" required/>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-vld-ta-2">图片描述：</label>
                                <textarea id="picDesc" minlength="5" maxlength="100" placeholder="输入你对图片的描述（至少5个字符）"></textarea>
                            </div>

                            <button id="uploadButton" class="am-btn am-btn-secondary" type="submit" disabled="disabled">提交</button>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>