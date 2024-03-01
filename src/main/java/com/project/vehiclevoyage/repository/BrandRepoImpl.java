package com.project.vehiclevoyage.repository;

import com.project.vehiclevoyage.entities.Brands;
import com.project.vehiclevoyage.entities.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class BrandRepoImpl implements BrandRepository{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Brands findByBrandName(String brandName) {
        return null;
    }

    @Override
    public List<Brands> getAllBrandsByVehicleType(String vehicleType) {
        return null;
    }

    @Override
    public List<Model> getModelsByBrandName(String brandName) {
        return null;
    }

    @Override
    public <S extends Brands> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Brands> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Brands> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Brands> findAll() {
        return null;
    }

    @Override
    public List<Brands> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Brands entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Brands> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Model> getModelsByBrandNameAndVehicleType(String brandName, String vehicleType) {
        System.out.println("Inside brand repo: "+"Brand Name: " + brandName + " Vehicle Type: " + vehicleType);
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("brandName").is(brandName),
                Criteria.where("vehicleType").is(vehicleType)
        );

        query.addCriteria(criteria);
        return mongoTemplate.find(query, Model.class, "brand_details");
    }

    @Override
    public Brands findByBrandNameAndVehicleType(String brandName, String vehicleType) {
        return null;
    }

    @Override
    public <S extends Brands> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Brands> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Brands> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Brands> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Brands> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Brands> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Brands> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Brands> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Brands, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }


    @Override
    public List<Brands> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Brands> findAll(Pageable pageable) {
        return null;
    }
}
