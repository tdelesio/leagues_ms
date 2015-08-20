package com.makeurpicks.repository.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.AbstractModel;

public abstract class AbstractRedisHashCRUDRepository<T extends AbstractModel>
		{

	protected final HashOperations<String, String, T> hashOps;

	public abstract String getKey();

	public AbstractRedisHashCRUDRepository(RedisTemplate<String, T> redisTemplate) {
		this.hashOps = redisTemplate.opsForHash();
	}

	public long count() {
		return hashOps.keys(getKey()).size();
	}

	public void delete(String id) {
		hashOps.delete(getKey(), id);
	}

	public void delete(T league) {
		hashOps.delete(getKey(), league.getId());
	}

	public void delete(Iterable<? extends T> leagues) {
		for (T league : leagues) {
			delete(league);
		}
	}

	public void deleteAll() {
		Set<String> ids = hashOps.keys(getKey());
		for (String id : ids) {
			delete(id);
		}
	}

	public boolean exists(String id) {
		return hashOps.hasKey(getKey(), id);
	}

	public Iterable<T> findAll() {
		return hashOps.values(getKey());
	}

	public Iterable<T> findAll(Iterable<String> ids) {
		return hashOps.multiGet(getKey(), convertIterableToList(ids));
	}

	public T findOne(String id) {
		return hashOps.get(getKey(), id);
	}

	public <S extends T> S save(S league) {
		hashOps.put(getKey(), league.getId(), league);
		return league;
	}

	public <S extends T> Iterable<S> save(Iterable<S> leagues) {
		List<S> result = new ArrayList<S>();
		for (S entity : leagues) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	private <T> List<T> convertIterableToList(Iterable<T> iterable) {
		List<T> list = new ArrayList<T>();
		for (T object : iterable) {
			list.add(object);
		}
		return list;
	}
}
