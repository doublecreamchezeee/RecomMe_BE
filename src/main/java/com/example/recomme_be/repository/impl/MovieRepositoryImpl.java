package com.example.recomme_be.repository.impl;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.model.Movie;
import com.example.recomme_be.repository.MovieRepository;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepository {
    private final MongoTemplate mongoTemplate;
    @Override
    public List<DBObject> getPopular(MoviePopularRequest moviePopularRequest) {
        Query query = new Query();

//        if (moviePopularRequest.getLanguage() != null && !moviePopularRequest.getLanguage().isEmpty()) {
//            query.addCriteria(Criteria.where("original_language").regex(".*" + moviePopularRequest.getLanguage() + ".*", "i"));
//        }

        if (moviePopularRequest.getRegion() != null && !moviePopularRequest.getRegion().isEmpty()) {
            query.addCriteria(Criteria.where("origin_country").in(moviePopularRequest.getRegion()));  // Use .in() for array matching
        }

        query.skip((moviePopularRequest.getPage()) * 20L);  // Assume 20 movies per page
        query.limit(20);
        return  mongoTemplate.find(query, DBObject.class, Movie.POPULAR_COLLECTION);
    }

    @Override
    public List<DBObject> getTrending(String timeWindow) {
        if (timeWindow.equals("day")) {
            return mongoTemplate.find(new Query(), DBObject.class, Movie.TRENDING_DAY_COLLECTION);
        }
        if (timeWindow.equals("week")) {
            return mongoTemplate.find(new Query(), DBObject.class, Movie.TRENDING_WEEK_COLLECTION);
        }
        return List.of();
    }

    @Override
    public DBObject getDetail(String movieId) {
        Query query = new Query(Criteria.where("tmdb_id").is(movieId));
        return mongoTemplate.findOne(query, DBObject.class, Movie.COLLECTION);
    }

    public List<DBObject> search(MovieSearchRequest movieSearchRequest) {
        Query query = new Query();

        if (movieSearchRequest.getQuery() != null && !movieSearchRequest.getQuery().isEmpty()) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("original_language").regex(".*" + movieSearchRequest.getQuery() + ".*", "i"),
                    Criteria.where("overview").regex(".*" + movieSearchRequest.getQuery() + ".*", "i"),
                    Criteria.where("original_title").regex(".*" + movieSearchRequest.getQuery() + ".*", "i"),
                    Criteria.where("title").regex(".*" + movieSearchRequest.getQuery() + ".*", "i"),
                    Criteria.where("tagline").regex(".*" + movieSearchRequest.getQuery() + ".*", "i"),
                    Criteria.where("origin_country").regex(".*" + movieSearchRequest.getQuery() + ".*", "i")
            ));
        }

        if (movieSearchRequest.getIncludeAdult() != null) {
            query.addCriteria(Criteria.where("adult").is(movieSearchRequest.getIncludeAdult()));
        }

        if (movieSearchRequest.getRegion() != null && !movieSearchRequest.getRegion().isEmpty()) {
            query.addCriteria(Criteria.where("origin_country").is(movieSearchRequest.getRegion()));
        }

        if (movieSearchRequest.getYear() != null && !movieSearchRequest.getYear().isEmpty()) {
            query.addCriteria(Criteria.where("release_date").regex("^" + movieSearchRequest.getYear(), "i"));
        }

        if (movieSearchRequest.getPrimaryReleaseYear() != null && !movieSearchRequest.getPrimaryReleaseYear().isEmpty()) {
            query.addCriteria(Criteria.where("release_date").regex("^" + movieSearchRequest.getPrimaryReleaseYear(), "i"));
        }

        query.skip((movieSearchRequest.getPage() - 1L) * 20L);  // Assume 20 movies per page
        query.limit(20);

        return mongoTemplate.find(query, DBObject.class,Movie.COLLECTION);
    }
}
