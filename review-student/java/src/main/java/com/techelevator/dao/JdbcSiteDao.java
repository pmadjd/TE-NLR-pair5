package com.techelevator.dao;

import com.techelevator.model.Campground;
import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcSiteDao implements SiteDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSiteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Site> getSitesThatAllowRVs(int parkId) {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT site_id, site_number, max_occupancy, accessible, max_rv_length, utilities, site.campground_id " +
                "FROM site INNER JOIN campground ON site.campground_id = campground.campground_id " +
                "INNER JOIN park ON park.park_id = campground.park_id " +
                "WHERE max_rv_length>0 AND (park.park_id = ?);";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, parkId);

        while (result.next()) {
            Site site = mapRowToSite(result);
            sites.add(site);
        }
        return sites;
    }

    public List<Site> currentlyAvailableSites(int parkID){
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT *\n" +
                "FROM park\n" +
                "INNER JOIN campground ON park.park_id = campground.park_id\n" +
                "INNER JOIN site ON campground.campground_id = site.campground_id\n" +
                "WHERE site.site_id NOT IN (SELECT site_id FROM reservation WHERE (CURRENT_DATE BETWEEN from_date AND to_date))\n" +
                "AND park.park_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, parkID);

        while(result.next()){
            Site site = mapRowToSite(result);
            sites.add(site);
        }
        return sites;
    }

    private Site mapRowToSite(SqlRowSet results) {
        Site site = new Site();
        site.setSiteId(results.getInt("site_id"));
        site.setCampgroundId(results.getInt("campground_id"));
        site.setSiteNumber(results.getInt("site_number"));
        site.setMaxOccupancy(results.getInt("max_occupancy"));
        site.setAccessible(results.getBoolean("accessible"));
        site.setMaxRvLength(results.getInt("max_rv_length"));
        site.setUtilities(results.getBoolean("utilities"));
        return site;
    }
}
