import java.sql.*;
import java.util.*;

public class DAO {

    void createUser(Connection conn, String newUserName, String newPassword) {
        String QUERY = "insert into user_login values ( '" + newUserName + "' , '" + newPassword + "' );";
        try {
            conn.prepareStatement(QUERY).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    Map<String, String> getLoginDetails(Connection conn, boolean isCompanyLogin) {

        Map<String, String> loginDetails = new LinkedHashMap<>();
        String tableName;
        if (isCompanyLogin)
            tableName = "company_login";
        else
            tableName = "user_login";

        String QUERY = "select * from " + tableName + ";";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            while (rs.next()) {
                loginDetails.put(rs.getString(1), rs.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginDetails;
    }

    List<String> getTableList(Connection conn) {
        List<String> tableList = new ArrayList<String>();

        String QUERY = "SHOW TABLES;";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            while (rs.next()) {
                if (!rs.getString(1).equals("user_login") && !rs.getString(1).equals("company_login"))
                    tableList.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableList;
    }

    List<String> getColumnList(Connection conn, String tableName) {
        List<String> columnList = new ArrayList<String>();

        String QUERY = "select * from " + tableName + " limit 1;";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                columnList.add(rsMetaData.getColumnName(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnList;
    }

    Map<String, Object> getValues(Connection conn, String tableName, int robotId) {

        Map<String, Object> values = new LinkedHashMap<>();
        String QUERY = "select * from " + tableName + " where id = " + robotId + ";";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= count; i++) {
                    values.put(rsMetaData.getColumnName(i), rs.getObject(i));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }

    String getSpecificValue(Connection conn, String tableName, int robotId, String columnName) {

        String specificValue = null;
        String QUERY = "select * from " + tableName + " where id = " + robotId + ";";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            while (rs.next())
                specificValue = rs.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specificValue;
    }

    void updateValues(Connection conn, String tableName, int robotId, int[] wishList) {

        String QUERY = "select * from " + tableName + " where id = " + robotId + ";";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            Scanner sc = new Scanner(System.in);

            System.out.println("Enter updated data:");
            for (int i = 0; i < wishList.length; i++) {
                // Write update query with dynamic object ... i.e the problem to use sc.next()
                // or sc.nextInt()
                System.out.print(rsMetaData.getColumnName(wishList[i]) + " : ");
                // System.out.println(rsMetaData.getColumnType(wishList[i]));
                String query;
                if (rsMetaData.getColumnType(wishList[i]) == 12)
                    query = "update " + tableName + " set " +
                            rsMetaData.getColumnName(wishList[i]) + " = "
                            + "'" + sc.nextLine() + "'" + " where id = " + robotId + ";";
                else
                    query = "update " + tableName + " set " +
                            rsMetaData.getColumnName(wishList[i]) + " = "
                            + "'" + sc.nextInt() + "'" + " where id = " + robotId + ";";
                conn.prepareStatement(query).execute();
            }
            sc.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
