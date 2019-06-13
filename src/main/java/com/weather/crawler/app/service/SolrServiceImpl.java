package com.weather.crawler.app.service;

import com.google.common.collect.Lists;
import com.weather.crawler.app.domain.Country;
import com.weather.crawler.app.domain.Location;
import com.weather.crawler.app.domain.LocationResultDto;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class SolrServiceImpl implements SolrService {
    private static final Logger log = LoggerFactory.getLogger(SolrServiceImpl.class);

    private final SolrClient solr;

    public SolrServiceImpl( @Value("${base.solr.url}") String baseSolrUrl) {
        this.solr = new HttpSolrClient.Builder(baseSolrUrl).build();
    }

    @Override
    public Collection<LocationResultDto> getLocations(String location) {
        try {
            SolrQuery query = new SolrQuery();
            String q = String.format("city:(*%s*) OR country:(*%s*)", location, location);
            query.setQuery(q);
            query.setSort("country", SolrQuery.ORDER.asc);
            query.setSort("city", SolrQuery.ORDER.asc);
            QueryResponse queryResponse = solr.query(query);
            SolrDocumentList docs = queryResponse.getResults();
            solr.commit();
            return convertToDtos(docs);
        } catch (SolrServerException | IOException e) {
            log.error("Error while performing SOLR search", e);
            return null;
        }
    }

    @Override
    public synchronized void addLocations(Collection<Country> countries) {
        List<SolrInputDocument> docs = Lists.newArrayList();
        try {
            solr.deleteByQuery("*:*");
            solr.commit();
            log.info("Deleted all documents in Solr_sample");
            for (Country country : countries) {
                for (Location city : country.getCities()) {
                    SolrInputDocument doc = new SolrInputDocument();
                    doc.addField("country", country.getLocation().getDisplayName());
                    doc.addField("countryUrl",country.getLocation().getUrlName());
                    doc.addField("city", city.getDisplayName());
                    doc.addField("cityUrl", city.getUrlName());
                    docs.add(doc);
                }
            }
            solr.add(docs);
            solr.commit();
            log.info("Finished indexing all countries");
        } catch (SolrServerException | IOException e) {
            log.error("Error while adding SOLR document", e);
        }
    }

    private Collection<LocationResultDto> convertToDtos(SolrDocumentList results) {
        List<LocationResultDto> dtos = Lists.newArrayList();
        for (SolrDocument solrDocument : results) {
            String country = (String) solrDocument.get("country");
            String countryUrl = (String) solrDocument.get("countryUrl");
            String city = (String) solrDocument.get("city");
            String cityUrl = (String) solrDocument.get("cityUrl");
            LocationResultDto resultDto = LocationResultDto.create(country, countryUrl, city, cityUrl);
            dtos.add(resultDto);
        }
        return dtos;
    }
}