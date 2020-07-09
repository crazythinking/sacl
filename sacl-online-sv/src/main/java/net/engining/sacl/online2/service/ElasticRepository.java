package net.engining.sacl.online2.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Eric Lu
 * @date 2020-07-03 17:00
 **/
public interface ElasticRepository extends ElasticsearchRepository<DocBean, Long> {

    //默认的注释
    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"content\" : \"?\"}}}}")
    Page<DocBean> findByContent(String content, Pageable pageable);

    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"firstCode.keyword\" : \"?\"}}}}")
    Page<DocBean> findByFirstCode(String firstCode, Pageable pageable);

    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"secondCode.keyword\" : \"?\"}}}}")
    Page<DocBean> findBySecondCode(String secordCode, Pageable pageable);


}

