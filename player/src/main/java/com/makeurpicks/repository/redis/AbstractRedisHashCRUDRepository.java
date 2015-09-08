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

	public void delete(T t) {
		hashOps.delete(getKey(), t.getId());
	}

	public void delete(Iterable<? extends T> ts) {
		for (T t : ts) {
			delete(t);
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

	public <S extends T> S save(S t) {
		hashOps.put(getKey(), t.getId(), t);
		return t;
	}

	public <S extends T> Iterable<S> save(Iterable<S> ts) {
		List<S> result = new ArrayList<S>();
		for (S entity : ts) {
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
