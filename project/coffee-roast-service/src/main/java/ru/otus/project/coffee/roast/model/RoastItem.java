package ru.otus.project.coffee.roast.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "roast_item")
public class RoastItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String coffee;

    private String roastDegree;

    private int weight;

    public RoastItem(String coffee, String roastDegree, int weight) {
        this.coffee = coffee;
        this.roastDegree = roastDegree;
        this.weight = weight;
    }
}
