package com.example.talky.DatabaseClasses;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Message {
    public String _ID;
    public String _Content;
    public String _SenderID;
    public String _RoomID;

    public Message(String _content, String _senderID, String _roomID)
    {
        this._ID = "";
        this._Content = _content;
        this._SenderID = _senderID;
        this._RoomID = _roomID;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("_ID", _ID);
        result.put("_Content", _Content);
        result.put("_SenderID", _SenderID);
        result.put("_RoomID", _RoomID);

        return result;
    }

}
