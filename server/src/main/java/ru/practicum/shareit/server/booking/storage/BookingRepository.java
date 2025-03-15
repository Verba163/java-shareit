package ru.practicum.shareit.server.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.enums.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId")
    List<Booking> findBookingsByBookerId(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start > CURRENT_TIMESTAMP")
    List<Booking> findBookingsByBookerIdAndStatusFuture(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.end < CURRENT_TIMESTAMP")
    List<Booking> findBookingsByBookerIdAndStatusPast(@Param("bookerId") Long bookerId);

    @Query("""
             SELECT b FROM Booking b WHERE b.booker.id = :bookerId\s
             AND b.start <= CURRENT_TIMESTAMP\s
             AND b.end >= CURRENT_TIMESTAMP
            \s""")
    List<Booking> findBookingsByBookerIdAndStatusCurrent(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND  b.status = :status")
    List<Booking> findBookingsByBookerIdAndStatus(@Param("bookerId") Long bookerId,
                                                  @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId")
    List<Booking> findBookingsByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId AND b.start > CURRENT_TIMESTAMP")
    List<Booking> findBookingsByOwnerIdAndStatusFuture(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId AND b.end < CURRENT_TIMESTAMP")
    List<Booking> findBookingsByOwnerIdAndStatusPast(@Param("ownerId") Long ownerId);

    @Query("""
             SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId\s
             AND  b.start <= CURRENT_TIMESTAMP\s
             AND b.end >= CURRENT_TIMESTAMP
            \s""")
    List<Booking> findBookingsByOwnerIdAndStatusCurrent(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId = :ownerId AND b.status = :status")
    List<Booking> findBookingsByOwnerIdAndStatus(@Param("ownerId") Long ownerId,
                                                 @Param("status") BookingStatus status);

}


