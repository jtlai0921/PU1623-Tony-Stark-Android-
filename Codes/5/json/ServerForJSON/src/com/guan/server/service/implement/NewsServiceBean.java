package com.guan.server.service.implement;

import java.util.ArrayList;
import java.util.List;

import com.guan.server.domain.News;
import com.guan.server.service.NewsService;


public class NewsServiceBean implements NewsService {
	/**
	 * 取得最新的視訊資訊
	 * @return
	 */
	public List<News> getLastNews(){
		List<News> newes = new ArrayList<News>();
		newes.add(new News(10, "aaa", 20));
		newes.add(new News(45, "bbb", 10));
		newes.add(new News(89, "Android is good", 50));
		return newes;
	}
}
