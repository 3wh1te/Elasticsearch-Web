<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%> 
 <%@ page import = "java.util.*" %>
 <%!
 	String searchContent; 
 	int pageSize=9;//每页显示多少条记录
 	int pageNow;//希望显示第几页
 	int pageCount;//一共有多少页
 	int rowCount;//一共有多少条记录
 	String result,page1;
 %>
 <%
 	searchContent = request.getParameter("content"); 
 	result = (String)request.getAttribute("result");
 	rowCount = (int)request.getAttribute("rowCount");
 	pageCount = rowCount/pageSize+1;
 	page1 = (String)request.getAttribute("page1");
 %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%out.print(searchContent); %>_Searcher 搜索</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/result.css" rel="stylesheet" type="text/css" />
</head>
<body>

<div id="container">
<form action="SearchServlet" method="post">
<input type="hidden" name="advanced" value="result"/>
	<div id="hd" class="ue-clear">
    	<a href="index.jsp"><div class="logo"></div></a>
        <div class="inputArea">
        	<input type="text" name="content"class="searchInput" value="<%=searchContent %>"/>
            <button type="submit" class="searchButton" ></button>
            <a class="advanced" href="advanced.jsp">高级搜索</a>
        </div>
    </div>
    <div class="nav">
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
        <a href="javascript:;" class="tips">什么是分类搜索</a>
    </div>
	<div id="bd" class="ue-clear">
        <div id="main">
        	<div class="sideBar">
                <div class="subfield">搜索引擎</div>
                <ul class="subfieldContext">
                		<li>
                    	<span class="name"><a href="https://www.baidu.com/">百度搜索</a></span>
                    </li>
                    <li>
                    	<span class="name"><a href="https://c.gufen.gq/">Google</a></span>
                    </li>
                    <li>
                    	<span class="name"><a href="https://www.so.com/">360搜索</a></span>
                    </li>
                    <li>
                    	<span class="name"><a href="https://www.sogou.com/?rfrom=soso">搜搜</a></span>
                    </li>
                    <li class="more">
                    	<a href="javascript:;">
                        	<span class="text">更多</span>
                        	<i class="moreIcon"></i>
                        </a>
                    </li>
                </ul>
                
                <div class="subfield">您要找的是不是</div>
                <ul class="subfieldContext">
                	<li>
                    	<span class="name"><%out.print(searchContent); %></span>
                    </li>
                    <li>
                    	<span class="name"></span>
                    </li>
                </ul>
                
              
                
                <div class="sideBarShowHide">
                	<a href="javascript:;" class="icon"></a>
                </div>
            </div>
            <div class="resultArea">
            	<p class="resultTotal">
                	<span class="info">找到约&nbsp;<span class="totalResult"><%=rowCount %></span>&nbsp;条结果<span class="totalPage"><%=pageSize %></span>页</span>
                    <span class="orderOpt">
                    	<a href="javascript:;" class="byTime">按时间排序</a>
                        <a href="javascript:;" class="byDependence">按相关度排序</a>
                    </span>
                </p>
                <div class="resultList">
                <!--标题、url、简介、相关度、时间 -->
							<%= page1%>
			</div>
							<!-- 分页 -->
                		<div class="pagination ue-clear"></div>
                <!-- 相关搜索-->
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
    </form>
</div>

<div id="foot">Copyright &copy;searcher小组</div>
</body>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/pagination.js"></script>
<script type="text/javascript">
	$('.searchList').on('click', '.searchItem', function(){
		$('.searchList .searchItem').removeClass('current');
		$(this).addClass('current');	
	});
	
	
	
	$.each($('.subfieldContext'), function(i, item){
		$(this).find('li:gt(2)').hide().end().find('li:last').show();		
	});
	
	$('.subfieldContext .more').click(function(e){
		var $more = $(this).parent('.subfieldContext').find('.more');
		if($more.hasClass('show')){
			
			if($(this).hasClass('define')){
				$(this).parent('.subfieldContext').find('.more').removeClass('show').find('.text').text("自定义");
			}else{
				$(this).parent('.subfieldContext').find('.more').removeClass('show').find('.text').text("更多");	
			}
			$(this).parent('.subfieldContext').find('li:gt(2)').hide().end().find('li:last').show();
	    }else{
			$(this).parent('.subfieldContext').find('.more').addClass('show').find('.text').text("收起");
			$(this).parent('.subfieldContext').find('li:gt(2)').show();	
		}
		
	});
	
	$('.sideBarShowHide a').click(function(e) {
		if($('#main').hasClass('sideBarHide')){
			$('#main').removeClass('sideBarHide');
			$('#container').removeClass('sideBarHide');
		}else{
			$('#main').addClass('sideBarHide');	
			$('#container').addClass('sideBarHide');
		}
        
    });
	
	//分页
	$(".pagination").pagination(<%=rowCount%>, {
		current_page :0, //当前页码
		items_per_page :9,
		display_msg :true,
		callback :pageselectCallback
	});
	function pageselectCallback(page_id, jq) {
	var pageContent = "<%=result%>";
	var page = pageContent.split("```");
		$(".resultList").html(page[page_id]);
	}
	
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