package searchController;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import ESOperation.ESQueryBuilderConstructor;
import ESOperation.ESQueryBuilders;
import ESOperation.ElasticSearchService;
import Model.WebPage;


//@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request,HttpServletResponse response)
			throws IOException,ServletException
	{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		//连接数据库
		 ElasticSearchService service = new ElasticSearchService("maxwell", "117.78.37.227", 9300);  
		 ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();  
		 constructor.should(new ESQueryBuilders().queryString(request.getParameter("content")));
		 constructor.should(new ESQueryBuilders().match("title",request.getParameter("content")));
		 constructor.should(new ESQueryBuilders().match("content",request.getParameter("content")));
		 constructor.should(new ESQueryBuilders().match("author",request.getParameter("content")));
		 constructor.should(new ESQueryBuilders().match("url",request.getParameter("content")));
		 constructor.setSize(200);
		 
		 //高级搜索
		 if(request.getParameter("advanced").equals("advanced"))
		 {
			 //参数
			 String[] field =request.getParameterValues("scope");
			 String time=request.getParameter("time");
			 String any=request.getParameter("any");
			 String not=request.getParameter("not");
			 String all=request.getParameter("all");
			 String integer=request.getParameter("integer");
			 String[] type=request.getParameterValues("type");
			 if(type!=null)
			 {
				 for(String t:type)
				 {
					constructor.must(new ESQueryBuilders().match("channel_name",t));
				 }
			 }
			 //日期范围
			 if(time!=null)
			 {
				
				Date date=new Date();
				Calendar agoDate = Calendar.getInstance();
				DateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				DateFormat format1=new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
				String now=format.format(date);
				String now1=format1.format(date);
				System.out.println(now1);
				 if(time.equals("all"));
				if(time.equals("year"))
				{
					agoDate.add(Calendar.YEAR, -1);
					String ago_time = format.format( agoDate.getTime());
					constructor.must(new ESQueryBuilders().range("pubtime", ago_time, now));
				}
				if(time.equals("month"))
				{
					agoDate.add(Calendar.MONTH, -1);
					String ago_time = format.format( agoDate.getTime());
					String ago_time1 = format1.format( agoDate.getTime());	
					constructor.should(new ESQueryBuilders().range("pubtime", ago_time1, now1));
					constructor.should(new ESQueryBuilders().range("pubtime", ago_time, now));
				}
				if(time.equals("week"))
				{
					agoDate.add(Calendar.DATE, -7);
					String ago_time = format.format( agoDate.getTime());
					String ago_time1 = format1.format( agoDate.getTime());	
					constructor.should(new ESQueryBuilders().range("pubtime", ago_time1, now1));
					constructor.should(new ESQueryBuilders().range("pubtime", ago_time, now));
				}
				if(time.equals("day"))
				{
					agoDate.add(Calendar.DATE, -1);
					String ago_time = format.format( agoDate.getTime());
					String ago_time1 = format1.format( agoDate.getTime());	
					constructor.should(new ESQueryBuilders().range("pubtime", ago_time1, now1));
					constructor.should(new ESQueryBuilders().range("pubtime", ago_time, now));
				}
			 }
			 //不包含
			 if(!not.equals(""))
			 {
				 System.out.println("不包含："+not);
				 if(field!=null)
				 {
					 for(String f:field)
					 {
						 constructor.mustNot(new ESQueryBuilders().match(f,not));
					 }
				 }
			 }
			 //包含任意关键词
			 if(!any.equals(""))
			 {
				 System.out.println(any);
				 if(field!=null)
				 {
					 for(String f:field)
					 {
						 constructor.should(new ESQueryBuilders().match(f,any));
					 }
				 }
			 }
			 //包含完整关键词
			 if(!integer.equals(""))
			 {
				 System.err.println(integer);
				 if(field!=null)
				 {
					 for(String f:field)
					 {
						 constructor.must(new ESQueryBuilders().term(f,integer));
					 }
				 }
			 }
			 //包含全部关键词
			 if(!all.equals(""))
			 {
				 System.out.println(all);
				 if(field!=null)
				 {
					 for(String f:field)
					 {
						 constructor.must(new ESQueryBuilders().match(f,all));
					 }
				 }
			 }
		 }
		
        // constructor.setSize(300);  //查询返回条数，最大 10000  
         //constructor.setFrom();  //分页查询条目起始位置， 默认0  
         //constructor.setAsc(""); //排序  
         
         List<WebPage> web = service.searchWeb("myindex", "mytype", constructor); 
        // List<Map<String, Object>> list = service.search("myindex", "mytype", constructor);  
         //Map<Object, Object> map = service.statSearch("myindex", "mytype", constructor, "update_time");
         //Map<String, Object> score = list.get(list.size()-1);
         //System.out.println("一共检索到："+list.size()+"条数据");
         System.out.println("一共检索到："+web.size()+"条数据");
         
         
         int i = 0;
		String result ="";
		String page1="";//第一页
		String[] pattern ={
				"<div class=\\\"resultItem\\\"><div class=\\\"itemHead\\\"><a href=\\\"", 
				"\\\"  target=\\\"_blank\\\" class=\\\"title\\\">",
				"</a><span class=\\\"divsion\\\">-</span><span class=\\\"fileType\\\"><span class=\\\"label\\\">分类：</span><span class=\\\"value\\\">",
				"</span></span><span class=\\\"dependValue\\\"><span class=\\\"label\\\">相关度</span><span class=\\\"value\\\">",
				"</span></span></div><div class=\\\"itemBody\\\">"	,	
				"</div><div class=\\\"itemFoot\\\"><span class=\\\"info\\\"><label>搜索引擎；</label><span class=\\\"value\\\">Searcher搜索</span></span><span class=\\\"info\\\"><label>时间：</label><span class=\\\"value\\\">",
				"</span></span></div></div>",
				"<span class=\\\"keyWord\\\">",
				"</span>"
		};
		String[] pattern1 ={
				"<div class=\"resultItem\"><div class=\"itemHead\"><a href=\"", 
				"\"  target=\"_blank\" class=\"title\">",
				"</a><span class=\"divsion\">-</span><span class=\"fileType\"><span class=\"label\">分类：</span><span class=\"value\">",
				"</span></span><span class=\"dependValue\"><span class=\"label\">相关度</span><span class=\"value\">",
				"</span></span></div><div class=\"itemBody\">"	,	
				"</div><div class=\"itemFoot\"><span class=\"info\"><label>搜索引擎：</label><span class=\"value\">Searcher搜索</span></span><span class=\"info\"><label>时间：</label><span class=\"value\">",
				"</span></span></div></div>",
				"<span class=\"keyWord\">",
				"</span>"
		};
		while(i<web.size()-1)
		{
			WebPage page = web.get(i);
			String profile = "";
			String title=request.getParameter("content");
			String web_name ="-"+page.getWebsite_name(); 
			if(page.getContent().length()>200)
			{
				profile = page.getContent().substring(0,200)+"...";
//				Pattern p=Pattern.compile("<s.*....");
//				String check = profile.substring(200-34, 203);
//				Matcher m=p.matcher(check);
//				int index = 34;
//				if(m.find())
//				{
//					index = m.start(0);
//				}
				//profile = page.getContent().substring(0,200-34+index)+"...";
			}
			else
			{
				profile = page.getContent()+"...";
			}
			if(!page.getTitle().equals(""))
				title = page.getTitle();
			if(i<9)
			{			
				page1+=pattern1[0]+page.getUrl()+pattern1[1]+title+web_name+pattern1[2]+page.getChannel_name()
				+pattern1[3]+Double.parseDouble(page.getScore())*100+"%"+pattern1[4]+profile+pattern1[5]
				+page.getPubtime()+pattern1[6];
			}

			//处理字符串
			profile = processString(profile);
			title = processString(title);
			//System.out.println(profile);
			
			
			result+=pattern[0]+page.getUrl()+pattern[1]+title+web_name+pattern[2]+page.getChannel_name()
			+pattern[3]+Double.parseDouble(page.getScore())*100+"%"+pattern[4]+profile+pattern[5]
			+page.getPubtime()+pattern[6];
			if((i+1)%9==0)
				result+="```";
			i++;
		}
		
//		while(i<list.size()-1)
//		{
//			Map<String, Object> m = list.get(i);
//			String profile = "";
//			String title=request.getParameter("content");
//			String web_name ="-"+(String)m.get("website_name"); 
//			if(m.get("content").toString().length()>100)
//			{
//				profile = m.get("content").toString().substring(0,100)+"...";
//			}
//			else
//			{
//				profile = m.get("content").toString()+"...";
//			}
//			if(!m.get("title").equals(""))
//				title = m.get("title").toString();
//			if(i<9)
//			{			
//				page1+=pattern1[0]+m.get("url")+pattern1[1]+title+web_name+pattern1[2]+m.get("channel_name")
//				+pattern1[3]+Double.parseDouble(score.get(i+"").toString())*100+"%"+pattern1[4]+profile+pattern1[5]
//				+m.get("update_time")+pattern1[6];
//			}
//
//			//处理字符串
//			profile = processString(profile);
//			title = processString(title);
//
//			result+=pattern[0]+m.get("url")+pattern[1]+title+web_name+pattern[2]+m.get("channel_name")
//			+pattern[3]+Double.parseDouble(score.get(i+"").toString())*100+"%"+pattern[4]+profile+pattern[5]
//		    +m.get("update_time")+pattern[6];
//			if((i+1)%9==0)
//				result+="```";
//			i++;
//		}
	
		request.setAttribute("rowCount", i);
		request.setAttribute("result",result);
		request.setAttribute("page1",page1);
        request.getRequestDispatcher("./result.jsp").forward(request, response);
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException
	{
		doGet(request,response);
	}
	
//高亮
//public void
	
	
	
	//处理字符串中的引号
	public String processString(String content)
	{
		String result = "";
		
		String temp[] = content.split("\"");

			for(int i=0;i<temp.length;i++)
			{
				if(i==temp.length-1)
					result += temp[i];
				else
					result += temp[i]+"\\\"";
			}
		
		//判断第一个字符是否为引号
		if(content.charAt(0)=='"')
		{
			result ="\\\""+result;	
		}
		//判断最后一个字符是否为引号
		if(content.charAt(content.length()-1)=='"')
		{
			result+="\\\"";	
		}	
		
		return result;
	}
}
