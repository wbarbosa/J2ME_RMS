/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.rms.filter;

import com.j2me.rms.DAO;
import com.j2me.rms.Entity;
import com.j2me.rms.ui.AlertRMS;
import java.io.ByteArrayInputStream;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author hugocampos
 */
public class RecordFilterId implements RecordFilter {
    private final int id;
    private final DAO dao;

    public RecordFilterId(int id, DAO dao) {
        this.id = id;
        this.dao = dao;
    }
    
    public boolean matches(byte[] bytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            java.io.DataInputStream dis = new java.io.DataInputStream(bis);

            Entity entity = dao.byteToEntity(dis);

            return entity.getId() == id;
        } catch (Exception ex) {
            AlertRMS.setTextAlert("RecordFilterSinchronized ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return false;
        }
    }
    
}
