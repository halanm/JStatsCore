package com.driga.jstatscore.database.sustainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseSustainer
{
    protected Connection connection;
    
    public abstract void openConnection();
    
    public abstract void closeConnection();
    
    protected static class DatabaseAdapter
    {
        public static DatabaseAdapter adapter;
        
        public static DatabaseAdapter getInstance() {
            if (DatabaseAdapter.adapter == null) {
                DatabaseAdapter.adapter = new DatabaseAdapter();
            }
            return DatabaseAdapter.adapter;
        }
        
        public void setGeneric(PreparedStatement stm, int index, Object obj) throws SQLException {
            String simpleName = obj.getClass().getSimpleName();
            switch (simpleName) {
                case "String": {
                    stm.setString(index, (String)obj);
                    break;
                }
                case "Integer": {
                    stm.setInt(index, (int)obj);
                    break;
                }
                case "Long": {
                    stm.setLong(index, (long)obj);
                    break;
                }
            }
        }
    }
}
