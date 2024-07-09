package com.maeumteo.maeumteobackend.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(nullable = false)
    private String userid;
    @Column(nullable = false)
    private Long price;
    @Column(nullable = false)
    private LocalDateTime createAt;
}
