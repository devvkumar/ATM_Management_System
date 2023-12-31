/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Actions;

import helper.AccountDetails_entity;
import helper.UserAccount_entity;
import helper.javaConnect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Dev Kumar
 */
public class OperationsInDatabase {
    
    static Connection conn;
    static PreparedStatement pst;
    static ResultSet rs;
    
    // call from 'Submit' button, 'Deposit' class of 'Account' package
    public static boolean saveIntoUser(UserAccount_entity obj)
    {
        boolean b = false;
//                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_dbms", "root", "1996");

        conn = javaConnect.connectDb();             // connection with database
        
        // 'Query' for insert data into 'UserAccount' table
        String sql = "insert into UserAccount(FormNo, Name, F_Name, Date, Month, Year, Gender, Address, City, Pincode, State, AadharNo, ContactNo, Sec_Ques, Sec_Ans, Email, Occupation, Account_No, Acc_Type, Card_Num, Pin_Num, Services, CurrentDate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try
        {
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, obj.getFormNO());
            pst.setString(2, obj.getName());
            pst.setString(3, obj.getfName());
            pst.setInt(4, obj.getDate());
            pst.setString(5, obj.getMonth());
            pst.setInt(6, obj.getYear());
            pst.setString(7, obj.getGender());
            pst.setString(8, obj.getAddress());
            pst.setString(9, obj.getCity());
            pst.setInt(10, obj.getPinCode());
            pst.setString(11, obj.getState());
            pst.setString(12, obj.getAadhar());
            pst.setString(13, obj.getContact());
            pst.setString(14, obj.getSec_ques());
            pst.setString(15, obj.getSec_ans());
            pst.setString(16, obj.getEmail());
            pst.setString(17, obj.getOccupation());
            pst.setString(18, obj.getAccNo());
            pst.setString(19, obj.getAccType());
            pst.setString(20, obj.getCardNo());
            pst.setString(21, obj.getPinNo());
            pst.setString(22, obj.getService());
            pst.setString(23, obj.getCurrentdate());
            
            pst.execute();
            
            b = true;
            
            pst.close();
            conn.close();
            
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return b;
    }
    
    // call from 'Submit' button, 'Deposit' class of 'Account' package
    public static boolean saveIntoAccountDetails(UserAccount_entity obj)
    {
        boolean b = false;
        
        // 'Query' for insert data into 'AccountDetails' table
        String sql = "insert into AccountDetails(AccountNo, AccountType, HolderName, CardNo, Pin, AccountBalance, LastTransaction, TransactionDate) values(?,?,?,?,?,?,?,?)";
        
        try
        {
            conn = javaConnect.connectDb();
            pst = conn.prepareStatement(sql);
            
            pst.setString(1, obj.getAccNo());
            pst.setString(2, obj.getAccType());
            pst.setString(3, obj.getName());
            pst.setString(4, obj.getCardNo());
            pst.setString(5, obj.getPinNo());
            pst.setString(6, obj.getDepositeAmmount());         // set 'Current Account Balance'
            pst.setString(7, obj.getDepositeAmmount());         // set 'Last Transaction'
            pst.setString(8, obj.getCurrentdate());             // set 'Last Transaction Date & time'
            
            pst.execute();
            
            b = true;
            
            pst.close();
            conn.close();
            
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return b;
    }
    
    // call from 'validateLogin()' method, 'Login' class of 'Account' package
    // for get all data in "AccountDetails" table using CardNumber & pinNumber
    public static AccountDetails_entity getAccountDetails(String cardNumber, String pin)
    {
        // having getters & setters, 'AccountDetails_entity' class of 'Helper' package
        AccountDetails_entity obj = new AccountDetails_entity();        // holds 'Account Details' information
        
        // Query for fetching 'Account Details Information' from 'AccountDetails' table
        String sql = "select * from AccountDetails where CardNo='"+cardNumber+"' and Pin='"+pin+"'";
        try
        {
            conn = javaConnect.connectDb();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next())
            {
                // set 'Account Information' using setters('AccountDetails_entity' class, 'Helper' package)
                obj.setCardNo(cardNumber);
                obj.setPin(pin);
                obj.setAccountNo(rs.getString("AccountNo"));
                obj.setAccountType(rs.getString("AccountType"));
                obj.setName(rs.getString("HolderName"));
                obj.setAccBalance(rs.getString("AccountBalance"));
                obj.setLastTransaction(rs.getString("LastTransaction"));
                obj.setTransactionDate(rs.getString("TransactionDate"));
            }
            
            rs.close();
            pst.close();
            conn.close();
            
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return obj;
    }
    
    // call from 'Submit' button, 'Deposit' class of 'Actions' package
    static boolean updateAccountBalance(AccountDetails_entity obj)
    {
        boolean b =false;
        
        // Query for Update 'AccountBalance', 'LastTransactionAmmount' and 'LastTransactionDate' in 'AccountDetails' table
        String sql = "update AccountDetails set AccountBalance='"+obj.getAccBalance()+"', LastTransaction='"+obj.getLastTransaction()+"', TransactionDate='"+obj.getTransactionDate()+"' where CardNo='"+obj.getCardNo()+"'";
        
        try
        {
            conn = javaConnect.connectDb();
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            
            pst.close();
            conn.close();
            
            b = true;
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return b;
    }
    
    // call from 'ChangePin(AccountDetails_entity)' constructor, 'ChangePin' class of 'Actions' package
    // for fetching 'Security Question' and set into corresponding jLabel
    public static String getSecurityQuestion(String cardNum)
    {
        String ques = null;
        
        // Query for fetching 'Security Question' from 'UserAccount' table
        String sql = "select Sec_Ques from UserAccount where Card_Num='"+cardNum+"'";
        try
        {
            conn = javaConnect.connectDb();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next())
                ques = rs.getString("Sec_Ques");
            
            rs.close();
            pst.close();
            conn.close();
        
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
            
        return ques;
    }
    
    // call from 'validateData()' method, 'ChangePin' class of 'Actions' package
    // for fetching 'Security Answer' and match with entered 'SecurityAnswer'
    public static String getSecurityAnswer(String cardNum)
    {
        String ans = null;
        
        // Query for fetching 'Security Answer' from 'UserAccount' table
        String sql = "select Sec_Ans from UserAccount where Card_Num='"+cardNum+"'";
        try
        {
            conn = javaConnect.connectDb();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next())
                ans = rs.getString("Sec_Ans");
            
            rs.close();
            pst.close();
            conn.close();
        
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
            
        return ans;
    }
    
    // call from 'Submit' button, 'ChangePin' class of 'Actions' package
    // for Update 'Pin Number' in database as well as current 'Login session'
    public static boolean updatePin(String cardNum, String pin)
    {
        boolean b = false;
        
        // Query for Update 'Pin Number' in 'AccountDetails' table
        String sql = "update AccountDetails set Pin='"+pin+"' where CardNo='"+cardNum+"'";
        
        try
        {
            conn = javaConnect.connectDb();
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
            
            pst.close();
            conn.close();
            
            b = true;
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
        
        return b;
    }
    
}
