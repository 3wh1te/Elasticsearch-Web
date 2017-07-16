package ESOperation;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.text.Text;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import Model.WebPage;

/**
 * Created by admin on 17/6/2.
 */
public class ElasticSearchService {
	private final static int MAX = 10000;

	static TransportClient client;

	/**
	 * 功能描述：服务初始化
	 *
	 * @param clusterName
	 *            集群名称
	 * @param ip
	 *            地址
	 * @param port
	 *            端口
	 */
	public ElasticSearchService(String clusterName, String ip, int port) {
		Settings setting = Settings.builder()
				// 集群名称
				.put("cluster.name", clusterName)//.put("client.transport.sniff", true)
				//.put("client.transport.ignore_cluster_name", false)
				//.put("client.transport.ping_timeout", "5s")
				//.put("client.transport.nodes_sampler_interval", "5s")
				.build();

		if (client == null) {
			try {
				client = TransportClient.builder().settings(setting).build()
		                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(ip, 9300)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** * 功能描述：新建索引 * * @param indexName 索引名 */
	public void createIndex(String indexName) {
		client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();
	}

	/** * 功能描述：新建索引 * * @param index 索引名 * @param type 类型 */
	public void createIndex(String index, String type) {
		client.prepareIndex(index, type).setSource().get();
	}

	/** * 功能描述：删除索引 * * @param index 索引名 
	 * @throws Exception */
	public void deleteIndex(String index) throws Exception {
		if (indexExist(index)) {
			DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
			if (!dResponse.isAcknowledged()) {
				throw new Exception("failed to delete index.");
			}
		} else {
			throw new Exception("index name not exists");
		}
	}

	/** * 功能描述：验证索引是否存在 * * @param index 索引名 */
	public boolean indexExist(String index) {
		IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(index);
		IndicesExistsResponse inExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
		return inExistsResponse.isExists();
	}

	/** * 功能描述：插入数据 * * @param index 索引名 * @param type 类型 * @param json 数据 */
	public void insertData(String index, String type, String json) {
		IndexResponse response = client.prepareIndex(index, type).setSource(json).get();
	}

	/**
	 * * 功能描述：插入数据 * * @param index 索引名 * @param type 类型 * @param _id 数据id
	 * * @param json 数据
	 */
	public void insertData(String index, String type, String _id, String json) {
		IndexResponse response = client.prepareIndex(index, type).setId(_id).setSource(json).get();
	}

	/**
	 * * 功能描述：更新数据 * * @param index 索引名 * @param type 类型 * @param _id 数据id
	 * * @param json 数据
	 */
	public void updateData(String index, String type, String _id, String json) throws Exception {
		try {
			UpdateRequest updateRequest = new UpdateRequest(index, type, _id).doc(json);
			client.update(updateRequest).get();
		} catch (Exception e) {
			throw new Exception("update data failed.", e);
		}
	}

	/** * 功能描述：删除数据 * * @param index 索引名 * @param type 类型 * @param _id 数据id */
	public void deleteData(String index, String type, String _id) {
		DeleteResponse response = client.prepareDelete(index, type, _id).get();
	}

	/**
	 * * 功能描述：批量插入数据 * * @param index 索引名 * @param type 类型 * @param data (_id
	 * 主键, json 数据)
	 */
	public void bulkInsertData(String index, String type, Map<String, String> data) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		data.forEach((param1, param2) -> {
			bulkRequest.add(client.prepareIndex(index, type, param1).setSource(param2));
		});
		BulkResponse bulkResponse = bulkRequest.get();
	}

	/**     * 功能描述：批量插入数据     *     * @param index    索引名     * @param type     类型     * @param jsonList 批量数据     */    
	public void bulkInsertData(String index, String type, List<String> jsonList) 
	{        
		BulkRequestBuilder bulkRequest = client.prepareBulk();        
		jsonList.forEach(item -> {bulkRequest.add(client.prepareIndex(index, type) .setSource(item));}); 
		BulkResponse bulkResponse = bulkRequest.get();   
	}
	

/**     * 功能描述：查询    
 * @param index       索引名     
 * @param type        类型     
 * @param constructor 查询构造    
 */   
	public List<Map<String, Object>> search(String index, String type, ESQueryBuilderConstructor constructor) 
	{        
		List<Map<String, Object>> result = new ArrayList<>();        
		Map<String, Object> score = new HashMap<String , Object>();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);        
		//排序        
		if (StringUtils.isNotEmpty(constructor.getAsc()))            
			searchRequestBuilder.addSort(constructor.getAsc(), SortOrder.ASC);        
		if (StringUtils.isNotEmpty(constructor.getDesc()))           
			searchRequestBuilder.addSort(constructor.getDesc(), SortOrder.DESC);        
		//设置查询体        
		searchRequestBuilder.setQuery(constructor.listBuilders());        
		//返回条目数        
		int size = constructor.getSize();        
		if (size < 0) 
		{            
			size = 0;        
			}        
		if (size > MAX) 
		{            
			size = MAX;        
			}        
		//返回条目数        
		searchRequestBuilder.setSize(size);        
		searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());
        // 设置高亮显示
        searchRequestBuilder.addHighlightedField("title");
        searchRequestBuilder.addHighlightedField("content");
        searchRequestBuilder
                .setHighlighterPreTags("<span class=\\\"keyWord\\\">");
        searchRequestBuilder.setHighlighterPostTags("</span>");
        
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();        
		SearchHits hits = searchResponse.getHits();        
		SearchHit[] searchHits = hits.getHits();
		
		int i = 0;
		for (SearchHit sh : searchHits) 
		{            
			result.add(sh.getSource()); 
			score.put(i+"", sh.getScore());
			
			//System.out.println(sh.getHighlightFields().get("content"));
			i++;
			}
		result.add(score);
		return result;    
	}   
	/**     * 功能描述：查询+高亮    
	 * @param index       索引名     
	 * @param type        类型     
	 * @param constructor 查询构造    
	 */   
	public List<WebPage> searchWeb(String index, String type, ESQueryBuilderConstructor constructor) 
	{        
		List<WebPage> web = new ArrayList<>();        
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);        
		//排序        
		if (StringUtils.isNotEmpty(constructor.getAsc()))            
			searchRequestBuilder.addSort(constructor.getAsc(), SortOrder.ASC);        
		if (StringUtils.isNotEmpty(constructor.getDesc()))           
			searchRequestBuilder.addSort(constructor.getDesc(), SortOrder.DESC);        
		//设置查询体        
		searchRequestBuilder.setQuery(constructor.listBuilders());        
		//返回条目数        
		int size = constructor.getSize();        
		if (size < 0) 
		{            
			size = 0;        
			}        
		if (size > MAX) 
		{            
			size = MAX;        
			}        
		//返回条目数        
		searchRequestBuilder.setSize(size);        
		searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());
        // 设置高亮显示
        searchRequestBuilder.addHighlightedField("title");
        searchRequestBuilder.addHighlightedField("content");
        searchRequestBuilder
                .setHighlighterPreTags("<span class=\"keyWord\">");
        searchRequestBuilder.setHighlighterPostTags("</span>");
        
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();        
		SearchHits hits = searchResponse.getHits();        
		SearchHit[] searchHits = hits.getHits();
		Map<String, Object> json;
		for (SearchHit sh : searchHits) 
		{            
			// 将文档中的每一个对象转换json串值
            json = sh.getSource();
            // 将json串值转换成对应的实体对象
            WebPage webPage = new WebPage(json,sh.getScore());
            // 获取对应的高亮域
            Map<String, HighlightField> result = sh.highlightFields();
            // 从设定的高亮域中取得指定域
            HighlightField titleField = result.get("title");
            if (titleField !=null) 
            {
                // 取得定义的高亮标签
                Text[] titleTexts =titleField.getFragments();
                // 为title串值增加自定义的高亮标签
                String title = "";
                for (Text text : titleTexts) {
                    title += text;
                }
                webPage.setTitle(title);
            }
//            // 从设定的高亮域中取得指定域
//            HighlightField contentField = result.get("content");
//            if (contentField !=null) {
//                // 取得定义的高亮标签
//                Text[] contentTexts = contentField.fragments();
//                // 为title串值增加自定义的高亮标签
//                String content = "";
//                for (Text text : contentTexts) {
//                    content += text;
//                }
//                // 将追加了高亮标签的串值重新填充到对应的对象
//                webPage.setContent(content);
//            }
            web.add(webPage);
		}

		return web;    
	}
	
	/**     * 功能描述：数据数量    
	 * @param index       索引名     
	 * @param type        类型     
	 * @param constructor 查询构造    
	 */   
	public int searchCount(String index, String type, ESQueryBuilderConstructor constructor) 
	{ 
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);          
		//设置查询体        
		searchRequestBuilder.setQuery(constructor.listBuilders());   
		searchRequestBuilder.setSize(MAX);   
		//searchRequestBuilder.setSize(100);  
		//返回条目数       
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();        
		SearchHits hits = searchResponse.getHits();        
		SearchHit[] searchHits = hits.getHits();
		System.out.println(searchHits.length);
		if(searchHits==null)
			return 0;
		
		return searchHits.length;
	}
	/*
* 功能描述：统计查询    
* *    
*  * @param index       索引名    
*   * @param type        类型     
*   * @param constructor 查询构造    
*    * @param groupBy     统计字段    
*     */    
	public Map<Object, Object> statSearch(String index, String type, ESQueryBuilderConstructor constructor,
			String groupBy) {
		Map<Object, Object> map = new HashMap();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type); // 排序
		if (StringUtils.isNotEmpty(constructor.getAsc()))
			searchRequestBuilder.addSort(constructor.getAsc(), SortOrder.ASC);
		if (StringUtils.isNotEmpty(constructor.getDesc()))
			searchRequestBuilder.addSort(constructor.getDesc(), SortOrder.DESC);
		// 设置查询体
		if (null != constructor) {
			searchRequestBuilder.setQuery(constructor.listBuilders());
		} else {
			searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
		}
		int size = constructor.getSize();
		if (size < 0) {
			size = 0;
		}
		if (size > MAX) {
			size = MAX;
		}
		// 返回条目数
		searchRequestBuilder.setSize(size);
		searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());
		SearchResponse sr = searchRequestBuilder.addAggregation(AggregationBuilders.terms("agg").field(groupBy)).get();
		Terms stateAgg = sr.getAggregations().get("agg");
		Iterator<Terms.Bucket> iter = stateAgg.getBuckets().iterator();
		while (iter.hasNext()) {
			Terms.Bucket gradeBucket = iter.next();
			map.put(gradeBucket.getKey(), gradeBucket.getDocCount());
		}
		return map;
	}

	/**
	 *
	 * 功能描述：统计查询
	 *
	 *
	 * @param index
	 *            索引名
	 *
	 * @param type
	 *            类型
	 *
	 * @param constructor
	 *            查询构造
	 *
	 * @param agg
	 *            自定义计算
	 */
	public Map<Object, Object> statSearch(String index, String type, ESQueryBuilderConstructor constructor,
			AggregationBuilder agg) {
		if (agg == null) {
			return null;
		}
		Map<Object, Object> map = new HashMap();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);
		// 排序
		if (StringUtils.isNotEmpty(constructor.getAsc()))
			searchRequestBuilder.addSort(constructor.getAsc(), SortOrder.ASC);
		if (StringUtils.isNotEmpty(constructor.getDesc()))
			searchRequestBuilder.addSort(constructor.getDesc(), SortOrder.DESC);
		// 设置查询体
		if (null != constructor) {
			searchRequestBuilder.setQuery(constructor.listBuilders());
		} else {
			searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
		}
		int size = constructor.getSize();
		if (size < 0) {
			size = 0;
		}
		if (size > MAX) {
			size = MAX;
		}
		// 返回条目数
		searchRequestBuilder.setSize(size);
		searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());
		SearchResponse sr = searchRequestBuilder.addAggregation(agg).get();
		Terms stateAgg = sr.getAggregations().get("agg");
		Iterator<Terms.Bucket> iter = stateAgg.getBuckets().iterator();
		while (iter.hasNext()) {
			Terms.Bucket gradeBucket = iter.next();
			map.put(gradeBucket.getKey(), gradeBucket.getDocCount());
		}
		return map;
	}

	/**
	 *
	 * 功能描述：关闭链接
	 */
	public void close() {
		client.close();
	}
	}
