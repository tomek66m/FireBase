package com.example.talky.DatabaseClasses;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String _ID;
    public String _Name;
    public String _Surname;
    public String _Email;

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("_ID", _ID);
        result.put("_Name", _Name);
        result.put("_Surname", _Surname);
        result.put("_Email", _Email);

        return result;
    }

    public User()
    {
        _ID = "";
        _Name = "";
        _Surname = "";
        _Email = "";
    }


}
