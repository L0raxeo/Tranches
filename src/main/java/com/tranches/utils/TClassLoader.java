package com.tranches.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
public class TClassLoader extends ClassLoader {
    @Override
    public Class<?> findClass(String name) {
        byte[] bt = loadClassData(name);
        return defineClass(name, bt, 0, bt.length);
    }
    private byte[] loadClassData(String className) {
        //read class
        InputStream is = getClass().getClassLoader().getResourceAsStream(className.replace(".", "/")+".class");
        ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
        //write into byte
        int len =0;
        try {
            while(true){
                assert is != null;
                if ((len = is.read()) == -1) break;
                byteSt.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //convert into byte array
        return byteSt.toByteArray();
    }

    //        try
//        {
//            URL url = new File("assets/plugins/TestDemoPluginTranches.jar").toURI().toURL();
//            URLClassLoader ucl = new URLClassLoader(new URL[]{url});
//            Class<?> c = ucl.loadClass("com.tranches.plugin.TestClazz");
//            Object obj = c.getDeclaredConstructor().newInstance();
//            Method method = c.getMethod("getShowMethodObj");
//            //PluginInterface pi = (PluginInterface) method.invoke(obj);
//            System.out.println(method.invoke(obj));
//        }
//        catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ignored)
//        {
//
//        } catch (MalformedURLException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

}