<html>
<head>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>成绩查询列表</title>
    <link rel="stylesheet" href="${request.contextPath}/frame/layui/css/layui.css">
</head>
<body class="body" style="width: 60%; margin-left: 20%">


<div class="my-btn-box" style=" margin-top: 5%">

    <form class="layui-form" action="">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">考试编号</label>
                <div class="layui-input-inline">
                    <input type="tel" name="phone" lay-verify="phone" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">学校编号</label>
                <div class="layui-input-inline">
                    <input type="text" name="email" lay-verify="email" autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <button class="layui-btn layui-btn-radius layui-btn-small" style="margin-left: 70%"> &nbsp;查&nbsp;询&nbsp; </button>
    </form>
</div>
<br/><br/>
<table class="layui-table" lay-even="" lay-skin="row" >
    <colgroup>
        <col width="150">
        <col width="150">
        <col width="200">
        <col>
    </colgroup>
    <thead>
    <tr>
        <th>考试编号</th>
        <th>学校编号</th>
        <th>成绩</th>
        <th>指导老师</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>201703210001</td>
        <td>002</td>
        <td>85</td>
        <td>张三丰</td>
    </tr>
    <tr>
        <td>201703210002</td>
        <td>002</td>
        <td>90</td>
        <td>张无忌</td>
    </tr>
    <tr>
        <td>201703210003</td>
        <td>002</td>
        <td>88</td>
        <td>杨过</td>
    </tr>
    <tr>
        <td>201703210004</td>
        <td>002</td>
        <td>96</td>
        <td>郭靖</td>
    </tr>
    <tr>
        <td>201703210005</td>
        <td>002</td>
        <td>70</td>
        <td>灭绝师太</td>
    </tr>
    </tbody>
</table>

<div id="demo5" style="margin-left: 20%"></div>

<script type="text/javascript" src="${request.contextPath}/frame/layui/layui.js"></script>
<script type="text/javascript" src="${request.contextPath}/js/jquery-3.2.0.js"></script>

<script type="text/javascript">
    layui.use(['laypage', 'layer'], function(){
        var laypage = layui.laypage
            ,layer = layui.layer;

        laypage({
            cont: 'demo5'
            ,pages: 100
            ,curr: location.hash.replace('#!fenye=', '') //获取hash值为fenye的当前页
            ,hash: 'fenye' //自定义hash值
        });

    });
</script>
</body>
</html>
