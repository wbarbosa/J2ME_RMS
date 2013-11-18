/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.rms;

import com.j2me.rms.ui.AlertRMS;
import javax.microedition.rms.*;

/**
 *
 * @author willian
 */
public class DataBase {

    private RecordStore rsDataBase;
    private boolean openDataBase = false;

    protected DataBase(String dataBaseName) {
        this(dataBaseName, false);
    }

    protected DataBase(String dataBaseName, boolean createIfNecessaty) {
        try {
            this.rsDataBase =
                    RecordStore.openRecordStore(dataBaseName, createIfNecessaty);

            this.openDataBase = true;
        } catch (RecordStoreFullException rEx) {
            AlertRMS.setTextAlert("DataBase name "+ dataBaseName +" ERROR: ");
            AlertRMS.setTextAlertException(rEx.getMessage());
            AlertRMS.show();
            //#mdebug error
            rEx.printStackTrace();
            //#enddebug
        } catch (RecordStoreException ex) {
            AlertRMS.setTextAlert("DataBase name "+dataBaseName+" ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
        }
    }

    protected void closeDataBase() {
        try {
            this.rsDataBase.closeRecordStore();
        } catch (RecordStoreFullException rEx) {
            AlertRMS.setTextAlert("closeDataBase ERROR: ");
            AlertRMS.setTextAlertException(rEx.getMessage());
            AlertRMS.show();
            //#mdebug error
            rEx.printStackTrace();
            //#enddebug
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("closeDataBase ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
        } catch (RecordStoreException ex) {
            AlertRMS.setTextAlert("closeDataBase ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
        }
    }

    protected boolean isOpenDataBase() {
        return openDataBase;
    }

    protected RecordStore getRecordStore() {
        return this.rsDataBase;
    }

    public int addRecord(byte[] data, int offset, int numBytes) {
        try {
            return this.rsDataBase.addRecord(data, offset, numBytes);
        } catch (RecordStoreFullException rEx) {
            AlertRMS.setTextAlert("addRecord ERROR: ");
            AlertRMS.setTextAlertException(rEx.getMessage());
            AlertRMS.show();
            //#mdebug error
            rEx.printStackTrace();
            //#enddebug
            return -1;
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("addRecord ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return -1;
        } catch (RecordStoreException ex) {
            AlertRMS.setTextAlert("addRecord ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return -1;
        }
    }

    public boolean updateRecord(int idRecord, byte[] data, int offset, int numBytes) {
        try {
            this.rsDataBase.setRecord(idRecord, data, offset, numBytes);
            return true;
        } catch (RecordStoreFullException rEx) {
            AlertRMS.setTextAlert("updateRecord ERROR: ");
            AlertRMS.setTextAlertException(rEx.getMessage());
            AlertRMS.show();
            //#mdebug error
            rEx.printStackTrace();
            //#enddebug
            return false;
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("updateRecord ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return false;
        } catch (RecordStoreException ex) {
            AlertRMS.setTextAlert("updateRecord ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return false;
        }
    }

    public boolean deleteRecord(int indexRecord) {
        try {
            this.rsDataBase.deleteRecord(indexRecord);
            return true;
        } catch (RecordStoreFullException rEx) {
            AlertRMS.setTextAlert("deleteRecord ERROR: ");
            AlertRMS.setTextAlertException(rEx.getMessage());
            AlertRMS.show();
            //#mdebug error
            rEx.printStackTrace();
            //#enddebug
            return false;
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("deleteRecord ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return false;
        } catch (InvalidRecordIDException ex) {
            AlertRMS.setTextAlert("deleteRecord ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return false;
        } catch (RecordStoreException ex) {
            AlertRMS.setTextAlert("deleteRecord ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return false;
        }
    }

    //Delete um RecordStore criado.
    public static void deleteRecordStore(String nameRecordStore){
        // Verifica se o exite algum RecordSotore criado
        if(RecordStore.listRecordStores() != null){
            try {
                RecordStore.deleteRecordStore(nameRecordStore);
            } catch (RecordStoreException e) {
                AlertRMS.setTextAlert("deleteRecord ERROR: ");
                AlertRMS.setTextAlertException(e.getMessage());
                AlertRMS.show();
                //#mdebug error
                e.printStackTrace();
                //#enddebug
            }
        }
    }
}
