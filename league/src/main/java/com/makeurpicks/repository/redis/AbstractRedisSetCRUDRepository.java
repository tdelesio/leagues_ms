package com.makeurpicks.repository.redis;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import com.makeurpicks.domain.AbstractModel;

public abstract class AbstractRedisSetCRUDRepository <T extends AbstractModel> {

	protected final SetOperations<String, T> setOps;
	
	public AbstractRedisSetCRUDRepository(RedisTemplate<String, T> redisTemplate)
	{
		this.setOps = redisTemplate.opsForSet();
	}

	 
	public long count(String key) {
		return setOps.size(key);
	}

	public void delete(String key, T value) {
		setOps.remove(key, value);
	}

//	public void delete(T league) {
//		setOps.delete(getKey(), league.getId());
//	}

	public void delete(String key, T... values) {
		for (T value : values) {
			delete(key, value);
		}
	}

	public void deleteAll(String key) {
		Set<T> games = setOps.members(key);
		for (T game : games) {
			delete(key, game);
		}
	}

	public boolean exists(String key, String value) {
		return setOps.isMember(key, value);
	}

	public Iterable<T> findAll(String key) {
		return setOps.members(key);
	}

//	public Iterable<T> findAll(Iterable<String> ids) {
//		return setOps.multiGet(getKey(), convertIterableToList(ids));
//	}

//	public String findOne(String id) {
//		return setOps.pop(getKey(), id);
//	}

	public void add(String key, T... values) {
		setOps.add(key, values);
		return;
	}

//	public <S extends T> Iterable<S> save(Iterable<S> leagues) {
//		List<S> result = new ArrayList<S>();
//		for (S entity : leagues) {
//			save(entity);
//			result.add(entity);
//		}
//		return result;
//	}

}

