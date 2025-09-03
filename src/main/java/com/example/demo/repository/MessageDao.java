package com.example.demo.repository;


//import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;

import com.example.demo.domain.message.model.Message;
import com.example.demo.domain.message.model.MessageKey;

//public interface MessageDao extends JpaRepository<Message, Integer> {
//for DB Join
public interface MessageDao extends JpaRepository<Message, MessageKey> {	
	  /* JPQL定数 */

	/*  final String JPQL_DELETE_MESSAGE_BY_KINDID = " DELETE "
	    + " FROM Message m"
	    + " WHERE m.kindId = :kindId "; */
	  
	  public Long deleteByKindId(String kindId); // JPA標準のメソッド
	    
	  /** 削除(種別ID指定) */
//	  @Modifying
//	  @Query(JPQL_DELETE_MESSAGE_BY_KINDID)
//	  public int deleteByKindId(String kindId) throws DataAccessException;

	  

}