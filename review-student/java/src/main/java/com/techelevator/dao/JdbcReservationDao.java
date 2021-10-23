package com.techelevator.dao;

import com.techelevator.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcReservationDao implements ReservationDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //        List<Campground> campgrounds = new ArrayList<>();
    //        String sql = "SELECT campground_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ?;";
    //        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, parkId);
    //
    //        while (result.next()) {
    //            Campground campground = mapRowToCampground(result);
    //            campgrounds.add(campground);
    //        }
    //        return campgrounds;

    //        List<Reservation> reservations = new ArrayList<>();
    //        String sql = "SELECT reservation_id FROM reservation ORDER BY reservation_id DESC LIMIT 1;";
    //        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, siteId, name, fromDate, toDate);
    //        ResultSet re =
    //        //int reservationNumber = (int) result.getObject(reservations);

    //A reservation requires a site ID, name to reserve under, a start date, and an end date.
    //
    //The user receives a confirmation ID (which is the new reservation_id from the database) once they submit their reservation.

    @Override
    public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
        String sql = "INSERT INTO reservation (site_id, name, from_date, to_date) " +
                "VALUES(?,?,?,?);";
        jdbcTemplate.update(sql,siteId,name,fromDate,toDate);
        return Integer.parseInt("SELECT DISTINCT lastval() FROM reservation;");
    }

    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation r = new Reservation();
        r.setReservationId(results.getInt("reservation_id"));
        r.setSiteId(results.getInt("site_id"));
        r.setName(results.getString("name"));
        r.setFromDate(results.getDate("from_date").toLocalDate());
        r.setToDate(results.getDate("to_date").toLocalDate());
        r.setCreateDate(results.getDate("create_date").toLocalDate());
        return r;
    }




}
