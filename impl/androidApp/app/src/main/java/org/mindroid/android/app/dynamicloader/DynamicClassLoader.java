package org.mindroid.android.app.dynamicloader;

import android.content.Context;
import dalvik.system.DexClassLoader;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.android.AndroidClassLoadingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import org.apache.commons.lang3.SerializationUtils;
import org.mindroid.android.app.programs.dynamic.Wrapper;
import org.mindroid.api.ImperativeAPI;
import org.mindroid.api.ImperativeWorkshopAPI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DynamicClassLoader {

    Context cntxt;
    public DynamicClassLoader(Context context){
        this.cntxt = context;
     }

    public void loadClass(ImperativeAPI api){
        //http://bytebuddy.net/javadoc/1.8.0/net/bytebuddy/android/AndroidClassLoadingStrategy.html

        /*
        Finally, we can use reflection to inject a type into an existent ClassLoader. Usually, a class loader is asked to provide a given type by its name.
        Using reflection, we can turn this principle around and call a protected method to inject a new class into the class loader without the class loader
         actually knowing how to locate this dynamic class.
         */
        AndroidClassLoadingStrategy acls = new AndroidClassLoadingStrategy.Injecting(getTemporaryClassDir());

        //TODO what happens with the api parameter?
        ByteBuddy bb = new ByteBuddy();
        Class<? extends ImperativeWorkshopAPI> loadedClass = bb.subclass(ImperativeWorkshopAPI.class)
                .make()
                .load(Wrapper.class.getClassLoader(),acls)
                .getLoaded();

        //Execute class
        loadedClass.cast(ImperativeWorkshopAPI.class).run();


    }

    public File getTemporaryClassDir(){
        return cntxt.getCodeCacheDir();
        //cntxt.getDir("TemporalClassDir",Context.MODE_PRIVATE);
    }



    /*
    final Class<? extends SomeClass> dynamicType = new ByteBuddy(ClassFileVersion.JAVA_V8)                    .subclass(com.pspdfkit.ui.PdfActivity.class, IMITATE_SUPER_CLASS)
    .name("com.research.Dummy")
    .method(ElementMatchers.named("toString"))
    .intercept(FixedValue.value("Hello World!"))
    .make()
    .load(getClass().getClassLoader(), new AndroidClassLoadingStrategy.Wrapping(this.getDir("dexgen", Context.MODE_PRIVATE))).getLoaded();

    final Intent intent = new Intent(this, dynamicType);
    startActivity(intent);
     */
}
