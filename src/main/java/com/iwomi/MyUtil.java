/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iwomi;

import java.math.BigDecimal;
import java.sql.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.springframework.stereotype.Service;

@Service
public class MyUtil {
  public JSONArray convert( ResultSet rs )
    throws SQLException, JSONException
  {
    JSONArray json = new JSONArray();
    ResultSetMetaData rsmd = rs.getMetaData();

    while(rs.next()) {
      int numColumns = rsmd.getColumnCount();
      JSONObject obj = new JSONObject();

      for (int i=1; i<numColumns+1; i++) {
        String column_name = rsmd.getColumnName(i);

        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
         obj.put(column_name, rs.getArray(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
         obj.put(column_name, rs.getBoolean(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
         obj.put(column_name, rs.getBlob(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
         obj.put(column_name, rs.getDouble(column_name)); 
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
         obj.put(column_name, rs.getFloat(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
         obj.put(column_name, rs.getNString(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
         obj.put(column_name, rs.getString(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
         obj.put(column_name, rs.getDate(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
        obj.put(column_name, rs.getTimestamp(column_name));   
        }
        else{
         obj.put(column_name, rs.getObject(column_name));
        }
      }

      json.put(obj);
    }

    return json;
  }
  
  
  public JSONArray mapResultSet(ResultSet rs) throws SQLException, JSONException
	{ 
        JSONArray jArray = new JSONArray();
        JSONObject jsonObject = null;
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        do
        {
	        jsonObject = new JSONObject();
	        for (int index = 1; index <= columnCount; index++) 
	        {
	            String column = rsmd.getColumnName(index);
	            Object value = rs.getObject(column);
	            if (value == null) 
	            {
	                jsonObject.put(column, "");
	            } else if (value instanceof Integer) {
	                jsonObject.put(column, (Integer) value);
	            } else if (value instanceof String) {
	                jsonObject.put(column, (String) value);                
	            } else if (value instanceof Boolean) {
	                jsonObject.put(column, (Boolean) value);           
	            } else if (value instanceof Date) {
	                jsonObject.put(column, ((Date) value).getTime());                
	            } else if (value instanceof Long) {
	                jsonObject.put(column, (Long) value);                
	            } else if (value instanceof Double) {
	                jsonObject.put(column, (Double) value);                
	            } else if (value instanceof Float) {
	                jsonObject.put(column, (Float) value);                
	            } else if (value instanceof BigDecimal) {
	                jsonObject.put(column, (BigDecimal) value);
	            } else if (value instanceof Byte) {
	                jsonObject.put(column, (Byte) value);
	            } else if (value instanceof byte[]) {
	                jsonObject.put(column, (byte[]) value);                
	            } else {
	                throw new IllegalArgumentException("Unmappable object type: " + value.getClass());
	            }
        	}
        	jArray.put(jsonObject);
        }while(rs.next());
        return jArray;
    }
}