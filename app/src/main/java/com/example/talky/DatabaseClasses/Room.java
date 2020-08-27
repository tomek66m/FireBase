package com.example.talky.DatabaseClasses;

import java.util.HashMap;
import java.util.Map;

public class Room {
    public String ID;
    public String ID1;
    public String ID2;

    public Room()
    {

    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", ID);
        result.put("ID1", ID1);
        result.put("ID2", ID2);
        return result;
    }
}
