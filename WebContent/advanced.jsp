<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%> 
 <%@ page import = "java.util.*" %>
 <%!
	String searchContent; 
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级搜素-Searcher搜索</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/advanced.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="container">
<form action="SearchServlet" method="post">
<input type="hidden" name="advanced" value="advanced"/>
	<div id="hd" class="ue-clear">
    	<div class="logo"></div>
        <div class="inputArea">
        	<input type="text" name="content"class="searchInput" />
            <button type="submit" class="searchButton"></button>
            <a class="back" href="index.jsp">返回主页</a>
        </div>
    </div>
    <div class="divsion"></div>
	<div id="bd">
        <div id="main">
        	<div class="subfield">高级搜索</div>
            <div class="subfieldContent">
            	<!--搜索范围-->
                <dl class="ue-clear advanceItem">
                	<dd>
                    	<label>搜索范围</label>
                        <span>选择要搜索的范围</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input name="scope" type="checkbox" checked="checked"value="title">
                            <span class="text">标题</span>
                        </span>
                        <span class="choose">
                        <input type="checkbox" name="scope" value="content">
                            <span class="text">正文</span>
                        </span>
                    </dt>
                </dl>
                
                <!--搜索关键字-->
                <dl class="ue-clear advanceItem keyWords">
                	<dd>
                    	<label>搜索关键字</label>
                        <div class="tips">
                        	<p class="tip">包含以下<span class="impInfo">全部</span>的关键</p>
                            <p class="tip">包含以下的<span class="impInfo">完整关键词</span></p>
                            <p class="tip">包含以下<span class="impInfo">任意一个</span>关键词</p>
                            <p class="tip"><span class="impInfo">不包括</span>以下关键词</p>
                        </div>
                    </dd>
                    <dt class="fillInArea">
                    	<p><input type="text" name="all" /></p>
                        <p><input type="text" name="integer"/></p>
                        <p><input type="text" name="any"/></p>
                        <p><input type="text" name="not"/></p>
                    </dt>
                </dl>
                
                <!--类型-->
                <dl class="ue-clear advanceItem">
                	<dd>
                    	<label>类型</label>
                        <span>指定类型</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input name="type" type="checkbox" checked="checked" value="资讯">
                            <span class="text">资讯</span>
                        </span>
                        <span class="choose">
                        <input type="checkbox" name="type" value="新闻">
                            <span class="text">新闻</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type"value="体育">
                            <span class="text">体育</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type"value="IT">
                            <span class="text">IT</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type"value="军事">
                            <span class="text">军事 </span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type"value="观察">
                            <span class="text">观察</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type"value="国际">
                            <span class="text">国际</span>
                        </span>
                    </dt>
                </dl>
                
                <!--发布时间-->
                <dl class="ue-clear advanceItem time">
                	<dd>
                    	<label>发布时间</label>
                        <span>指定搜索的时间范围</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input type="radio" name="time" value="all">
                            <span class="text">全部</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time"value="day">
                            <span class="text">近一天</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time"value="week">
                            <span class="text">近一周</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time"value="month">
                            <span class="text">近一月</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time"value="year">
                            <span class="text">近一年</span>
                        </span>
                        <span class="define">
                            <input type="text" />
                            <span class="divsion">-</span>
                            <input type="text" />
                        </span>
                    </dt>
                </dl>
                <div class="button">
                	<button type="submit" class="search" value="" >立刻搜索</button>
                </div>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
    
  </form>  
</div>
<div id="foot">Copyright &copy;uimaker.com 版权所有  E-mail:admin@uimaker.com</div>
</body>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript">
	$('.defineRadio input[type=radio]').click(function(e) {
        if($(this).prop('checked')){
			$('.define').show();
		}
    });
	
	$('.time input[type=radio]').click(function(){
		if(!$(this).parent().hasClass('defineRadio')){
			$('.define').hide();
		}	
	});
	
	$('.part input[type=checkbox]:gt(3)').parent().hide();
	$('.part .more').toggle(function(e) {
		$(this).addClass('show').find('.text').text('收起');
        $('.part input[type=checkbox]:gt(3)').parent().show();
    },function(){
		$(this).removeClass('show').find('.text').text('更多');
		$('.part input[type=checkbox]:gt(3)').parent().hide();	
	});
	
	setHeight();
	$(window).resize(function(){
		setHeight();	
	});
	
	function setHeight(){
		if($('#container').outerHeight() < $(window).height()){
			$('#container').height($(window).height()-33);
		}	
	}
</script>
</html>