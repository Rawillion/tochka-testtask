package com.tochka.testtask.repositories;

import com.tochka.testtask.model.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long>
{
	List<Subscribe> getAllBySourceURL(String sourceURL);
}
