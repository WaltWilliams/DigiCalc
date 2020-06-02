package com.woodlandcoders.digicalc;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTab extends Fragment {
    public HomeTab() {

    }

    private TextView instruct;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_tab, container, false);
        instruct = view.findViewById(R.id.instructions);
        instruct.setText(Html.fromHtml(getString(R.string.home)));
        instruct.setShowSoftInputOnFocus(false);
        return view;
    }
}
