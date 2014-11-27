package ru.cscenter.practice.recsys.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Collection;


public class DataBaseQueue {

    private final JdbcTemplate dbAccess;

    public DataBaseQueue(JdbcTemplate template) {
        dbAccess = template;
    }

    public int size() {
        return (Integer)dbAccess.queryForObject("select count(*) from queue", Integer.class);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Integer o) {
        Integer result = (Integer)dbAccess.queryForObject("select userid from queue where userid = ?", Integer.class);
        return o.equals(result);
    }


    public boolean push(Pair<Integer, Integer> userFlatPair) {
        return dbAccess.update("insert into queue value(?,?)", new Object[]{userFlatPair.fst(), userFlatPair.snd()}) == 1;
    }

    public Pair<Integer, Integer> pop() {
        SqlRowSet rowSet = dbAccess.queryForRowSet("select * from queue limit 1");

        if( (rowSet != null ) && rowSet.next()) {
            Pair<Integer, Integer> result = Pair.of(rowSet.getInt("userid"), rowSet.getInt("flatid"));
            dbAccess.execute("delete from queue limit 1");
            return result;
        }

        return null;
    }

    public boolean addAll(Collection<Pair<Integer,Integer>> collection) {
        boolean result = true;
        for(Pair<Integer,Integer> currentElement : collection)
            result &= push(currentElement);
        return result;
    }

}
