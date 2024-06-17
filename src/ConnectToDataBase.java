import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ConnectToDataBase {
    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String user = "root";
    private static final String password = "....";
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public ConnectToDataBase() { }

    // Метод для выполнения запросов, которые изменяют данные в базе данных
    public void executeUpdate(String query) {
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { }
            try { stmt.close(); } catch(SQLException se) { }
        }
    }

    // Метод для выполнения запросов, которые возвращают данные из базы данных
    public void executeQuery(String query, int Count, List result){
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                if(Count == 0){
                    result.add(new Client(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                }
                else if(Count == 1){
                    result.add(new Car(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getBoolean(5)));
                }
                else if(Count == 2){
                    result.add(new RentalInfo(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try { con.close(); } catch(SQLException se) { }
            try { stmt.close(); } catch(SQLException se) { }
            try { rs.close(); } catch(SQLException se) { }
        }
    }
}
