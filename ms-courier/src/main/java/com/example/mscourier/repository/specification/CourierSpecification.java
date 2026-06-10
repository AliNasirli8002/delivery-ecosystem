package com.example.mscourier.repository.specification;

import com.example.mscourier.entity.Courier;
import com.example.mscourier.dto.CourierCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class CourierSpecification implements Specification<Courier> {

    private final CourierCriteria courierCriteria;

    public CourierSpecification(CourierCriteria courierCriteria) {
        this.courierCriteria = courierCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (courierCriteria != null && StringUtils.hasText(courierCriteria.getName())) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + courierCriteria.getName().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}