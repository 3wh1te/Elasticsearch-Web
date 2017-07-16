<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%> 
 <%@ page import = "java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Searcher，意想不到</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/index.css" rel="stylesheet" type="text/css" />
<style type="text/css" media="screen">
* html .btn { border: 3px double #aaa; }  
* html .btn.blue { border-color: #2ae; }  
* html .btn.green { border-color: #9d4; }  
* html .btn.pink { border-color: #e1a; }  
* html .btn:hover { border-color: #a00; }
.btn { display: block; position: relative; background: #aaa; padding: 5px; float: left; color: #fff; text-decoration: none; cursor: pointer; }  
.btn * { font-style: normal; background-image: url(btn2.png); background-repeat: no-repeat; display: block; position: relative; }  
.btn i { background-position: top left; position: absolute; margin-bottom: -5px;  top: 0; left: 0; width: 5px; height: 5px; }  
.btn span { background-position: bottom left; left: -5px; padding: 0 0 5px 10px; margin-bottom: -5px; }  
.btn span i { background-position: bottom right; margin-bottom: 0; position: absolute; left: 100%; width: 10px; height: 100%; top: 0; }  
.btn span span { background-position: top right; position: absolute; right: -10px; margin-left: 10px; top: -5px; height: 0; }
.btn.blue { background: #2ae; }  
</style>
</head>
<body>
<div id="container">
<form action="SearchServlet" method="post">
<input type="hidden" name="advanced" value="index"/>
	<div id="bd">
        <div id="main">
        	<h1 class="title">
            	<div class="logo large"></div>
            </h1>
            <div class="nav ue-clear">
            	<ul class="searchList">
                    <li class="searchItem current">新闻</li>
                    <li class="searchItem">体育</li>
                    <li class="searchItem">时尚</li>
                    <li class="searchItem">IT</li>
                    <li class="searchItem">资讯</li>
                    <li class="searchItem">军事</li>
                    <li class="searchItem">观察</li>
                    <li class="searchItem">国际</li>
                </ul>
            </div>
            <div class="inputArea">
				
            		<input type="text" class="searchInput" name="content"/>
                	<button type="submit" name="search" class="searchButton"></button>
				
                <a class="advanced" href="advanced.jsp">高级搜索</a>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
    
    <div class="foot">
    	<div class="wrap">
            <div class="copyright">Copyright &copy;searcher小组版权所有</div>
        </div>
    </div>
    </form>
    <div style="width:100%;margin-top:25%;">
    <form action="SectorServlet" method="post">
    <button  type="submit" name="stastics" class="btn blue">数据分类统计</button>
    </form>
     <form action="LineServlet" method="post">
    <button  type="submit" name="realtime"class="btn blue">实时数据统计</button>
    </form>

</div>
</body>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript">
	$('.searchList').on('click', '.searchItem', function(){
		$('.searchList .searchItem').removeClass('current');
		$(this).addClass('current');	
	});
	
	// 联想下拉显示隐藏
	$('.searchInput').on('focus', function(){
		$('.dataList').show()
    });
</script>
</html>