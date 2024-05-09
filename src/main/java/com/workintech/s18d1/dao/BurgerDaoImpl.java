package com.workintech.s18d1.dao;

import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import com.workintech.s18d1.exceptions.BurgerException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
@Slf4j
@Repository
/* Veritabanına hizmet eden bir component olduğu için @Repository annotation'ı kullanılır. SpringBoot buranın bir veritabanı iletişim classı olduğunu tanımış oldu. */
public class BurgerDaoImpl implements BurgerDao{
    private final EntityManager entityManager;
    /* */
    @Autowired
    public BurgerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Transactional /*Data Manipulation'da kullanılır. Metod tamamen çalışmayıncaya kadar SQL'de commitlenmez ve db'ye geçmez. */
    @Override
    public Burger save(Burger burger) {
        log.info("save started");
        entityManager.persist(burger);
        log.info("save ended");
        return burger;
    }

    @Override
    public Burger findById(long id) {
       Burger burger = entityManager.find(Burger.class,id);
       if(burger == null){
throw new BurgerException("Burger not found" + id, HttpStatus.NOT_FOUND);
       }
      return burger;
    }

    @Override
    public List<Burger> findAll() {
        TypedQuery<Burger> foundAll = entityManager.createQuery("SELECT b from Burger b",Burger.class);
        return foundAll.getResultList();
    }

    @Override
    public List<Burger> findByPrice(double price) {
        TypedQuery<Burger> foundList = entityManager.createQuery("SELECT b FROM Burger b WHERE b.price > :price ORDER BY b.price DESC",Burger.class); //Burdaki iki noktanın amacı parametre geçmektir.
        foundList.setParameter("price",price);
        return foundList.getResultList();
    }

    @Override
    public List<Burger> findByBreadType(BreadType breadType) {


        TypedQuery<Burger> foundList = entityManager.createQuery("SELECT b FROM Burger b WHERE b.burgerType = :burgerType ORDER BY b.burgerType DESC",Burger.class); //Burdaki iki noktanın amacı parametre geçmektir.
        //Sorguda breadType geçiyor ancak entity'e bread_type olarak geçti. Burdaki işlem entity ile yapıldığı için böyle yapılması gerekiyor.
        foundList.setParameter("breadType",breadType);
        return foundList.getResultList();
    }

    @Override
    public List<Burger> findByContent(String content) {

        TypedQuery<Burger> foundList = entityManager.createQuery("SELECT b FROM Burger b WHERE b.contents LIKE ('%', :content,%) price ORDER BY b.name",Burger.class); //Burdaki iki noktanın amacı parametre geçmektir.
        foundList.setParameter("content",content);
        return foundList.getResultList();
    }

    @Transactional
    @Override
    public Burger update(Burger burger) {

        return entityManager.merge(burger);
    }

    @Override
    public Burger remove(long id) {
        Burger foundBurger = findById(id);
        entityManager.remove(foundBurger);
        return foundBurger;
    }
}
