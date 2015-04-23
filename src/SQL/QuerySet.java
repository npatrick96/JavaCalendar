package SQL;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by John on 4/17/2015.
 */
public class QuerySet<T extends SQLRow> {

    /*
    * Lazy Evaluator for sql sets.
    * Customizable lazyness
    */

    private int size, currentIndex;
    private ArrayList<Integer> cached;
    private String query, tableName, where, database;
    private Class<?> type;

    public QuerySet(){
        this.size = 0;
        this.currentIndex = 0;
        this.cached = new ArrayList<>();
        this.query = "";
    }

    public void prepare(Class<?> type){
        this.type = type;
    }

    public void setQuery(String tableName, String where){
        this.tableName = tableName;
        this.where = where;
        this.query = "SELECT id FROM " + tableName + " WHERE " + where;
        this.populateIDs();
    }

    public void setDatabase(String database){
        this.database = database;
    }

    public T next(){
        if (this.currentIndex >= this.size){
            throw new IndexOutOfBoundsException("There are no more results\nCurrent Index: " + this.currentIndex + "\nMax Index: " + this.size);
        }
        Integer id = this.cached.get(currentIndex);
        ResultSet resultSet = this.query("SELECT * FROM " + tableName + " WHERE id='" + id + "'");

        try {
            T item = newObject();
            T returned = (T)item.getFromResultSet(resultSet).get(0);
            return returned;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        currentIndex++;

        return null;
    }

    public boolean hasNext(){
        return this.currentIndex < this.size;
    }

    private void populateIDs(){
        try {
            ResultSet ids = query(this.query);
            if (ids == null) {
                this.size = 0;
                return;
            }
            while(ids.next()){
                Integer id = ids.getInt(0);
                this.cached.add(id);
            }

            this.size = this.cached.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private T newObject() throws IllegalAccessException, InstantiationException {
        Object object = type.newInstance();
        return (T)object;
    }

    private ResultSet query(String query){

        Connection conn = null;
        ResultSet set = null;

        try{
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection("jdbc:sqlite:" + database );
            Statement q = conn.createStatement();
            q.setQueryTimeout(30);

            set = q.executeQuery(query);

        } catch (SQLException e){
        }
        catch (ClassNotFoundException e) {
        }

        return set;
    }

    public boolean tableExists() {
        try {
            String check = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
            ResultSet data = query(check);
            boolean tableExists = isResultSetEmpty(data);
            data.close();
            return tableExists;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isResultSetEmpty(ResultSet set) throws SQLException {
        if (set.isBeforeFirst()){
            return true;
        }
        return false;
    }
}
