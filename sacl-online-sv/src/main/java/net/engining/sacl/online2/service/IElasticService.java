package net.engining.sacl.online2.service;

import org.springframework.data.domain.Page;

import java.util.Iterator;
import java.util.List;

/**
 * @author Eric Lu
 * @date 2020-07-03 17:11
 **/
public interface IElasticService {

    void createIndex();

    void deleteIndex(String index);

    void save(DocBean docBean);

    void saveAll(List<DocBean> list);

    Iterator<DocBean> findAll();

    Page<DocBean> findByContent(String content);

    Page<DocBean> findByFirstCode(String firstCode);

    Page<DocBean> findBySecondCode(String secordCode);

    Page<DocBean> query(String key);
}
