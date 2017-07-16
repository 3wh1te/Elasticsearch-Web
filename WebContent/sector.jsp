<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%!
	int news=0,it=0,sports=0,msg=0,military=0,watch=0,internation=0,fashion=0;
	int ch =0,ko=0,jap=0,eng=0;
%>
<% 
	news =(Integer)request.getAttribute("新闻");
	it =(Integer)request.getAttribute("IT");
	msg =(Integer)request.getAttribute("资讯");
	military =(Integer)request.getAttribute("军事");
	sports =(Integer)request.getAttribute("体育");
	watch =(Integer)request.getAttribute("观察");
	internation =(Integer)request.getAttribute("国际");
	fashion =(Integer)request.getAttribute("时尚");
	ch =(Integer)request.getAttribute("中文");
	ko =(Integer)request.getAttribute("韩语");
	jap =(Integer)request.getAttribute("日语");
	eng =(Integer)request.getAttribute("英语");
	
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="js/echarts.min.js"></script>
<title>数据统计</title>
</head>
<body style="text-align:center;">
 <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
 
 <div style="width:100%;margin-top:5%;">
 
 	<div id="main" style="width: 50%;height:600px;text-align:center;float:left;"></div>
    
    <div id="main2" style="width: 50%;height:600px;background-color:#000000;float:left;"></div>
    
 </div>
    
   
    <script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    // 指定图表的配置项和数据
   option = {
   	title : {
        text: '类型分布图',
        subtext: 'Search搜索',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: ['新闻','IT','体育','时尚','资讯','军事','观察','国际']
    },
    series : [
        {
            name: '分类',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
                {value:<%=news%>, name:'新闻'},
                {value:<%=it%>, name:'IT'},
                {value:<%=sports%>, name:'体育'},
                {value:<%=fashion%>, name:'时尚'},
                {value:<%=msg%>, name:'资讯'},
                {value:<%=military%>, name:'军事'},
                {value:<%=watch%>, name:'观察'},
                {value:<%=internation%>, name:'国际'}
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
        
    ]
};

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

     </script>
     
     <script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart2 = echarts.init(document.getElementById('main2'));
    // 指定图表的配置项和数据
   option2 = {
   	title : {
        text: '语种分布统计',
        subtext: 'Searcher搜索',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: ['中文','韩语','英语','日语']
    },
    series : [
        {
            name: '语种',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
				{value:<%=ch%>, name:'中文'},
                {value:<%=ko%>, name:'韩语'},
                {value:<%=jap%>, name:'日语'},
                {value:<%=eng%>, name:'英语'}     
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
        
    ]
};

    // 使用刚指定的配置项和数据显示图表。
    myChart2.setOption(option2);

     </script>
     
</body>
</html>