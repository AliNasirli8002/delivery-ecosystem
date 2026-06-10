package com.example.msorder.specification;

import com.example.msorder.dao.Order;
import com.example.msorder.criteria.OrderCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification implements Specification<Order> {

    private final OrderCriteria criteria;

    public OrderSpecification(OrderCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria != null) {
            if (StringUtils.hasText(criteria.getDescription())) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + criteria.getDescription().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(criteria.getStatus())) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}