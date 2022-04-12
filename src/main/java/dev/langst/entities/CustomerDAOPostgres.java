package dev.langst.entities;

import dev.langst.data.CustomerDAO;
import dev.langst.utilities.ConnectionUtil;
import dev.langst.utilities.LogLevel;
import dev.langst.utilities.Logger;
import org.postgresql.util.PSQLException;
import sun.rmi.runtime.Log;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class CustomerDAOPostgres implements CustomerDAO {


    @Override
    public Customer createCustomer(Customer customer) {
        try {
            Connection conn = ConnectionUtil.createConnection();
            String sql = "insert into customer values (default, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getUserName());
            ps.setString(4, customer.getPassword());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            int generatedId = rs.getInt("customer_id");
            customer.setId(generatedId);
            return customer;

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(e.getMessage(), LogLevel.ERROR);
            return null;
        }
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        Customer result = null;

        try{
            Connection conn = ConnectionUtil.createConnection();
            String sql  = "select * from customer where username = ?";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, username);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            rs.next();

            if(rs.getString("username").equals(username)){
                result = new Customer(rs.getInt("customer_id"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("username"), rs.getString("password"));
                return result;
            }
            else{
                return null;
            }

        }
        catch(SQLException e){
            Logger.log(e.getMessage(), LogLevel.ERROR);
            return null;
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        try{
            Customer result = null;

            Connection conn = ConnectionUtil.createConnection();
            String sql  = "update customer set first_name = ?, last_name = ?, username = ?, " +
                    "password = ?, checking_account = ?, savings_account = ? where customer_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getUserName());
            ps.setString(4, customer.getPassword());
            ps.setInt(5, customer.getCheckingAccount());
            ps.setInt(6, customer.getSavingsAccount());
            ps.setInt(7, customer.getId());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            result = new Customer(rs.getInt("customer_id"), rs.getString("first_name"),
                    rs.getString("last_name"), rs.getString("username"),
                    rs.getString("password"));

            return result;
        }
        catch (SQLException e){
            Logger.log(e.getMessage(), LogLevel.ERROR);
            return null;
        }
    }

    @Override
    public boolean deleteCustomerById(int id) {
        int deletedId;

        try {
            Connection conn = ConnectionUtil.createConnection();
            String sql = "delete from customer where customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, id);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            deletedId = rs.getInt("customer_id");

            if(deletedId == id){
                return true;
            }
            else{
                Logger.log("Customer with the ID \"" + deletedId
                        + "\" was wrongfully deleted", LogLevel.WARNING);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(e.getMessage(), LogLevel.ERROR);

            return false;
        }
    }
}
