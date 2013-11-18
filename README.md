Project in Java ME for Reading and Writing RMS.
===========================================================================================================
Each MIDP-compliant device maintains a dedicated area of device memory for persistent application data storage. MIDlet data stored here persists across multiple invocations of the applications that use it. Both the physical location and the size of the data store are device dependent.
The RMS API abstracts the device-dependent details of the storage area and access to those details, and it provides a uniform mechanism to create, destroy, and modify data. This ensures portability of MIDlets to different devices.

RMS Data Storage Model

The RMS supports the creation and management of multiple record stores, shown in Figure below. A record store is a database whose central abstraction is the record. Each record store consists of zero or more records. A record store name is case sensitive and can consist of a maximum of 32 Unicode characters. A record store is created by a MIDlet.

MIDlets within the same MIDlet suite can share one another's record stores. A MIDlet suite defines a name space for record stores; a record store must have a unique name within a MIDlet suite. The same name can be used in different MIDlet suites, however.

MIDlets can list the names of all the record stores available to them. They can also determine the amount of free space available for storing data. Incidentally, you should be aware that when all MIDlets in a MIDlet suite are removed from a device, the device AMS removes all record stores in the MIDlet suite namespace. All persistent data will be lost. For this reason, you should consider designing applications to include a warning or confirmation that requires users to acknowledge that they understand the potential loss of data when they remove applications. Applications might also include a mechanism to back up the records in a data store to another location. This might require server side support.

The RMS defines the following conceptual operations on an individual record store:

• Add a record. 
• Delete a record. 
• Change a record. 
• Look up (retrieve) a record. 
• Enumerate all records.

Records are uniquely identified by a record ID, which is the only primary key type supported. The type of all record ids is the Java built-in type int. The RMS has no support for features—such as tables, rows, columns, data types, and so forth—that are present in relational databases. 
  
Records 
A record is a byte array of type byte []. The RMS doesn't support the definition or formatting of fields within a record. Your application must define the data elements within a record and their format.

The reader of a record, therefore, must be aware of the format that was used to write the record. Because a record is simply a byte array, applications must convert data from arbitrary types to bytes when writing records, and they must convert from bytes to those types upon reading the data. 
 

Here is a sample code that shows simple read and write of RMS in J2ME:

import com.j2me.rms.Entity;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.kxml2.io.KXmlParser;

//Record Store Class

/**
 *
 * @author Willian
 */
public class Config extends Entity {

    public final static String ENTITY_NAME = "Config";

    private int param;
    private String value;

    public Config() {
        super();
    }

    public Config(int param) {
        super(new Integer(param));
        this.param = param;
    }

    public Config(int param, String value) {
        super(new Integer(param));
        this.param = param;
        this.value = value;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;

        this.setKey(new Integer(param));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getKey(byte[] bytes) {
        Integer keyRMS = null;
        try {
            ByteArrayInputStream bis
                    = new ByteArrayInputStream(bytes);
            DataInputStream dis
                    = new DataInputStream(bis);

            keyRMS = new Integer(dis.readInt());

            bis.close();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return keyRMS;
    }

    public String toStringXML() {
        return xml;
    }

    public void parse(KXmlParser kxp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }
}

//Record Store Controller Class

import com.j2me.rms.DAO;
import com.j2me.rms.Entity;
import com.j2me.utils.entity.Config;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Willian
 */
public class ConfigDao extends DAO {

    public ConfigDao() {
        super(Config.ENTITY_NAME);
    }

    public Entity byteToEntity(DataInputStream dataInputStream) {
        Config config = null;
        try {
            config = new Config();
            config.setKey(new Integer(dataInputStream.readInt()));
            config.setParam(dataInputStream.readInt());
            config.setValue(dataInputStream.readUTF());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return config;
    }

    public void entityToByte(DataOutputStream data, Entity entity) {
        try {
            Config config = (Config) entity;

            data.writeInt(Integer.parseInt(String.valueOf(config.getKey())));
            data.writeInt(config.getParam());
            data.writeUTF(config.getValue());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

//How to use the persistence API RMS

ConfigDao configDao = new ConfigDao();
configDao.searchKey(new Config(param, value));

if (configDao.hasNextElement()) {
	Config configRecord = (Config) configDao.nextElement();

	configRecord.setValue(value);

	configDao.update(configRecord);
} else {
	configDao.insert(new Config(param, value));
}