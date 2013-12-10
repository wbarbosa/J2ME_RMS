/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.rms.comparator;

import com.j2me.rms.DAO;
import com.j2me.rms.DBStatusType;
import com.j2me.rms.Entity;
import com.j2me.rms.ui.AlertRMS;
import java.io.ByteArrayInputStream;
import javax.microedition.rms.RecordComparator;

/**
 *
 * @author hugocampos
 */
public class RecordComparatorId implements RecordComparator {
    
    private final DAO dao;
    
    public RecordComparatorId(DAO dao) {
        this.dao = dao;
    }

    public int compare(byte[] original, byte[] compare) {        
        ByteArrayInputStream bytesOriginal = new ByteArrayInputStream(original);
        ByteArrayInputStream bytesCompare = new ByteArrayInputStream(compare);
        java.io.DataInputStream inputOriginal = new java.io.DataInputStream(bytesOriginal);
        java.io.DataInputStream inputCompare = new java.io.DataInputStream(bytesCompare);

        Entity entityOriginal = dao.byteToEntity(inputOriginal);
        Entity entityCompare = dao.byteToEntity(inputCompare);

        if(entityOriginal.getId() == entityCompare.getId()){
            return RecordComparator.EQUIVALENT;
        }
        else if(entityOriginal.getId()< entityCompare.getId()){
            return RecordComparator.FOLLOWS;
        }
        else{
            return RecordComparator.PRECEDES;
        }
    }
}
