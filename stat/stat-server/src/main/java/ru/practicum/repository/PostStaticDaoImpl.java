package ru.practicum.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.practicum.dto.PostStaticDtoForResponse;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PostStaticDaoImpl implements PostStaticDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<PostStaticDtoForResponse> findAllStatic(LocalDateTime startDate, LocalDateTime endDate) {
        String sqlQuery = "select COUNT(id),app,uri from stat where created between ? and ? GROUP BY uri,app ";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, startDate, endDate);
        List<PostStaticDtoForResponse> postStaticDtoForResponseList = new ArrayList<>();
        while (userRows.next()) {
            PostStaticDtoForResponse postStaticDtoForResponse = new PostStaticDtoForResponse();
            postStaticDtoForResponse.setHits(userRows.getInt("count"));
            postStaticDtoForResponse.setUri(userRows.getString("uri"));
            postStaticDtoForResponse.setApp(userRows.getString("app"));
            postStaticDtoForResponseList.add(postStaticDtoForResponse);
        }
        return postStaticDtoForResponseList;
    }

    @Override
    public List<PostStaticDtoForResponse> findAllUniqStatic(LocalDateTime startDate, LocalDateTime endDate) {
        String sqlQuery = "select COUNT(DISTINCT ip),app,uri from stat where created between ? and ? GROUP BY uri,app";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, startDate, endDate);
        List<PostStaticDtoForResponse> postStaticDtoForResponseList = new ArrayList<>();
        while (userRows.next()) {
            PostStaticDtoForResponse postStaticDtoForResponse = new PostStaticDtoForResponse();
            postStaticDtoForResponse.setHits(userRows.getInt("count"));
            postStaticDtoForResponse.setUri(userRows.getString("uri"));
            postStaticDtoForResponse.setApp(userRows.getString("app"));
            postStaticDtoForResponseList.add(postStaticDtoForResponse);
        }
        return postStaticDtoForResponseList;
    }
}
