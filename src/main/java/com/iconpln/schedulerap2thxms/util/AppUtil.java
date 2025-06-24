package com.iconpln.schedulerap2thxms.util;

import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtil {

    public static List<Map<String, Object>> convertResultsetToListStr(ResultSet rs) {
        List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            Object value = "";

            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= colCount; i++) {
                    try {
                        if (rs.getObject(i).toString().equals("") || rs.getObject(i).toString().equals("null")) {
                            value = null;
                        } else {
                            value = rs.getObject(i);
                        }
                    } catch (Exception e) {
                        value = null;
                    }
                    map.put(rsmd.getColumnName(i).toLowerCase(), value);
                }
                lst.add(map);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lst;
    }
}
