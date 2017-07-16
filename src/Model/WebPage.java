package Model;

import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;

public class WebPage 
{
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getWebsite_name() {
		return website_name;
	}
	public void setWebsite_name(String website_name) {
		this.website_name = website_name;
	}
	private Integer id;
    private String title;
    private String content;
    private String url;//
    private String language;
    private String pubtime;//
    private String author;
    private String country;
    private String source;
    private String update_time;//
    private String website_name;
    private String channel_name;
	private String score;
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

    public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}

    
    public WebPage( Integer id,String title,String content,String url,
    		String language,String pubtime,String author,String country,
    		String source,String update_time,String website_name,String score)
    {

    }
    public WebPage( Integer id,String title,String content,String url,String author,String score)
    {
    	this.id = id;
    	this.title = title;
    	this.content = content;
    	this.url = url;
    	this.author = author;
    	this.language = " ";
    	this.pubtime =  " ";
    	this.country =  " ";
    	this.source =  " ";
    	this.update_time =  " ";
    	this.website_name =  " ";
    }
    public WebPage(Map<String, Object> json,float score)
    {
    	this.id = (Integer)json.get("id");
    	this.title =(String)json.get("title");
    	this.content = (String)json.get("content");
    	this.url =(String)json.get("url");
    	this.language = (String)json.get("language");
    	this.pubtime = (String)json.get("pubtime");
    	this.author =(String)json.get("author");
    	this.country = (String)json.get("country");
    	this.source =(String)json.get("source");
    	this.update_time = (String)json.get("update_time");
    	this.website_name = (String)json.get("website_name");
    	this.score = new Float(score).toString();
    	this.channel_name = (String)json.get("channel_name");
    }
}
