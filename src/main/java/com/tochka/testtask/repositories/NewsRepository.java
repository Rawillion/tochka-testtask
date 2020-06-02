package com.tochka.testtask.repositories;

import com.tochka.testtask.model.News;
import com.tochka.testtask.model.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>
{
	List<News> getAllBySourceSubscribe(Subscribe subscribe);

	@Query("select n from News n where lower(n.title) like lower(concat('%',?1,'%'))")
	List<News> getAllByTitleLike(String title);
}
