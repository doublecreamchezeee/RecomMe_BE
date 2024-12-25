package com.example.recomme_be.repository.impl;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.model.Movie;
import com.example.recomme_be.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    public List<Movie> getPopular(MoviePopularRequest request) {
        Query query = new Query();
        if (request.getRegion() != null && !request.getRegion().isEmpty()) {
            query.addCriteria(Criteria.where("origin_country").in(request.getRegion()));
        }
        query.skip((request.getPage() - 1L) * 20L).limit(20);
        return mongoTemplate.find(query, Movie.class, Movie.POPULAR_COLLECTION);
    }

    @Override
    public List<Movie> getTrending(String timeWindow) {
        String collectionName = timeWindow.equalsIgnoreCase("day") ?
                Movie.TRENDING_DAY_COLLECTION : Movie.TRENDING_WEEK_COLLECTION;

        Query query = new Query()
                // Sắp xếp theo popularity giảm dần
                .with(Sort.by(Sort.Direction.DESC, "popularity"))
                // Giới hạn số lượng kết quả trả về (ví dụ, 10 phim)
                .limit(10);

        return mongoTemplate.find(query, Movie.class, collectionName);
    }

    @Override
    public Movie getDetail(String movieId) {
        Query query = new Query(Criteria.where("tmdb_id").is(movieId));
        return mongoTemplate.findOne(query, Movie.class, Movie.COLLECTION);
    }

    @Override
    public List<Movie> search(MovieSearchRequest request) {
        Query query = new Query();
        if (request.getQuery() != null && !request.getQuery().isEmpty()) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i"),
                    Criteria.where("overview").regex(request.getQuery(), "i")
            ));
        }
        query.skip((request.getPage() - 1L) * 20L).limit(20);
        return mongoTemplate.find(query, Movie.class, Movie.COLLECTION);
    }
}
