package com.driga.jstatscore.database.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface DatabaseService
{
     <T> Optional<T> query(Class<T> p0, String p1, DatabaseResult<T> p2, Object... p3);
    
    void execute(String p0, Object... p1);
    
    void update(String p0, Object... p1);
    
    ResultSet querys(String p0, Object... p1) throws SQLException;
    
    public interface DatabaseResult<T>
    {
        T accept(ResultSet p0) throws SQLException;
    }
}
