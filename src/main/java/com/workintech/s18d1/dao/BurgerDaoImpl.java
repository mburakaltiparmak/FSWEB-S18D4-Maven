package com.workintech.s18d1.dao;

import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import com.workintech.s18d1.exceptions.BurgerException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
@Slf4j
@Repository
public class BurgerDaoImpl implements BurgerDao{
    private final EntityManager entityManager;


    public BurgerDaoImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }
    @Transactional
    @Override
    public Burger save(Burger burger){
        log.info("save started");
        entityManager.persist(burger);
        log.info("save ended");
        return burger;
    }

    @Override
    public Burger findById(Long id) {
        Burger burger = entityManager.find(Burger.class,id);
        if(burger == null){
            throw new BurgerException("Burger not found!", HttpStatus.NOT_FOUND);
        }
        return burger;
    }

    @Override
    public List<Burger> findAll() {
        TypedQuery<Burger> foundAll = entityManager.createQuery("SELECT b FROM Burger b", Burger.class);
        return foundAll.getResultList();
    }

    @Override
    public List<Burger> findByPrice(Double price) {
        TypedQuery<Burger> foundByPrice = entityManager.createQuery("select b from Burger as b where b.price > :price order by b.price desc",Burger.class);
        foundByPrice.setParameter("price", price);
        return foundByPrice.getResultList();

    }

    @Override
    public List<Burger> findByBreadType(BreadType breadType) {
        TypedQuery<Burger> foundByBreadType = entityManager.createQuery("select b from Burger as b where b.breadType = :breadType order by b.breadType desc",Burger.class);
        foundByBreadType.setParameter("breadType", breadType);
        return foundByBreadType.getResultList();
    }

    @Override
    public List<Burger> findByContent(String content) {
        TypedQuery<Burger> foundByContent = entityManager.createQuery("select b from Burger as b where b.contents like concat('%', :content, '%') order by b.name",Burger.class);
        foundByContent.setParameter("content", content);
        return foundByContent.getResultList();
    }
    @Transactional
    @Override
    public Burger update(Burger burger) {
        return entityManager.merge(burger);
    }
    @Transactional
    @Override
    public Burger remove(Long id) {
        Burger foundBurger = findById(id);
        entityManager.remove(foundBurger);
        return foundBurger;
    }
}
