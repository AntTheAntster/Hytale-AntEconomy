package uk.anttheantster.anteconomy.utils;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import uk.anttheantster.anteconomy.AntEconomy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SQLGetter {
    private AntEconomy plugin;
    public SQLGetter(AntEconomy plugin){
        this.plugin = plugin;
    }

    public void createTable() {
        PreparedStatement ps;

        try {
            ps = plugin.mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS antseconomy "
                    + "(UUID CHAR(36),NAME VARCHAR(32),BALANCE BIGINT, PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(UUID uuid, String name, int defaultBalance) {
        try {

            if (!exists(uuid)){
                PreparedStatement ps2 = plugin.mysql.getConnection().prepareStatement("INSERT antseconomy"
                        + " (UUID,NAME,BALANCE) VALUES (?,?,?)");
                ps2.setString(1, uuid.toString());
                ps2.setString(2, name);
                ps2.setInt(3, defaultBalance);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = plugin.mysql.getConnection().prepareStatement("SELECT * FROM antseconomy WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setBalance(int balance, UUID uuid) {
        try {
            PreparedStatement ps = plugin.mysql.getConnection().prepareStatement("UPDATE antseconomy SET BALANCE=? WHERE UUID=?");
            ps.setInt(1, balance);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerName(UUID uuid) {
        try {
            PreparedStatement ps = plugin.mysql.getConnection().prepareStatement("SELECT NAME FROM antseconomy WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String pName = rs.getString("NAME");
                return pName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPlayerName(UUID uuid, String name) {
        try {
            PreparedStatement ps = plugin.mysql.getConnection().prepareStatement("UPDATE antseconomy SET NAME=? WHERE UUID=?");
            ps.setString(1, name);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Integer> loadAllBalances() {
        Map<UUID, Integer> out = new HashMap<>();

        try {
            PreparedStatement ps = plugin.mysql.getConnection().prepareStatement("SELECT UUID, BALANCE FROM antseconomy");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("UUID"));
                int bal = rs.getInt("BALANCE");
                out.put(uuid, bal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public UUID getUUIDByName(String name) {
        try {
            PreparedStatement ps = plugin.mysql.getConnection().prepareStatement("SELECT UUID FROM antseconomy WHERE LOWER(NAME) = LOWER(?)");
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return UUID.fromString(rs.getString("UUID"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getBalance(UUID uuid) {
        try {
            PreparedStatement ps = plugin.mysql.getConnection().prepareStatement("SELECT BALANCE FROM antseconomy WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("BALANCE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
