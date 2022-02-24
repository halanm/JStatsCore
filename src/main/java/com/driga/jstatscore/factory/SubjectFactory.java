package com.driga.jstatscore.factory;

import com.driga.jstatscore.JStatsCore;
import com.driga.jstatscore.api.prototype.Subject;
import com.driga.jstatscore.database.DatabaseProvider;
import com.driga.jstatscore.prototype.JSubject;
import com.driga.jstatscore.provider.SubjectProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SubjectFactory {

    private static SubjectFactory subjectFactory;
    private JStatsCore plugin;
    private DatabaseProvider databaseProvider;

    public SubjectFactory() {
        this.plugin = JStatsCore.getInstance();
        this.databaseProvider = this.plugin.getDatabaseProvider();
    }

    public static SubjectFactory getInstance() {
        if (SubjectFactory.subjectFactory == null) {
            SubjectFactory.subjectFactory = new SubjectFactory();
        }
        return SubjectFactory.subjectFactory;
    }

    public Subject find(Player player) {
        Optional<Subject> optional = this.databaseProvider.query(Subject.class, "SELECT * FROM players WHERE name = ?", resultSet -> {
            Subject subject = new JSubject(player.getName(), resultSet.getDouble("tp"), resultSet.getString("attributes"));
            resultSet.close();
            return subject;
        }, player.getName());
        if (optional.isPresent()) {
            return optional.get();
        }
        String map = SubjectProvider.getInstance().defaultMap().toString();
        final Subject subject2 = new JSubject(player.getName(), 0.0, map);
        this.databaseProvider.execute("INSERT INTO players VALUES (?,?,?)", player.getName(), "0.0", map);
        return subject2;
    }
}
