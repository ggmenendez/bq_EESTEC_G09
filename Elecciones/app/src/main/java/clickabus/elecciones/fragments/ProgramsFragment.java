package clickabus.elecciones.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.clickabus.elecciones.R;
import clickabus.elecciones.activities.ProgramsActivity;

public class ProgramsFragment extends Fragment {

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView myWebView = (WebView) view.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(ProgramsActivity.uri);
        ProgramsActivity.uri = null;
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_programs, container, false);
    }

}