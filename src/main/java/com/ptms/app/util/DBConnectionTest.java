package com.ptms.app.util;
import java.sql.Connection;
public class DBConnectionTest {
    public static void main(String[] args)
    {
   try(Connection connection=DBConnection.getConnection())
   {
       System.out.println("Connection successful");
       System.out.println("Connect to: "+connection.getCatalog());

   }
   catch (Exception e)
   {
       System.out.println("Database connection failed");
       e.printStackTrace();
   }
    }
}
