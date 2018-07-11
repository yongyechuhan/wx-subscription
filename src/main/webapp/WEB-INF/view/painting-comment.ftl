<html>
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

    <script src="${rc.contextPath}/js/base.js"></script>
</head>
<body>
<header data-am-widget="header" class="am-header am-header-default am-no-layout am-header-fixed">
    <div class="am-header-left am-header-nav">
        <a href="#left-link" onclick="urlDirection('${rc.contextPath}/showPaingtingIndex.do')">
            <span class="am-icon-angle-left am-icon-sm">&nbsp;返回</span>
        </a>
    </div>

    <h3 class="am-header-title">远方 有一个地方 那里种有我们的梦想</h3>
</header>
<section class="am-g am-g-collapse">
    <div class="am-panel-primary">
        <div class="am-container am-u-sm-centered">
            <img src="http://s.amazeui.org/media/i/demos/pure-1.jpg?imageView2/0/w/640"
                 class="am-img-responsive"
                 data-rel="http://s.amazeui.org/media/i/demos/pure-1.jpg" alt="春天的花开秋天的风以及冬天的落阳"/>
        </div>
        <div class="am-panel-bd">
            <div class="am-gallery-desc am-g am-g-collapse">
                <div class="am-u-sm-6 am-gallery-desc">上传：永夜初晗</div>
                <div class="am-u-sm-6 am-text-right">2375-09-26</div>
            </div>
        </div>
    </div>
    <hr data-am-widget="divider" style="" class="am-divider am-divider-default" />
    <div class="am-u-sm-12 am-u-sm-centered">
        <div class="am-panel-bd">
            <ul class="am-comments-list am-comments-list-flip">
                <li class="am-comment am-comment-primary">
                    <a href="#link-to-user-home" onclick="">
                        <img src="http://s.amazeui.org/media/i/demos/bw-2014-06-19.jpg?imageView/1/w/96/h/96" alt=""
                             class="am-comment-avatar" width="48" height="48"/>
                    </a>

                    <div class="am-comment-main">
                        <header class="am-comment-hd">
                            <div class="am-comment-meta">
                                <a href="#link-to-user" class="am-comment-author">某人</a>
                                评论于
                                <time datetime="2013-07-27T04:54:29-07:00" title="2013年7月27日 下午7:54 格林尼治标准时间+0800">
                                    2014-7-12 15:30
                                </time>
                            </div>
                        </header>

                        <div class="am-comment-bd">
                            ...
                        </div>
                    </div>
                </li>
                <li class="am-comment am-comment-primary">
                    <a href="#link-to-user-home">
                        <img src="http://s.amazeui.org/media/i/demos/bw-2014-06-19.jpg?imageView/1/w/96/h/96" alt=""
                             class="am-comment-avatar" width="48" height="48"/>
                    </a>

                    <div class="am-comment-main">
                        <header class="am-comment-hd">
                            <div class="am-comment-meta">
                                <a href="#link-to-user" class="am-comment-author">某人</a>
                                评论于
                                <time datetime="2013-07-27T04:54:29-07:00" title="2013年7月27日 下午7:54 格林尼治标准时间+0800">
                                    2014-7-12 15:30
                                </time>
                            </div>
                        </header>

                        <div class="am-comment-bd">
                            ...
                        </div>
                    </div>
                </li>
                <li class="am-comment am-comment-primary">
                    <a href="#link-to-user-home">
                        <img src="http://s.amazeui.org/media/i/demos/bw-2014-06-19.jpg?imageView/1/w/96/h/96" alt=""
                             class="am-comment-avatar" width="48" height="48"/>
                    </a>

                    <div class="am-comment-main">
                        <header class="am-comment-hd">
                            <div class="am-comment-meta">
                                <a href="#link-to-user" class="am-comment-author">某人</a>
                                评论于
                                <time datetime="2013-07-27T04:54:29-07:00" title="2013年7月27日 下午7:54 格林尼治标准时间+0800">
                                    2014-7-12 15:30
                                </time>
                            </div>
                        </header>

                        <div class="am-comment-bd">
                            ...
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <footer data-am-widget="footer" class="am-panel-footer">
        <div class="am-footer-switch am-u-sm-centered am-input-group am-input-group-primary">
            <form class="am-form">
                <div class="am-u-sm-7"><input type="text" class="am-form-field am-round am-input-sm"
                                              placeholder="请填写你的评论"></div>
                <div class="am-u-sm-3">
                    <button type="button" class="am-btn am-btn-primary am-round am-btn-sm">发表</button>
                </div>
            </form>
        </div>
    </footer>
</body>
</html>