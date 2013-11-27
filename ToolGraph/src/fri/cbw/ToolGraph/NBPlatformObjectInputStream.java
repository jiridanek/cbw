/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fri.cbw.ToolGraph;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import org.openide.util.Lookup;

/**
 * Code snipet form http://www.pellissier.co.za/hermien/?p=179
 * 
 */
public class NBPlatformObjectInputStream extends java.io.ObjectInputStream {

    public NBPlatformObjectInputStream(InputStream TheStream)
            throws IOException, StreamCorruptedException {
        super(TheStream);
    }

    @Override
    protected Class resolveClass(ObjectStreamClass Osc) throws IOException, ClassNotFoundException {
        Class theClass = null;

        ClassLoader syscl = Lookup.getDefault().lookup(ClassLoader.class);
        theClass = Class.forName(Osc.getName(), true, syscl);

        return theClass;
    }
}
