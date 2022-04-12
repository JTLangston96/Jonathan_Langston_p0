package dev.langst.entities;

import dev.langst.data.AccountDAO;
import dev.langst.utilities.*;

import java.sql.*;

public class AccountDAOPostgres implements AccountDAO {

    @Override
    public Account createAccount(Account account) {
        try{
            Connection conn = ConnectionUtil.createConnection();
            String sql = "insert into account values (default, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, account.getUserId());
            ps.setString(2, account.getAccountType());
            ps.setDouble(3, account.getBalance());

            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            int generatedId = rs.getInt("account_id");
            account.setAccountId(generatedId);
            return account;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Account getAccountById(int accountId) {

        try{
            Connection conn = ConnectionUtil.createConnection();
            String sql = "select * from account where account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, accountId);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            rs.next();

            if(accountId == rs.getInt("account_id")){
                return new Account(rs.getInt("account_id"), rs.getInt("customer_id"),
                        rs.getString("account_type"), rs.getDouble("balance"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Arraylist<Account> getAccountsByUserId(int userId) {

        try{
            Arraylist<Account> accounts = new Arraylist();

            Connection conn = ConnectionUtil.createConnection();
            String sql = "select account_id, account_type, balance from account inner join customer on customer.customer_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, userId);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            rs.next();

            for(int i = 0; rs.next(); i++){
                Account currentAccount = new Account(rs.getInt("account_id"), userId,
                        rs.getString("account_type"), rs.getDouble("balance"));
                accounts.add(currentAccount);
            }
            return accounts;
        }
        catch(SQLException e){

        }

        return null;
    }

    @Override
    public Account updateAccount(Account account) {
//        Account updatedAccount = new Account(account.getAccountId(), account.getUserId(),
//                account.getAccountType(), account.getBalance());

        try{
            Connection conn = ConnectionUtil.createConnection();
            String sql = "update account set balance = ?";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setDouble(1, account.getBalance());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();

            return new Account(account.getAccountId(), account.getUserId(),
                account.getAccountType(), account.getBalance());
        }
        catch (SQLException e){
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public boolean deleteAccountById(int accountId) {

        try{
            Connection conn = ConnectionUtil.createConnection();
            String sql = "delete from account where account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, accountId);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int deletedId = rs.getInt("account_id");

            if(deletedId == accountId){
                return true;
            }
            else{
                Logger.log("Account with the ID \"" + deletedId
                        + "\" was wrongfully deleted", LogLevel.WARNING);
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
