package com.example.ecommerce.application.order;

import com.example.ecommerce.infrastructure.idempotency.IdempotencyKey;
import com.example.ecommerce.infrastructure.idempotency.IdempotencyRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InMemoryIdempotencyRepository implements IdempotencyRepository {

    private final Map<String, IdempotencyKey> store = new ConcurrentHashMap<>();

    @Override
    public Optional<IdempotencyKey> findById(String key) {
        return Optional.ofNullable(store.get(key));
    }

    @Override
    public <S extends IdempotencyKey> S save(S entity) {
        store.put(entity.getKey(), entity);
        return entity;
    }

    @Override
    public <S extends IdempotencyKey> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(String s) {
        return store.containsKey(s);
    }

    @Override
    public List<IdempotencyKey> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<IdempotencyKey> findAllById(Iterable<String> strings) {
        List<IdempotencyKey> result = new ArrayList<>();
        strings.forEach(id -> {
            IdempotencyKey key = store.get(id);
            if (key != null) {
                result.add(key);
            }
        });
        return result;
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public void deleteById(String s) {
        store.remove(s);
    }

    @Override
    public void delete(IdempotencyKey entity) {
        store.remove(entity.getKey());
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        strings.forEach(store::remove);
    }

    @Override
    public void deleteAll(Iterable<? extends IdempotencyKey> entities) {
        entities.forEach(entity -> store.remove(entity.getKey()));
    }

    @Override
    public void deleteAll() {
        store.clear();
    }

    @Override
    public List<IdempotencyKey> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<IdempotencyKey> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends IdempotencyKey> S insert(S entity) {
        return save(entity);
    }

    @Override
    public <S extends IdempotencyKey> List<S> insert(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends IdempotencyKey> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends IdempotencyKey> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends IdempotencyKey> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends IdempotencyKey> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends IdempotencyKey> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends IdempotencyKey> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends IdempotencyKey, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }
}