package com.project.vehiclevoyage.repository;

import com.project.vehiclevoyage.entities.BookingDetails;
import com.project.vehiclevoyage.service.BookingDetailsService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingDetailsRepository extends MongoRepository<BookingDetails, String> {
//    @Query("{$and: [{city: ?0}, {vehicleType: ?1}, {$or: ["
//            + "{startDate: {$lte: ?2}, endDate: {$gte: ?2}}, "
//            + "{startDate: {$lte: ?3}, endDate: {$gte: ?3}}, "
//            + "{$and: [{startDate: {$gte: ?2}}, {endDate: {$lte: ?3}}]}]}]}," +
//            " {bookingStatus: ?4}]")
//    List<BookingDetails> findOverlappingBookings(
//            @Param("city") String city,
//            @Param("vehicleType") String vehicleType,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("bookingStatus") String bookingStatus
//    );

    @Query("{$and: [{city: ?0}, {vehicleType: ?1}, {bookingStatus: ?4}, {$or: ["
            + "{startDate: {$lte: ?2}, endDate: {$gte: ?2}}, "
            + "{startDate: {$lte: ?3}, endDate: {$gte: ?3}}, "
            + "{$and: [{startDate: {$gte: ?2}}, {endDate: {$lte: ?3}}]}]}]}")
    List<BookingDetails> findOverlappingBookings(
            @Param("city") String city,
            @Param("vehicleType") String vehicleType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("bookingStatus") String bookingStatus
    );


    BookingDetails findByOrderId(String orderId);

    void deleteByOrderId(String orderId);

    BookingDetails findByVehicleId(String id);

    List<BookingDetails> findByUserId(String id);

    List<BookingDetails> findByOwnerId(String id);
}
