/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.rms;

import com.j2me.rms.ui.AlertRMS;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
/**
 *
 * @author willian
 */
public abstract class DAO{
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
    public DAO(String nameEntity){
        try{
            this.nameEntity = nameEntity;
            this.dataBase = new DataBase(this.nameEntity, true);
            this.openTable = this.dataBase.isOpenDataBase();
        }catch(Exception ex){
            AlertRMS.setTextAlert("Contructor "+ nameEntity +" ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
        }

        /*
        System.out.println("nome: "+getName());
        System.out.println("tamanho: "+getSizeAvailableBytes());
        System.out.println("usado: "+getSizeBytes());
        System.out.println("registros totais: "+getSizeAll());
         */
    }

    /**
     *
     */
    public void close(){
        if(openTable){
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
            ex.printStackTrace();
            return new Date(0);
        }
    }

    /**
     *
     * @return Retorna o nome da entidade
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
            ex.printStackTrace();
            return "";
        }
    }

    /**
     *
     * @return
     */
    public int getSize(){
        if(resultSet != null){
            return resultSet.length;
        }else{
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
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @return Returns the amount of space, in bytes, that the record store occupies.
     */
    public int getSizeBytes() {
        try {
            return this.dataBase.getRecordStore().getSize();
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("getSizeBytes ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @return Returns the amount of additional room (in bytes) available for this record store to grow.
     */
    public int getSizeAvailableBytes() {
        try {
            return this.dataBase.getRecordStore().getSizeAvailable();
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("getSizeAvailableBytes ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            ex.printStackTrace();
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
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     *
     * @return
     */
    public boolean hasNextElement() {
        boolean existNextElement = false;
        if(resultSet != null){
            if(resultSet.length > 0){
                if(indexResultSet < resultSet.length){
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
    public Entity nextElement(){
        Entity entity = resultSet[indexResultSet];
        indexResultSet++;

        return entity;
    }

    /**
     * Buscar registros na entidade do RMS
     * @param recordFilter
     * @param recordComparator
     */
    protected void search(RecordFilter recordFilter, RecordComparator recordComparator){
        try {
            clearResultSet();

            RecordStore store = getDataBase().getRecordStore();

            if(store != null){
                reEntity = store.enumerateRecords(recordFilter, recordComparator, false);
                reEntityIndex = store.enumerateRecords(recordFilter, recordComparator, false);

                resultSet = new Entity[reEntity.numRecords()];

                int countEntity = 0;

                while(reEntity.hasNextElement()){
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
                        ex.printStackTrace();
                    } catch (InvalidRecordIDException ex) {
                        AlertRMS.setTextAlert("Search ERROR: ");
                        AlertRMS.setTextAlertException(ex.getMessage());
                        AlertRMS.show();
                        ex.printStackTrace();
                    } catch (RecordStoreNotOpenException ex) {
                        AlertRMS.setTextAlert("Search ERROR: ");
                        AlertRMS.setTextAlertException(ex.getMessage());
                        AlertRMS.show();
                        ex.printStackTrace();
                    } catch (RecordStoreException ex) {
                        AlertRMS.setTextAlert("Search ERROR: ");
                        AlertRMS.setTextAlertException(ex.getMessage());
                        AlertRMS.show();
                        ex.printStackTrace();
                    }
                }
            }
        } catch (RecordStoreNotOpenException ex) {
            AlertRMS.setTextAlert("Search ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            AlertRMS.setTextAlert("Search ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            ex.printStackTrace();
        }
    }

    /**
     * Buscar todos os registros
     */
    public void searchAll(){
        searchAll(false);
    }

    /**
     * Buscar todos os registros
     * @param comparator
     */
    public void searchAll(RecordComparator comparator){
        searchAll(false, comparator);
    }

    /**
     * Buscar todos os registros
     * @param orderByIndex - Ordenar os registros pelo índice gerado para registro
     */
    public void searchAll(boolean orderByIndex){
        searchAll(orderByIndex, null);
    }

    /**
     * Buscar todos os registros
     * @param orderByIndex - Ordenar os registros pelo índice gerado para registro
     * @param comparator
     */
    public void searchAll(boolean orderByIndex, RecordComparator comparator){
        if(orderByIndex){
            search(null, comparator);
        }else{
            search(null, comparator);
        }
    }

    /**
     * Buscar registros pela chave
     * @param userId
     */
    public void searchKey(Entity entity){
        search(new RecordFilterKey(entity), null);
    }

    /**
     * Procurar os registros não sincronizados
     */
    public void searchNotSynchronized(){
        searchNotSynchronized(-1);
    }

    /**
     * Procurar os registros não sincronizados
     * @param changeStatusAs - Seta o Status que os registros encontrados deverão ficar
     */
    public void searchNotSynchronized(int changeStatusAs){
        search(new RecordFilterNotSynchronized(), null);
        if(changeStatusAs != -1){
            synchronizedData(changeStatusAs, false);
        }
    }

    /**
     * Seta o Status para os registros não sincronizados deverão ficar
     * @param changeStatusAs
     */
    public void synchronizedData(int changeStatusAs){
        this.synchronizedData(changeStatusAs, true);
    }

    private void synchronizedData(int changeStatusAs, boolean search){
        if(search) {
            search(new RecordFilterNotSynchronized(), null);
        }else{
            resetResultSet();
        }

        while(hasNextElement()){
            Entity entity = nextElement();

            entity.setMBStatus(changeStatusAs);

            update(entity);
        }
    }

    /**
     * Insere o registro no RMS
     * @param userId
     * @return
     */
    public int insert(Entity entity){
        int id = -1;
        byte[] dados = entityToByte(entity);

        id = getDataBase().addRecord(dados, 0, dados.length);

        entity.setIndexRMS(id);

        return id;
    }

    /**
     * Insere o registro existente no RMS
     * @param userId
     * @return
     */
    public boolean update(Entity entity){
        if(delete(entity) != -1){
            if(insert(entity) != -1){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
        /*
         * byte[] dados = entityToByte(userId);

        return getDataBase().updateRecord(userId.getIndexRMS(), dados, 0, dados.length);
         */
    }

    private boolean deleteRMS(int indexRMS) {
        try {
            return getDataBase().deleteRecord(indexRMS);
        } catch (Exception ex) {
            AlertRMS.setTextAlert("Delete ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Exclui o registro existentes no RMS
     * @param userId
     * @return
     */
    public int delete(Entity entity){
        if(deleteRMS(entity.getIndexRMS())){
            return entity.getIndexRMS();
        }else{
            return -1;
        }
    }

    /**
     * Exclui os registros sincronizados existentes no RMS
     */
    public void deleteSynchronized(){
        search(new RecordFilterSynchronized(), null);
        while(hasNextElement()){
            delete(nextElement());
        }
    }

    /**
     * Exclui todos os registros desse Entidade existentes no RMS
     */
    public void deleteAll(){
        try {
            searchAll(false, null);
            while(hasNextElement()){
                deleteRMS(nextElement().getIndexRMS());
            }
        } catch (Exception ex){
            AlertRMS.setTextAlert("DeleteAll ERROR: ");
            AlertRMS.setTextAlertException(ex.getMessage());
            AlertRMS.show();
            ex.printStackTrace();
        }
    }

    /*
     * private void verifyInsert(Entity userId, int indexRMS){
     *   if(userId.getIndexRMS() == -1){
     *       userId.setIndexRMS(indexRMS);
     *       //delete(userId);
     *       //insert(userId);
     *       //updateRMS(userId);
     *       update(userId);
     *   }
     * }
     */

    protected abstract Entity byteToEntity(DataInputStream dataInputStream);
    protected abstract byte[] entityToByte(Entity entity);

    /**
     * Limpa o ResultSet para futuras buscas
     */
    public void clearResultSet(){
        reEntity = null;
        reEntityIndex = null;
        resultSet = null;
        indexResultSet = 0;
    }

    private void resetResultSet(){
        indexResultSet = 0;
    }

    private class RecordFilterKey implements RecordFilter {
        private Entity entity;

        public RecordFilterKey(Entity entity) {
            this.entity = entity;
        }

        public boolean matches(byte[] persisted) {
            return entity.getKey().equals(entity.getKey(persisted));
        }
    }

    private class RecordFilterSynchronized implements RecordFilter {
        public RecordFilterSynchronized() {}

        public boolean matches(byte[] persisted) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(persisted);
                java.io.DataInputStream dis = new java.io.DataInputStream(bis);

                Entity entity = byteToEntity(dis);

                return entity.getMBStatus() == DBStatusType.SYNCHRONIZED;
            } catch (Exception ex) {
                AlertRMS.setTextAlert("RecordFilterSinchronized ERROR: ");
                AlertRMS.setTextAlertException(ex.getMessage());
                AlertRMS.show();
                ex.printStackTrace();
                return false;
            }
        }
    }

    private class RecordFilterNotSynchronized implements RecordFilter {
        public RecordFilterNotSynchronized() {}

        public boolean matches(byte[] persisted) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(persisted);
                java.io.DataInputStream dis = new java.io.DataInputStream(bis);

                Entity entity = byteToEntity(dis);

                return entity.getMBStatus() != DBStatusType.SYNCHRONIZED;
            } catch (Exception ex) {
                AlertRMS.setTextAlert("RecordFilterNotSinchronized ERROR: ");
                AlertRMS.setTextAlertException(ex.getMessage());
                AlertRMS.show();
                ex.printStackTrace();
                return false;
            }
        }
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
