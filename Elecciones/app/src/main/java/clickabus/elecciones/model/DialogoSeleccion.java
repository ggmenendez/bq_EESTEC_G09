package clickabus.elecciones.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import clickabus.elecciones.activities.ResultActivity;
import clickabus.elecciones.activities.StatisticsActivity;

public class DialogoSeleccion extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String[] items = {"Twitter", "Facebook", "WhatsApp"};

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        //String value = "";
        builder.setTitle("Selección")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Log.i("Dialogos", "Opción elegida: " + items[item]);
                        String value = items[item];
                        if(StatisticsActivity.FLAG==1){
                            StatisticsActivity statisticsActivity = (StatisticsActivity)getActivity();
                            statisticsActivity.seleccionado(value);
                        }else{
                            ResultActivity callingActivity = (ResultActivity) getActivity();
                            callingActivity.seleccionado(value);
                        }


                    }
                });


        return builder.create();
    }




}
