/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.rms.filter;

import com.j2me.rms.DAO;
import com.j2me.rms.DBStatusType;
import com.j2me.rms.Entity;
import com.j2me.rms.ui.AlertRMS;
import java.io.ByteArrayInputStream;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Willian
 */
public class RecordFilterNotSynchronized implements RecordFilter {

    private final DAO dao;
    
    public RecordFilterNotSynchronized(DAO dao) {
        this.dao = dao;
    }

    public boolean matches(byte[] persisted) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(persisted);
            java.io.DataInputStream dis = new java.io.DataInputStream(bis);

            Entity entity = dao.byteToEntity(dis);

            return entity.getMBStatus() != DBStatusType.SYNCHRONIZED;
        } catch (Exception ex) {
            AlertRMS.setTextAlert("RecordFilterNotSinchronized ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return false;
        }
    }
}