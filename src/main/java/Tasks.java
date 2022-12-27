import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public class Tasks {

    public static void run(Connection cnt) throws SQLException, FileNotFoundException {
        //Постройте график общего капитала участников Forbes, объединив их по странам.
        var statement = cnt.createStatement();
        var printWriter = new PrintWriter(new File("dataForbesGraph.txt"));

        var resultSet = statement.executeQuery("SELECT printf(\"%.2f\", sum(networth)) as net, country  FROM forbes GROUP BY country;");

        while (resultSet.next()) {
            printWriter.println(resultSet.getString("country") + ";=" + resultSet.getString("net").replace('.', ','));
        }

        printWriter.close();

        //Выведите самого молодого миллиардера из Франции, капитал которого превышает 10 млрд
        System.out.println(statement.executeQuery("select *, min(age) from forbes where country ='France' and networth > 10;").getString("name"));


        //Выведите в консоль название штата, в котором произошло самое глубокое землетрясение в 2013 году
        resultSet = statement.executeQuery("select name,source, max(networth)  from forbes where country ='United States' and industry='Energy ';");
        System.out.println(resultSet.getString("name") + " " + resultSet.getString("source"));
        statement.close();
    }


}
