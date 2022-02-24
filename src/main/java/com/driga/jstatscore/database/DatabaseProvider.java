package com.driga.jstatscore.database;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.database.service.DatabaseService;
import com.driga.jstatscore.database.sustainer.DatabaseSustainer;

import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseProvider extends DatabaseSustainer implements DatabaseService {


    private JStatsCore plugin;
    private ExecutorService executorService;

    public DatabaseProvider() {
        this.plugin = JStatsCore.getInstance();
        this.executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public <T> Optional<T> query(Class<T> clazz, String query, DatabaseResult<T> resultSet, Object... objects) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            this.applyGeneric(preparedStatement, objects);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return Optional.of(resultSet.accept(result));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void execute(final String execute, final Object... objects) {
        final Throwable t2 = new Throwable();
        CompletableFuture.runAsync(() -> {
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(execute);
                try {
                    this.applyGeneric(preparedStatement, objects);
                    preparedStatement.execute();
                }
                catch (Throwable t) {
                    throw t;
                }
                finally {
                    if (preparedStatement != null) {
                        if (t2 != null) {
                            try {
                                preparedStatement.close();
                            }
                            catch (Throwable exception) {
                                t2.addSuppressed(exception);
                            }
                        }
                        else {
                            preparedStatement.close();
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }, this.executorService);
    }

    @Override
    public void update(String update, Object... objects) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(update)) {
            this.applyGeneric(preparedStatement, objects);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet querys(String query, Object... objects) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(query);
        this.applyGeneric(preparedStatement, objects);
        return preparedStatement.executeQuery();
    }

    @Override
    public void openConnection() {
        try {
            File fileSQL = new File(this.plugin.getDataFolder(), "database.db");
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + fileSQL);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.execute("CREATE TABLE IF NOT EXISTS players(name VARCHAR(15), tp DOUBLE, attributes TEXT)", new Object[0]);
            this.execute("CREATE TABLE IF NOT EXISTS booster(name VARCHAR(15), map TEXT, PRIMARY KEY (`name`))", new Object[0]);
            this.execute("INSERT OR IGNORE INTO booster VALUES (?,?)", "BOOSTER", "");
        }
    }

    @Override
    public void closeConnection() {
        try {
            this.connection.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void applyGeneric(PreparedStatement stm, Object... obj) throws SQLException {
        for (int i = 0; i != obj.length; ++i) {
            if (obj[i] != null) {
                DatabaseAdapter.getInstance().setGeneric(stm, i + 1, obj[i]);
            }
        }
    }
}
