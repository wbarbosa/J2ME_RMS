/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.

 * Project in Java ME for Reading and Writing RMS.
 * Copyright (C) 2013  Willian Lemos Barbosa
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.j2me.rms;

import com.j2me.rms.filter.RecordFilterKey;
import com.j2me.rms.filter.RecordFilterNotSynchronized;
import com.j2me.rms.filter.RecordFilterSynchronized;
import com.j2me.rms.ui.AlertRMS;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import com.j2me.rms.filter.RecordFilterId;

/**
 *
 * @author Willian Lemos Barbosa
 * @contact willian.lemos@gmail.com
 */
public abstract class DAO {

    private DataBase dataBase;
    private RecordEnumeration reEntity;
    private RecordEnumeration reEntityIndex;
    private int indexResultSet = 0;
    private boolean openTable = false;

    protected Entity[] resultSet;

    private String nameEntity;

    /**
     *
     * @param nameEntity
     */
    public DAO(String nameEntity) {
        try {
            this.nameEntity = nameEntity;
            this.dataBase = new DataBase(this.nameEntity, true);
            this.openTable = this.dataBase.isOpenDataBase();
        } catch (Exception ex) {
            AlertRMS.setTextAlert("Contructor " + nameEntity + " ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
        }

        //#mdebug info
        System.out.println("nome: " + getName());
        System.out.println("tamanho: " + getSizeAvailableBytes());
        System.out.println("usado: " + getSizeBytes());
        System.out.println("registros totais: " + getSizeAll());
        //#enddebug
    }

    /**
     *
     */
    public void close() {
        if (openTable) {
            this.dataBase.closeDataBase();
        }
    }

    /**
     *
     * @return
     */
    public boolean isOpenTable() {
        return openTable;
    }

    /**
     *
     * @return
     */
    public DataBase getDataBase() {
        return dataBase;
    }

    /**
     *
     * @return
     */
    public Date getLastModified() {
        try {
            return new Date(this.dataBase.getRecordStore().getLastModified());
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("getLastModified ERROR:");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return new Date(0);
        }
    }

    /**
     *
     * @return Returns the name of the entity
     */
    public String getNameEntity() {
        return this.nameEntity;
    }

    /**
     *
     * @return
     */
    public String getName() {
        try {
            return this.dataBase.getRecordStore().getName();
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("getName ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return "";
        }
    }

    /**
     *
     * @return
     */
    public int getSize() {
        if (resultSet != null) {
            return resultSet.length;
        } else {
            return 0;
        }
    }

    /**
     *
     * @return
     */
    public int getSizeAll() {
        try {
            return this.dataBase.getRecordStore().getNumRecords();
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("getSizeAll ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return 0;
        }
    }

    /**
     *
     * @return Returns the amount of space, in bytes, that the record store
     * occupies.
     */
    public int getSizeBytes() {
        try {
            return this.dataBase.getRecordStore().getSize();
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("getSizeBytes ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return 0;
        }
    }

    /**
     *
     * @return Returns the amount of additional room (in bytes) available for
     * this record store to grow.
     */
    public int getSizeAvailableBytes() {
        try {
            return this.dataBase.getRecordStore().getSizeAvailable();
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("getSizeAvailableBytes ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return 0;
        }
    }

    /**
     *
     * @return
     */
    public int getVersion() {
        try {
            return this.dataBase.getRecordStore().getVersion();
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("getVersion ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return 0;
        }
    }

    /**
     *
     * @return
     */
    public boolean hasNextElement() {
        boolean existNextElement = false;
        if (resultSet != null) {
            if (resultSet.length > 0) {
                if (indexResultSet < resultSet.length) {
                    existNextElement = true;
                }
            }
        }

        return existNextElement;
    }

    /**
     *
     * @return
     */
    public Entity nextElement() {
        Entity entity = resultSet[indexResultSet];
        indexResultSet++;

        return entity;
    }

    /**
     * Search records in the entity RMS
     *
     * @param recordFilter
     * @param recordComparator
     */
    protected void search(RecordFilter recordFilter, RecordComparator recordComparator) {
        try {
            clearResultSet();

            RecordStore store = getDataBase().getRecordStore();

            if (store != null) {
                reEntity = store.enumerateRecords(recordFilter, recordComparator, false);
                reEntityIndex = store.enumerateRecords(recordFilter, recordComparator, false);

                resultSet = new Entity[reEntity.numRecords()];

                int countEntity = 0;

                while (reEntity.hasNextElement()) {
                    try {
                        ByteArrayInputStream byteArrayInputStream = null;
                        DataInputStream dataInputStream = null;
                        byte[] dados = null;
                        dados = reEntity.nextRecord();
                        byteArrayInputStream = new ByteArrayInputStream(dados);
                        dataInputStream = new DataInputStream(byteArrayInputStream);
                        resultSet[countEntity] = byteToEntity(dataInputStream);
                        resultSet[countEntity].setIndexRMS(reEntityIndex.nextRecordId());
                        countEntity++;
                        byteArrayInputStream.close();
                        dataInputStream.close();
                    } catch (IOException ex) {
                        AlertRMS.setTextAlert("Search ERROR: ");
                        AlertRMS.setTextAlertException(ex.getMessage());
                        AlertRMS.show();
                        //#mdebug error
                        ex.printStackTrace();
                        //#enddebug
                    } catch (InvalidRecordIDException ex) {
                        AlertRMS.setTextAlert("Search ERROR: ");
                        AlertRMS.setTextAlertException(ex.getMessage());
                        AlertRMS.show();
                        //#mdebug error
                        ex.printStackTrace();
                        //#enddebug
                    } catch (RecordStoreNotOpenException ex) {
                        AlertRMS.setTextAlert("Search ERROR: ");
                        AlertRMS.setTextAlertException(ex.getMessage());
                        AlertRMS.show();
                        //#mdebug error
                        ex.printStackTrace();
                        //#enddebug
                    } catch (RecordStoreException ex) {
                        AlertRMS.setTextAlert("Search ERROR: ");
                        AlertRMS.setTextAlertException(ex.getMessage());
                        AlertRMS.show();
                        //#mdebug error
                        ex.printStackTrace();
                        //#enddebug
                    }
                }
            }
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("Search ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
        } catch (RecordStoreException ex) {
            AlertRMS.setTextAlert("Search ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
        }
    }    
    public void searchById(int id){
        search(new RecordFilterId(id, this), null);
    }
    /**
     * Search all records
     */
    public void searchAll() {
        searchAll(false);
    }

    /**
     * Search all records
     *
     * @param comparator
     */
    public void searchAll(RecordComparator comparator) {
        searchAll(false, comparator);
    }

    /**
     * Search all records
     *
     * @param orderByIndex - Sort the records generated by the index for the
     * record
     */
    public void searchAll(boolean orderByIndex) {
        searchAll(orderByIndex, null);
    }

    /**
     * Search all records
     *
     * @param orderByIndex - Sort the records generated by the index for the
     * record
     * @param comparator
     */
    public void searchAll(boolean orderByIndex, RecordComparator comparator) {
        if (orderByIndex) {
            search(null, comparator);
        } else {
            search(null, comparator);
        }
    }

    /**
     * Search records by key
     *
     * @param entity
     */
    public void searchKey(Entity entity) {
        search(new RecordFilterKey(entity), null);
    }

    /**
     * Find all records not synchronized
     */
    public void searchNotSynchronized() {
        searchNotSynchronized(-1);
    }

    /**
     * Find all records not synchronized
     *
     * @param changeStatusAs - Sets the status that the records found will be
     */
    public void searchNotSynchronized(int changeStatusAs) {
        search(new RecordFilterNotSynchronized(this), null);
        if (changeStatusAs != -1) {
            synchronizedData(changeStatusAs, false);
        }
    }

    /**
     * Sets the status for the records should not be synchronized
     *
     * @param changeStatusAs
     */
    public void synchronizedData(int changeStatusAs) {
        this.synchronizedData(changeStatusAs, true);
    }

    private void synchronizedData(int changeStatusAs, boolean search) {
        if (search) {
            search(new RecordFilterNotSynchronized(this), null);
        } else {
            resetResultSet();
        }

        while (hasNextElement()) {
            Entity entity = nextElement();

            entity.setMBStatus(changeStatusAs);

            update(entity);
        }
    }

    /**
     * Inserts the record in RMS
     *
     * @param entity
     * @return
     */
    public int insert(Entity entity) {
        int id = -1;
        byte[] dados = entityToByte(entity);

        id = getDataBase().addRecord(dados, 0, dados.length);

        entity.setIndexRMS(id);

        return id;
    }

    /**
     * Inserts the existing record in RMS
     *
     * @param entity
     * @return
     */
    public boolean update(Entity entity) {
        if (delete(entity) != -1) {
            if (insert(entity) != -1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean deleteRMS(int indexRMS) {
        try {
            return getDataBase().deleteRecord(indexRMS);
        } catch (Exception ex) {
            AlertRMS.setTextAlert("Delete ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
            return false;
        }
    }

    /**
     * Deletes the existing record in RMS
     *
     * @param entity
     * @return
     */
    public int delete(Entity entity) {
        if (deleteRMS(entity.getIndexRMS())) {
            return entity.getIndexRMS();
        } else {
            return -1;
        }
    }

    /**
     * Excludes records synchronized existing RMS
     */
    public void deleteSynchronized() {
        search(new RecordFilterSynchronized(this), null);
        while (hasNextElement()) {
            delete(nextElement());
        }
    }

    /**
     * Deletes all records of that entity existing in RMS
     */
    public void deleteAll() {
        try {
            searchAll(false, null);
            while (hasNextElement()) {
                deleteRMS(nextElement().getIndexRMS());
            }
        } catch (Exception ex) {
            AlertRMS.setTextAlert("DeleteAll ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            //#mdebug error
            ex.printStackTrace();
            //#enddebug
        }
    }
    
    public abstract Entity byteToEntity(DataInputStream dataInputStream);

    public abstract void entityToByte(DataOutputStream data, Entity entity);

    protected byte[] entityToByte(Entity entity) {
        try {
            ByteArrayOutputStream byteArray
                    = new ByteArrayOutputStream(); //cria um novo byte
            DataOutputStream data
                    = new DataOutputStream(byteArray);

            entityToByte(data, entity);

            data.flush();

            byte[] dados = byteArray.toByteArray();

            byteArray.close();
            data.close();

            return dados;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Clears the ResultSet for future searches
     */
    public void clearResultSet() {
        reEntity = null;
        reEntityIndex = null;
        resultSet = null;
        indexResultSet = 0;
    }

    private void resetResultSet() {
        indexResultSet = 0;
    }

    /*
     public class ComparatorIndex implements RecordComparator {
     public int compare(byte[] rec1, byte[] rec2) {
     int val1 = ((rec1[0] << 24) & 0xFF000000)
     + ((rec1[1] << 16) & 0x00FF0000)
     + ((rec1[2] << 8)  & 0x0000FF00)
     + ((rec1[3] << 0)  & 0x000000FF);;
     int val2 = ((rec2[0] << 24) & 0xFF000000)
     + ((rec2[1] << 16) & 0x00FF0000)
     + ((rec2[2] << 8)  & 0x0000FF00)
     + ((rec2[3] << 0)  & 0x000000FF);;

     if (val1 == val2)
     return RecordComparator.EQUIVALENT;
     else
     if (val1 > val2)
     return RecordComparator.FOLLOWS;
     //return RecordComparator.PRECEDES;
     else
     return RecordComparator.PRECEDES;
     //return RecordComparator.FOLLOWS;
     }
     }
     */
}
