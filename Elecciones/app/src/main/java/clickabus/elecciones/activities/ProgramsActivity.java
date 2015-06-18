package clickabus.elecciones.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.clickabus.elecciones.R;
import clickabus.elecciones.fragments.PartiesFragment;
import clickabus.elecciones.fragments.ProgramsFragment;

//Activity que contiene el tabHost que es el artificio padre de las pestañas

public class ProgramsActivity extends FragmentActivity {

    // Creo esta variable con el id del child tab.
    private static final int ID_FRAGMENT_PROGRAMS = 1;

    public static FragmentTabHost tabHost;
    public static String uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_programs);
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);

        tabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("fragment_parties").setIndicator("Partidos"), PartiesFragment.class, null); //Añado las pestañas
        tabHost.addTab(tabHost.newTabSpec("fragment_programs").setIndicator("Programa"), ProgramsFragment.class, null);
        tabHost.getTabWidget().getChildTabViewAt(ID_FRAGMENT_PROGRAMS).setEnabled(false);  //Hago que no sea clickable para que no cargue un navegador vacio
    }

}

