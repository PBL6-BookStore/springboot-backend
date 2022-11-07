package com.pbl6.bookstore.domain.prefetch;

import com.pbl6.bookstore.domain.entity.OrderStatusEntity;
import com.pbl6.bookstore.domain.entity.RoleEntity;
import com.pbl6.bookstore.domain.repository.jpa.OrderStatusRepository;
import com.pbl6.bookstore.domain.repository.jpa.RoleRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lkadai0801
 * @since 06/11/2022
 */

@Component
@Getter
public class PrefetchEntityProvider {
    private final List<RoleEntity> roles;
    private final List<OrderStatusEntity> orderStatuses;
    private final Map<Long /* */, RoleEntity> roleMap;
    private final Map<Long, OrderStatusEntity> orderStatusMap;

    public PrefetchEntityProvider(RoleRepository roleRepository, OrderStatusRepository orderStatusRepository) {
        this.roles = roleRepository.findAll();
        this.orderStatuses = orderStatusRepository.findAll();
        this.roleMap = this.roles.stream().collect(Collectors.toMap(RoleEntity::getId, r -> r, (r1, r2) -> r1));
        this.orderStatusMap = this.orderStatuses.stream().collect(Collectors.toMap(OrderStatusEntity::getId, o -> o, (o1, o2) -> o1));
    }
}
