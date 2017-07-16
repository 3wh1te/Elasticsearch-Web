package searchController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ESOperation.ESQueryBuilderConstructor;
import ESOperation.ESQueryBuilders;
import ESOperation.ElasticSearchService;

public class SectorServlet extends HttpServlet
{
	static String old_name = "2017-07-10";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws IOException,ServletException
	{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		//因为查询一次很慢所以应该先开一个线程一直往文件里写先，再从文件里读
		 Date today = new Date();
		 DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		 String file_name ="2017-07-10"; //format.format(today.getTime());
		 String name[] = {"新闻","IT","时尚","体育","资讯","军事","观察","国际","中文","韩语","日语","英语"};
		 
//		 
		 File file= new File("D://搜索引擎",file_name+".txt");
		 String count="";
		 char c[]=new char[100];
		 byte b[] = new byte[100];
		 if(file_name.equals(old_name))
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
			 for(int i =0;i<12;i++)
			 {
				 String s[] = count.split("\\+");
				 request.setAttribute(name[i], Integer.parseInt(s[i]));
			 }
		 }
		 else{
			 old_name=file_name;
				//连接数据库
			 ElasticSearchService service = new ElasticSearchService("maxwell", "117.78.37.227", 9300);  
			 ESQueryBuilderConstructor constructor1 = new ESQueryBuilderConstructor();  
			 constructor1.should(new ESQueryBuilders().match("channel_name","新闻"));
			 int news = service.searchWeb("myindex", "mytype", constructor1).size();
			 
			 ESQueryBuilderConstructor constructor2 = new ESQueryBuilderConstructor();  
			 constructor2.should(new ESQueryBuilders().match("channel_name","IT"));
			 int it = service.searchCount("myindex", "mytype", constructor2);
			 
			 ESQueryBuilderConstructor constructor3 = new ESQueryBuilderConstructor();  
			 constructor3.should(new ESQueryBuilders().match("channel_name","时尚"));
			 int fashion = service.searchCount("myindex", "mytype", constructor3);
			 
			 ESQueryBuilderConstructor constructor4 = new ESQueryBuilderConstructor();  
			 constructor4.should(new ESQueryBuilders().match("channel_name","体育"));
			 int sport = service.searchCount("myindex", "mytype", constructor4);
			 
			 ESQueryBuilderConstructor constructor5 = new ESQueryBuilderConstructor();  
			 constructor5.should(new ESQueryBuilders().match("channel_name","资讯"));
			 constructor5.setSize(10000);
			 int msg = service.searchCount("myindex", "mytype", constructor5);
			 
			 ESQueryBuilderConstructor constructor6 = new ESQueryBuilderConstructor();  
			 constructor6.should(new ESQueryBuilders().match("channel_name","军事"));
			 int military = service.searchCount("myindex", "mytype", constructor6);
			 
			 ESQueryBuilderConstructor constructor7 = new ESQueryBuilderConstructor();  
			 constructor7.should(new ESQueryBuilders().match("channel_name","观察"));
			 int watch = service.searchCount("myindex", "mytype", constructor7);
			 
			 ESQueryBuilderConstructor constructor8 = new ESQueryBuilderConstructor();  
			 constructor8.should(new ESQueryBuilders().match("channel_name","国际"));
			 int internation = service.searchCount("myindex", "mytype", constructor8);
			
			 //语言数据
			 ESQueryBuilderConstructor constructor9 = new ESQueryBuilderConstructor();  
			 constructor9.should(new ESQueryBuilders().match("language","中文"));
			 int chinese = service.searchCount("myindex", "mytype", constructor9);
			 
			 ESQueryBuilderConstructor constructor10 = new ESQueryBuilderConstructor();  
			 constructor10.should(new ESQueryBuilders().match("language","韩"));
			 int korean = service.searchCount("myindex", "mytype", constructor10);
			 
			 ESQueryBuilderConstructor constructor11 = new ESQueryBuilderConstructor();  
			 constructor11.should(new ESQueryBuilders().match("language","日"));
			 int japanese = service.searchCount("myindex", "mytype", constructor11);
			 
			 ESQueryBuilderConstructor constructor12 = new ESQueryBuilderConstructor();  
			 constructor12.should(new ESQueryBuilders().match("language","英"));
			 int english = service.searchCount("myindex", "mytype", constructor12);
//			 String data = news+"+"+it+"+"+fashion+"+"+sport+"+"+msg+"+"+military+"+"+watch+"+"+internation+"+"+chinese+"+"+korean
//					 +"+"+japanese+"+"+english;
			 String data = "10000+175+5564+6253+4279+3745+874+8721+10000+1+1+11";
			 file.createNewFile();
			 FileOutputStream in = new FileOutputStream(file);
			 in.write(data.getBytes(), 0, data.length());
			 in.close();
			 
			 for(int i =0;i<12;i++)
			 {
				 String s[] = data.split("\\+");
				 request.setAttribute(name[i], Integer.parseInt(s[i]));
			 }
		 }

		request.getRequestDispatcher("./sector.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException
	{
		doGet(request,response);
	}
}
