package com.applicationsec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("index")
public class Controller {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String prequery = "SELECT * FROM USERS WHERE id=";

    @GetMapping
    @ResponseBody
    public String index(@RequestParam String name) {
        String query = this.prequery+name;
        jdbcTemplate.execute(query);
        return query;
    }

}
