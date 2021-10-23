package com.techelevator.dao;

import com.techelevator.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcReservationDao implements ReservationDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
        Reservation reservation = new Reservation();
        reservation.setSiteId(siteId);
        reservation.setName(name);
        reservation.setFromDate(fromDate);
        reservation.setToDate(toDate);

        String sql = "INSERT INTO reservation(site_id,name,from_date,to_date,create_date) "+
                " VALUES(?,?,?,?,NOW()) RETURNING reservation_id;";
        return jdbcTemplate.queryForObject(sql, Integer.class, reservation.getSiteId(),reservation.getName(),reservation.getFromDate(),reservation.getToDate());
    }

    public List<Reservation> upcomingReservations(int parkId){
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT *\n" +
                "FROM reservation\n" +
                "INNER JOIN site ON reservation.site_id = site.site_id\n" +
                "INNER JOIN campground ON campground.campground_id = site.campground_id\n" +
                "INNER JOIN park ON park.park_id = campground.park_id\n" +
                "WHERE (reservation.from_date BETWEEN CURRENT_DATE AND CURRENT_DATE+30) AND park.park_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, parkId);
        while (result.next()){
            reservations.add(mapRowToReservation(result));
        }

        return reservations;
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
