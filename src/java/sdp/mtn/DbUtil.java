/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdp.mtn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.csapi.schema.parlayx.data.v1_0.UserID;

/**
 *
 * @author User-PC
 */
class DbUtil {

    private static Connection getConnection() {
        Connection con = null;
//        boolean UNDONE = true;
        //while (UNDONE) {
        try {
            Context env;
            env = (Context) new InitialContext();//.lookup("java:comp/env");
//            System.out.println("App name is " + env.lookup("java:app/AppName"));
            DataSource ds = (DataSource) env.lookup("jdbc/charginggw");

            try {
                con = ds.getConnection();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
//            java.util.logging.Logger.getLogger(AccessDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    static void saveDataSyncPayload(String data) throws SQLException {
        String sql;
        sql = "insert into charginggw.datasync (uniq_payload ) values (?)";
        Connection con = getConnection();
        try {
            PreparedStatement stat = con.prepareStatement(sql);

            stat.setString(1, data);
            System.out.println(stat.toString());
            stat.execute();
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static boolean confirmEligibility(UserID userID) {
        String sql;
        sql = "select EXISTS(select * from charginggw.processed_rewards where phone = ? and date > DATE_SUB(now(),INTERVAL 45 DAY))";
        boolean notEligible = true;
        Connection con = getConnection();
        try {
            PreparedStatement stat = con.prepareStatement(sql);

            stat.setString(1, userID.getID());
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                notEligible = rs.getBoolean(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return !notEligible;
    }

    static void saveReward(UserID userID, String network, int amount) {
        String sql;
        sql = "insert into charginggw.processed_rewards (phone,value,date,network ) values (?,?,now(),?)";
        Connection con = getConnection();
        try {
            PreparedStatement stat = con.prepareStatement(sql);
            stat.setString(1, userID.getID());
            stat.setInt(2, amount);
            stat.setString(3, network);
            System.out.println(stat.toString());
            stat.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
