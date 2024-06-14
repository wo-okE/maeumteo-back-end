package com.maeumteo.maeumteobackend.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final MainRepository mainRepository;

    public List<Menu> menu(){
        return mainRepository.findAll();
    }
}
