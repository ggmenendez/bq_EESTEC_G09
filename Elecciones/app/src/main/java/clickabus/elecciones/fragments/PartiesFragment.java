package clickabus.elecciones.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clickabus.elecciones.R;
import clickabus.elecciones.activities.ProgramsActivity;

public class PartiesFragment extends Fragment {

    private static final String URL_PP_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.pp.es/sites/default/files/documentos/af_pp_programa-municipales_2015_actualizado-20.03.15.pdf";
    private static final String URL_PSOE_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.psoe.es/source-media/000000629000/000000629127.pdf";
    private static final String URL_IU_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.izquierda-unida.es/sites/default/files/doc/Programa_Marco_Electoral_Municipales_Autonomicas_IU_2015.pdf";
    private static final String URL_PODEMOS_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://podemos.info/wp-content/uploads/2015/05/prog_marco_12.pdf";
    private static final String URL_CIUDADANOS_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.elcambio.es/assets/programa.pdf";
    private static final String URL_UPYD_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.upyd.es/contenidos/ficheros/121849";
    private static final String URL_PACMA_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.pacma.es/files/programa_electoral_PACMA_autonomicas_2015.pdf";
    private static final String URL_CIU_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.ciu.cat/olot/sites/default/files/documents/programa_ciu_olot_2015.pdf";
    private static final String URL_PNV_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.eaj-pnv.eus/adjuntos/pnvDocumentos/14391_archivo.pdf";
    private static final String URL_BNG_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://bng.gal/wp-content/uploads/programa_europeas_2014.pdf";
    private static final String URL_CC_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://coalicioncanaria.org/wp-content/uploads/2015/01/programa-web-5.pdf";
    private static final String URL_COMPROMIS_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://compromispervalencia.org/files/2015/05/Programa-Electoral-Compromis-2015-Castellano.pdf";
    private static final String URL_FORO_PROGRAM = "http://www.foroasturias.es/elecciones2015/programa/";
    private static final String URL_GEROA_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://geroabai.com/upload/multimedia/pdf_120150507134827_1122_bases-programaticas-2015.pdf";
    private static final String URL_PAR_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.partidoaragones.es/elecciones2015/PAR_Programa2015web4.pdf";
    private static final String URL_CA_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.chunta.org/workspace/uploads/programa-electoral-autonomico-2015.pdf";
    private static final String URL_PRC_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://prc.es/archivosbd/noticias_galeria/58212853987470a450bab8b5161e68d8.pdf";
    private static final String URL_VOX_PROGRAM = "http://www.voxespana.es/nuestro-programa-marco-autonomico/";
    private static final String URL_ERC_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://locals.esquerra.cat/documents/eleccions-municipals-2015-erc-arenys-de-munt.pdf";
    private static final String URL_BILDU_PROGRAM = "http://ehbildu.eus/es/elecciones/elecciones-forales-y-municipales-2015/programas";
    private static final String URL_UPN_PROGRAM = "https://docs.google.com/gview?embedded=true&url=http://www.parlamentodenavarra.es/UserFiles/files/UPN_programa_electoral_2011.pdf";
    private static final String URL_PR_PROGRAM = "https://files.acrobat.com/a/preview/462ea9e5-e632-433f-b807-1845e0a0ede4";

    // Creo esta variable con el id del child tab.
    private static final int ID_FRAGMENT_PROGRAMS = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }
    //Se ejecuta cuando se abre la pestaña
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView pp =(TextView) view.findViewById(R.id.textViewPP);
        pp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS); //Se cambia a la otra pestaña y se carga el enlace
                ProgramsActivity.uri = URL_PP_PROGRAM;

            }
        });

        TextView psoe =(TextView) view.findViewById(R.id.textViewPSOE);
        psoe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_PSOE_PROGRAM;

            }
        });
        TextView iu =(TextView) view.findViewById(R.id.textViewIU);
        iu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_IU_PROGRAM;

            }
        });
        TextView podemos =(TextView) view.findViewById(R.id.textViewPodemos);
        podemos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_PODEMOS_PROGRAM;

            }
        });
        TextView ciudadanos =(TextView) view.findViewById(R.id.textViewCiudadanos);
        ciudadanos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_CIUDADANOS_PROGRAM;

            }
        });
        TextView upyd =(TextView) view.findViewById(R.id.textViewUPyD);
        upyd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_UPYD_PROGRAM;

            }
        });
        TextView pacma =(TextView) view.findViewById(R.id.textViewPACMA);
        pacma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_PACMA_PROGRAM;

            }
        });
        TextView ciu =(TextView) view.findViewById(R.id.textViewCiU);
        ciu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_CIU_PROGRAM;

            }
        });
        TextView pnv =(TextView) view.findViewById(R.id.textViewPNV);
        pnv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_PNV_PROGRAM;

            }
        });
        TextView bng =(TextView) view.findViewById(R.id.textViewBNG);
        bng.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_BNG_PROGRAM;

            }
        });
        TextView cc =(TextView) view.findViewById(R.id.textViewCC);
        cc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_CC_PROGRAM;

            }
        });
        TextView compromis =(TextView) view.findViewById(R.id.textViewCompromís);
        compromis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_COMPROMIS_PROGRAM;

            }
        });
        TextView foro =(TextView) view.findViewById(R.id.textViewFAC);
        foro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_FORO_PROGRAM;

            }
        });
        TextView geroa =(TextView) view.findViewById(R.id.textViewGBAI);
        geroa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_GEROA_PROGRAM;

            }
        });
        TextView par =(TextView) view.findViewById(R.id.textViewPAR);
        par.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_PAR_PROGRAM;

            }
        });
        TextView ch =(TextView) view.findViewById(R.id.textViewCA);
        ch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_CA_PROGRAM;

            }
        });
        TextView prc =(TextView) view.findViewById(R.id.textViewPRC);
        prc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_PRC_PROGRAM;

            }
        });
        TextView vox =(TextView) view.findViewById(R.id.textViewVOX);
        vox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_VOX_PROGRAM;

            }
        });
        TextView erc =(TextView) view.findViewById(R.id.textViewERC);
        erc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_ERC_PROGRAM;

            }
        });
        TextView bildu =(TextView) view.findViewById(R.id.textViewBildu);
        bildu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_BILDU_PROGRAM;

            }
        });
        TextView upn =(TextView) view.findViewById(R.id.textViewUPN);
        upn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_UPN_PROGRAM;

            }
        });
        TextView pr =(TextView) view.findViewById(R.id.textViewPR);
        pr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgramsActivity.tabHost.setCurrentTab(ID_FRAGMENT_PROGRAMS);
                ProgramsActivity.uri = URL_PR_PROGRAM;

            }
        });
    }



    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_parties, container, false);

    }

}