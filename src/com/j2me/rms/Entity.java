/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.rms;

import org.kxml2.io.KXmlParser;

/**
 *
 * @author willian
 */
public abstract class Entity {
    public static final String DBNULL_VALUE = "null";
    public static final String TAG_STRING_START = "<string>";
    public static final String TAG_STRING_FINISH = "</string>";
    public static final String TAG_ARRAY_STRING_START = "<ArrayOfString>";
    public static final String TAG_ARRAY_STRING_FINISH = "</ArrayOfString>";
    public static final String PARAM_STRING_TABLE = "table";
    public static final String PARAM_SEPARATOR_TABLE = "#";
    public static final String PARAM_SEPARATOR_COLUMN = "|";
    public static final String PARAM_IDENTIFIED = "=";
    
    private int id;
    private int indexRMS = -1;
    private Object key = null;
    private int MBStatus;
    protected String xml;

    public Entity(){
    }

    public Entity(Object key){
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndexRMS() {
        return indexRMS;
    }

    public void setIndexRMS(int indexRMS) {
        this.indexRMS = indexRMS;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public int getMBStatus() {
        return MBStatus;
    }

    public void setMBStatus(int MBStatus) {
        this.MBStatus = MBStatus;
    }

    public abstract Object getKey(byte[] bytes);
    
    public abstract String toStringXML();

    public static String readUTF(String value){
        if(value != null && value.equals(DBNULL_VALUE)){
            value = null;
        }

        return value;
    }

    public static String writeUTF(String value){
        if(value == null){
            value = DBNULL_VALUE;
        }

        return value;
    }
    
    /**
     *
     * @param parser
     */
    public abstract void parse(KXmlParser parser);
    
}