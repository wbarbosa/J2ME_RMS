/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.rms.filter;

import com.j2me.rms.Entity;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Willian
 */
public class RecordFilterKey implements RecordFilter {

    private final Entity entity;

    public RecordFilterKey(Entity entity) {
        this.entity = entity;
    }

    public boolean matches(byte[] persisted) {
        return entity.getKey().equals(entity.getKey(persisted));
    }
}