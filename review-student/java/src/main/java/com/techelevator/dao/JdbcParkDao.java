package com.techelevator.dao;

import com.techelevator.model.Park;
import jdk.nashorn.api.tree.ArrayLiteralTree;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.rmi.activation.ActivationGroup_Stub;
import java.util.ArrayList;
import java.util.List;

public class JdbcParkDao implements ParkDao {

    private final JdbcTemplate jdbcTemplate;
    //intellij wanted this to be final?

    public JdbcParkDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Park> getAllParks() {
        List<Park> parks = new ArrayList<>();
         String sql = "SELECT * FROM park ORDER BY location;";
         SqlRowSet result = jdbcTemplate.queryForRowSet(sql);

         while (result.next()) {
             Park park = mapRowToPark(result);
             parks.add(park);
         }
        return parks;
    }

    private Park mapRowToPark(SqlRowSet results) {
        Park park = new Park();
        park.setParkId(results.getInt("park_id"));
        park.setName(results.getString("name"));
        park.setLocation(results.getString("location"));
        park.setEstablishDate(results.getDate("establish_date").toLocalDate());
        park.setArea(results.getInt("area"));
        park.setVisitors(results.getInt("visitors"));
        park.setDescription(results.getString("description"));
        return park;
    }




}
