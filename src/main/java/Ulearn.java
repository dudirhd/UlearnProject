import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Ulearn {

    public static void main(String[] args) {
        try {
            var persons = getPersons();

            var connection = getConnection(persons);
            createDatabase(persons, connection);

            Tasks.run(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Person> getPersons() throws IOException, CsvException {
        var persons = new ArrayList<Person>();
        var csvreader = new CSVReader(new FileReader("Forbes.csv"));

        csvreader.readNext();

        for (var str : csvreader.readAll())
            persons.add(new Person(Integer.parseInt(str[0]), str[1], Double.parseDouble(str[2]), Integer.parseInt(str[3]), str[4], str[5], str[6]));
        return persons;
    }

    private static Connection getConnection(ArrayList<Person> list) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:forbes.db");
    }

    private static void createDatabase(ArrayList<Person> persons, Connection cnt) throws SQLException {
        var statement = cnt.createStatement();

        statement.execute("drop table 'forbes';");
        statement.execute("CREATE TABLE 'forbes' ('rank' int, 'name' varchar, 'networth' real,'age' int, 'country' text, 'source' text, 'industry' text); ");

        var ps = cnt.prepareStatement("INSERT INTO 'forbes' ('rank','name','networth','age','country','source','industry') VALUES (?,?,?,?,?,?,?);");

        for (var p : persons) {
            ps.setInt(1, p.getRank());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getNetworth());
            ps.setInt(4, p.getAge());
            ps.setString(5, p.getCountry());
            ps.setString(6, p.getSource());
            ps.setString(7, p.getIndustry());
            ps.executeUpdate();
        }

    }
}
