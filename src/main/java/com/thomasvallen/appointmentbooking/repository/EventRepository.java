package com.thomasvallen.appointmentbooking.repository;

import com.thomasvallen.appointmentbooking.entity.Event;
import com.thomasvallen.appointmentbooking.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatusOrderByEventDateTimeAsc(EventStatus status);

}
