package net.uqcloud.infs7202.project.restaurant.repository;

import net.uqcloud.infs7202.project.restaurant.repository.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, RestaurantTable.Key> {
    @Query("""
        select count(*)
        from RestaurantTable t
        where t.key.restaurant.id = ?1 and t.isActive = true
    """)
    int countTablesForRestaurant(int id);

    @Modifying
    @Query("""
        update RestaurantTable t
        set t.isActive = ?4
        where t.key.restaurant.id = ?1 and t.key.tableNumber >= ?2 and t.key.tableNumber <= ?3
    """)
    int updateIsActiveByTableNumberRange(int id, int start, int end, boolean isActive);
}
