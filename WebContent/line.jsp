<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%!
	String time[]=new String[7];
	int week[] =new int[7];
%>
 <%
	for(int i=0;i<7;i++)
	 {
	 	time[i] = (String)request.getAttribute(""+i);
	 	week[i] = (Integer)request.getAttribute(time[i]);
	 } 
 %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="js/echarts.min.js"></script> 
<title>近一周数据采集量统计图</title>
</head>
<body>
 <div style="width:100%;margin-top:8%;text-align:center;">
 	<div id="main" style="width: 900px;height:500px;margin:0 auto;"></div>
</div>>   
 <script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    // 指定图表的配置项和数据
option = {
    title: {
        text: '近一周数据采集量统计图'
    },
    tooltip : {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            label: {
                backgroundColor: '#6a7985'
            }
        }
    },
    legend: {
        data:['采集量']
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : ['<%=time[6]%>','<%=time[5]%>','<%=time[4]%>','<%=time[3]%>','<%=time[2]%>','<%=time[1]%>','<%=time[0]%>'] 
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'采集量',
            type:'line',
            stack: '总量',
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            },
            areaStyle: {normal: {}},
            data:[<%=week[6]%>, <%=week[5]%>, <%=week[4]%>, <%=week[3]%>, <%=week[2]%>, <%=week[1]%>, <%=week[0]%>]
        }
    ]
};


    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

     </script>
</body>
</html>