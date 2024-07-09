package com.maeumteo.maeumteobackend.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String itemname;
    @Column
    private String imgurl;
    @Column(nullable = false)
    private Long price;
    @Column(nullable = false)
    private String createBy;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @Formula("(SELECT COUNT(A.*) FROM BID A WHERE A.ITEM_ID = id)")
    private int bidCount;
    @Formula("(SELECT CASE WHEN MAX(B.PRICE) IS NULL THEN MAX(A.PRICE) ELSE MAX(B.PRICE) END FROM ITEM A LEFT JOIN BID B on A.ID = B.ITEM_ID WHERE A.ID = id)")
    private Long bidPrice;
}
