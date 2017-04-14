<html>
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>登录</title>
    <link rel="stylesheet" href="${request.contextPath}/frame/layui/css/layui.css">
    <link rel="stylesheet" href="${request.contextPath}/css/style.css">
</head>
<body class="login-body body">

<div class="login-box">
    <form class="layui-form layui-form-pane" method="post" action="${request.contextPath}/login.htm">
        <input type="hidden" name="returnUrl" value="${returnUrl}" />
        <div class="layui-form-item">
            <h3>xx后台登录系统</h3>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">账号：</label>

            <div class="layui-input-inline">
                <input type="text" name="account" class="layui-input" lay-verify="account" placeholder="账号" autocomplete="on" maxlength="20"/>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">密码：</label>

            <div class="layui-input-inline">
                <input type="password" name="password" class="layui-input" lay-verify="password" placeholder="密码" maxlength="20"/>
            </div>
        </div>
        <div class="layui-form-item">
            <button type="reset" class="layui-btn layui-btn-danger btn-reset">重置</button>
            <button id="sub" type="submit" class="layui-btn btn-submit" lay-submit="" lay-filter="sub">立即登录</button>
        </div>
    </form>
</div>

<script type="text/javascript" src="${request.contextPath}/frame/layui/layui.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/jquery-3.2.0.js"></script>
<script type="text/javascript">
    layui.use(['form', 'layer'], function () {
        var $ = layui.jquery,form = layui.form(),layer = layui.layer;
        // 验证
        form.verify({
            account: function (value) {
                if (value == "") {
                    return "请输入用户名";
                }
            },
            password: function (value) {
                if (value == "") {
                    return "请输入密码";
                }
            },
        });

        // 提交监听
        /*form.on('submit(sub)', function (data) {



            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            });

            return false;
        })*/

    })

</script>
</body>
</html>