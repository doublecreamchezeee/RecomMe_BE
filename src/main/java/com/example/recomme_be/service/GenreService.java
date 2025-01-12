package com.example.recomme_be.service;

import com.example.recomme_be.dto.request.movie.GenresFilterRequest;
import com.example.recomme_be.model.Genre;
import com.example.recomme_be.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private  final GenreRepository genreRepository;
    public  List<Genre> getMany(GenresFilterRequest request) {
        if (StringUtils.hasText(request.getObjectIds())) {
            List<String> objectIdsString = Arrays.asList(request.getObjectIds().split(","));

            List<ObjectId> objectIds = objectIdsString.stream()
                    .map(ObjectId::new)  // or Long::valueOf if Mongo stores IDs as long
                    .toList();

            return genreRepository.findAllBy_idIn(objectIds);
        }
        return genreRepository.findAll();
    }
}
