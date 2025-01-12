package com.example.recomme_be.repository.impl;

import com.example.recomme_be.dto.request.movie.MoviePopularRequest;
import com.example.recomme_be.dto.request.movie.MovieSearchRequest;
import com.example.recomme_be.model.Movie;
import com.example.recomme_be.repository.MovieRepository;
import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<BasicDBObject> getPopular(MoviePopularRequest request) {
        Query query = new Query();
        if (request.getRegion() != null && !request.getRegion().isBlank()) {
            query.addCriteria(Criteria.where("origin_country").elemMatch(
                    Criteria.where("$eq").is(request.getRegion())
            ));
        }
        query.skip((request.getPage() - 1L) * 20L).limit(20);
        return mongoTemplate.find(query, BasicDBObject.class, Movie.POPULAR_COLLECTION);
    }

    @Override
    public List<BasicDBObject> getTrending(String timeWindow) {
        String collectionName = timeWindow.equalsIgnoreCase("day") ?
                Movie.TRENDING_DAY_COLLECTION : Movie.TRENDING_WEEK_COLLECTION;

        Query query = new Query()
                // Sắp xếp theo popularity giảm dần
                .with(Sort.by(Sort.Direction.DESC, "popularity"))
                // Giới hạn số lượng kết quả trả về (ví dụ, 10 phim)
                .limit(10);

        return mongoTemplate.find(query, BasicDBObject.class, collectionName);
    }

    @Override
    public BasicDBObject getDetail(Integer movieId) {
        Query query = new Query(Criteria.where("id").is(movieId));
//        Query query = new Query(Criteria.where("id").is(movieId));
        return mongoTemplate.findOne(query, BasicDBObject.class, Movie.COLLECTION);
    }

    @Override
    public BasicDBObject getDetailWithObjectId(String objectId) {
        Query query = new Query(Criteria.where("_id").is(objectId));
        return mongoTemplate.findOne(query, BasicDBObject.class, Movie.COLLECTION);
    }

    @Override
    public List<BasicDBObject> search(MovieSearchRequest request) {
        Query query = new Query();
        if (request.getQuery() != null && !request.getQuery().isBlank()) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i"),
                    Criteria.where("overview").regex(request.getQuery(), "i")
            ));
        }
        if (request.getRegion() != null && !request.getRegion().isBlank()) {
            query.addCriteria(Criteria.where("origin_country").elemMatch(
                    Criteria.where("$eq").is(request.getRegion())
            ));
        }
        query.addCriteria(Criteria.where("adult").is(request.getIncludeAdult()));
        if (request.getYear() != null && !request.getYear().isBlank()) {
            query.addCriteria(Criteria.where("release_date").regex("^" + request.getYear()));
        }
        if (request.getPrimaryReleaseYear() != null && !request.getPrimaryReleaseYear().isBlank()) {
            query.addCriteria(Criteria.where("release_date").regex("^" + request.getPrimaryReleaseYear()));
        }
        query.skip((request.getPage() - 1L) * 20L).limit(20);
        return mongoTemplate.find(query, BasicDBObject.class, Movie.COLLECTION);
    }

    @Override
    public void updateMovieRating(String movieId, double newRating) {
        // Query to find the movie by ID
        Query query = new Query(Criteria.where("id").is(Integer.parseInt(movieId)));

        // Retrieve the current movie details
        BasicDBObject movie = mongoTemplate.findOne(query, BasicDBObject.class, Movie.COLLECTION);
        if (movie == null) {
            throw new IllegalArgumentException("Movie not found with ID: " + movieId);
        }

        // Get current vote_average and vote_count
        double currentVoteAverage = Double.parseDouble(movie.get("vote_average").toString());
        int currentVoteCount = Integer.parseInt(movie.get("vote_count").toString());

        // Calculate new vote_average and increment vote_count
        double updatedVoteAverage =
                (currentVoteAverage * currentVoteCount + newRating) / (currentVoteCount + 1);
        int updatedVoteCount = currentVoteCount + 1;
        DecimalFormat df = new DecimalFormat("#.00");

        // Update operation
        Update update = new Update()
                .set("vote_average", df.format(updatedVoteAverage))
                .set("vote_count", updatedVoteCount);

        // Perform the update
        mongoTemplate.updateFirst(query, update, Movie.COLLECTION);
    }

    @Override
    public List<BasicDBObject> getByIds(Collection<Integer> ids) {
        Query query = new Query(Criteria.where("id").in(ids));
        return mongoTemplate.find(query, BasicDBObject.class, Movie.COLLECTION);
    }

    @Override
    public List<BasicDBObject> getByObjectIds(Collection<String> objectIds) {
        List<ObjectId> objectIdList = objectIds.stream()
                .map(ObjectId::new)
                .toList();

        Query query = new Query(Criteria.where("_id").in(objectIdList));
        return mongoTemplate.find(query, BasicDBObject.class, Movie.COLLECTION);
    }

}
