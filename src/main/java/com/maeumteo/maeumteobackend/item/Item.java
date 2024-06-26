package com.maeumteo.maeumteobackend.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Item {
    @Id
    private Long id;
    @Column(nullable = false)
    private String itemname;
    @Column(nullable = false)
    private String imgurl;
    @Column(nullable = false)
    private Long price;
    @Column(nullable = false)
    private String createBy;
    @Column(nullable = false)
    private LocalDateTime createAt;
}
