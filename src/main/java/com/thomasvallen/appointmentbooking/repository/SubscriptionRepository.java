package com.thomasvallen.appointmentbooking.repository;

import com.thomasvallen.appointmentbooking.entity.NewsletterSubscriber;
import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<NewsletterSubscriber, Long> {

    Optional<NewsletterSubscriber> findByEmail(String email);

    boolean existsByEmail(String email);

    List<NewsletterSubscriber> findByIsActiveTrue();

    @Query("SELECT ns FROM NewsletterSubscriber ns WHERE ns.isActive = true AND :type MEMBER OF ns.types")
    List<NewsletterSubscriber> findActiveSubscribersByType(@Param("type") SubscriptionType type);

    @Query("SELECT COUNT(ns) FROM NewsletterSubscriber ns WHERE ns.isActive = true")
    Long countActiveSubscribers();
}
