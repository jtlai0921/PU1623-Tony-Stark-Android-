package com.guan.server.service;

import java.util.List;

import com.guan.server.domain.News;


public interface NewsService {

	/**
	 * 取得最新的視訊資訊
	 * @return
	 */
	public List<News> getLastNews();

}