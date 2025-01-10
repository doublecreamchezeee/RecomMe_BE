package com.example.recomme_be.service;

import com.example.recomme_be.model.Genre;
import com.example.recomme_be.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private  final GenreRepository genreRepository;
    public  List<Genre> getManyByObjectsId(Collection<String> objectIds) {
        List<ObjectId> objectIdList = objectIds.stream()
                .map(ObjectId::new)
                .toList();
        return genreRepository.findAllBy_idIn(objectIdList);
    }
}
