package searchController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ESOperation.ESQueryBuilderConstructor;
import ESOperation.ESQueryBuilders;
import ESOperation.ElasticSearchService;

@WebServlet("/LineServlet")
public class LineServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static String old_name = "2017-07-10";
	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws IOException,ServletException
	{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		//因为查询一次很慢所以应该先开一个线程一直往文件里写先，再从文件里读
		 Date today = new Date();
		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		 String file_name ="2017-07-10"; //format.format(today.getTime());//得到今天的时间
		 String time[] = generate_time(7);//生成前几天的日期
		 int week[] =new int[7];//用来存储前几天的数据
		 File file= new File("D://Google下载",file_name+".txt");//创建文件
		 String count="";
		 char c[]=new char[100];
		 byte b[] = new byte[100];
		 
		if(old_name.equals(file_name))
		{
			 FileInputStream out = new FileInputStream(file);
			 InputStreamReader isr = new InputStreamReader(out); 
			  int ch = 0; 
			  int index = 0;
	            while ((ch = isr.read()) != -1)
	            {  
	            	c[index] = (char) ch;
	            	index++;
	            }
	            count =new String(c).substring(0,index);
	            String s[] = count.split("\\+");
			 for(int i =0;i<7;i++)
			 {
				week[i] = Integer.parseInt(s[i]);
			 }
		}
		else
		{
			//连接数据库
			String data="4062+234+432+3501+8876+9179+10000+";
			 ElasticSearchService service = new ElasticSearchService("maxwell", "117.78.37.227", 9300);  	 
			 for(int i=0;i<7;i++)
			 {
				 ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
				 constructor.must(new ESQueryBuilders().range("crawler_time", time[i+1], time[i]));
				 week[i] = service.searchCount("myindex", "mytype", constructor);
				 System.out.println(time[i]);
				 System.out.println(week[i]);
				 data = week[i]+"+";
			 }
			 
			 file.createNewFile();
			 FileOutputStream in = new FileOutputStream(file);
			 in.write(data.getBytes(), 0, data.length());
			 in.close();
		}


		
		 
		 for(int i=0;i<7;i++)
		 {
			 request.setAttribute(time[i+1].substring(0, 10),week[i]);
			 request.setAttribute(""+i,time[i+1].substring(0, 10));
		 }
		 
		 request.getRequestDispatcher("./line.jsp").forward(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException
	{
		doGet(request,response);
	}
	//返回前n天每天的时间
	public String[] generate_time(int n)
	{
		String[] time = new String[n+1];
		Calendar agoDate = Calendar.getInstance();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i=0;i<=n;i++)
		{
			time[i] = format.format(agoDate.getTime())+"T09:36:55.000+08:00";
			agoDate.add(Calendar.DATE, -1);
		}
		
		return time;
	}
}
