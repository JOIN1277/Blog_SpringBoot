package com.klx.dao;
import com.klx.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface TypeRepository extends JpaRepository<Type,Long> {   //通过JPA操作数据库
    Type findByName(String name);   //通过名字查询
    @Query("select t from Type t") //自定义数据库语句
    List<Type> findTop(Pageable pageable);
}
