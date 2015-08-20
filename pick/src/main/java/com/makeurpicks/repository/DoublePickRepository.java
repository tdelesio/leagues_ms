package com.makeurpicks.repository;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.DoublePick;

public interface DoublePickRepository extends CrudRepository<DoublePick, String> {

}
