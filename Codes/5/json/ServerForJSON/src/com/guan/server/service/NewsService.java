package com.guan.server.service;

import java.util.List;

import com.guan.server.domain.News;


public interface NewsService {

	/**
	 * ���o�̷s�����T��T
	 * @return
	 */
	public List<News> getLastNews();

}