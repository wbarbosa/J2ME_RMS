/*
 * To change this template, choose Tools | Template(s)
 * and open the template in the editor.
 */
package com.j2me.rms.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 *
 * @author willian
 */
public class StatusRMS {

    private Command cmdVoltar;
    private Form frmStatusRMS;
    private CommandListener commandListener;
    private StringItem[] arrayStringItem;
    private int indexStringItem = 1;
    private int bytesTotalDisponivel = 0;
    private int bytesTotalUsado = 0;
    private int versionRMS = 0;

    /**
     * Returns an initiliazed instance of frmStatusRMS component.
     * @return the initialized component instance
     */
    public Form getFrmStatusRMS() {
        if (frmStatusRMS == null) {
            frmStatusRMS = new Form("Banco de Dados");

            frmStatusRMS.addCommand(getCmdVoltar());
            frmStatusRMS.setCommandListener(this.commandListener);
        }

        frmStatusRMS.deleteAll();

        calcularKBytesTotal();

        for (int x = 0, y = arrayStringItem.length; x < y; x++) {
            if (arrayStringItem[x] != null) {
                if (x > 0) {
                    frmStatusRMS.append(new Spacer(frmStatusRMS.getWidth(), 1));
                }
                frmStatusRMS.append(arrayStringItem[x]);
            }
        }

        return frmStatusRMS;
    }

    /**
     * Returns an initiliazed instance of cmdCancelar component.
     * @return the initialized component instance
     */
    public Command getCmdVoltar() {
        if (cmdVoltar == null) {
            // write pre-init user code here
            cmdVoltar = new Command("Voltar", Command.BACK, 0);
        // write post-init user code here
        }
        return cmdVoltar;
    }

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    private void loadStringItem(){
        if (arrayStringItem == null) {
            arrayStringItem = new StringItem[1];
        }

        if (indexStringItem >= arrayStringItem.length) {
            resizeStringItem();
        }
    }

    public void readSiseRMS(){
        loadStringItem();

        String[] names = RecordStore.listRecordStores();

        for (int i = 0;
                names != null && i < names.length;
                ++i) {
            RecordStore entityStore = null;
            try {
                entityStore = RecordStore.openRecordStore(names[i], false);
                int bytesDisponivel = entityStore.getSizeAvailable();
                int bytesUsado = entityStore.getSize();

                bytesTotalDisponivel = bytesDisponivel;
                bytesTotalUsado = bytesTotalUsado + (bytesUsado == 0 ? 1 : bytesUsado);

                if(versionRMS == 0){
                    versionRMS = entityStore.getVersion();
                }
            } catch (RecordStoreFullException ex) {
                ex.printStackTrace();
            } catch (RecordStoreNotFoundException ex) {
                ex.printStackTrace();
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }

            if(entityStore != null){
                try {
                    entityStore.closeRecordStore();
                } catch (RecordStoreNotOpenException ex) {
                    ex.printStackTrace();
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }
                entityStore = null;
            }
        }
    }

    private void resizeStringItem() {
        StringItem[] stringItemAux = arrayStringItem;

        arrayStringItem =
                new StringItem[stringItemAux.length + (stringItemAux.length / 2)];

        for (int x = 0, y = stringItemAux.length; x < y; x++) {
            arrayStringItem[x] = stringItemAux[x];
        }
    }

    private void calcularKBytesTotal() {
        StringBuffer stringSize = new StringBuffer();
        int kBytesTotalUsado =
                bytesTotalUsado / 1024 == 0 ? 1 : bytesTotalUsado / 1024;
        int kBytesTotalDisponivel = bytesTotalDisponivel / 1024;

        arrayStringItem[0] =
                new StringItem("Status do Banco de Dados ", "");

        arrayStringItem[0].setLayout(StringItem.LAYOUT_LEFT | StringItem.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_EXPAND);

        stringSize.append("\r\nEspa\u00E7o Dispon\u00EDvel: ");
        
        if(kBytesTotalDisponivel > 1024){
            int mBytesTotalDisponivel = kBytesTotalDisponivel / 1024;
            stringSize.append(mBytesTotalDisponivel);
            stringSize.append(" MByte(s)");

            int resto = (kBytesTotalDisponivel - (mBytesTotalDisponivel*1024));

            if(resto > 0){
                stringSize.append(" e ");
                stringSize.append(resto);
                stringSize.append(" KByte(s)");
            }

            stringSize.append(".");
        }else{
            if(kBytesTotalDisponivel == 0){
                stringSize.append(bytesTotalDisponivel);
                stringSize.append(" Byte(s).");
            }else{
                stringSize.append(kBytesTotalDisponivel);
                stringSize.append(" KByte(s).");
            }
        }

        stringSize.append("\n");
        stringSize.append("Espa\u00E7o Usado: ");

        if(kBytesTotalUsado > 1024){
            int mBytesTotalUsado = kBytesTotalUsado / 1024;
            stringSize.append(mBytesTotalUsado);
            stringSize.append(" MByte(s)");

            int resto = (kBytesTotalUsado - (mBytesTotalUsado*1024));

            if(resto > 0){
                stringSize.append(" e ");
                stringSize.append(resto);
                stringSize.append(" KByte(s)");
            }

            stringSize.append(".");
        }else{
            if(kBytesTotalUsado == 0){
                stringSize.append(bytesTotalUsado);
                stringSize.append(" Byte(s).");
            }else{
                stringSize.append(kBytesTotalUsado);
                stringSize.append(" KByte(s)");

                int resto = (bytesTotalUsado - (kBytesTotalUsado*1024));

                if(resto > 0){
                    stringSize.append(" e ");
                    stringSize.append(resto);
                    stringSize.append(" Byte(s)");
                }

                stringSize.append(".");
            }
        }

        /*
        if(versionRMS > 0){
            stringSize.append("\r\n");
            stringSize.append("Vers\u00E3o: ");
            stringSize.append(versionRMS);
        }*/

        arrayStringItem[0].setText(stringSize.toString());
    }
}