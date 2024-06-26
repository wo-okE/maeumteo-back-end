package com.maeumteo.maeumteobackend.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findById(Long id){
        return itemRepository.findById(id).get();
    }
}
