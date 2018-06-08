package org.mindroid.android.app.fragments.admin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.mindroid.android.app.R;
import org.mindroid.android.app.robodancer.SettingsProvider;

import javax.xml.datatype.Duration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    private final String PASSWORD = "133742";

    private EditText txt_password_input;
    private Button btn_enter;
    private TextView txt_password;

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance() {
        AdminFragment fragment = new AdminFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        txt_password_input = (EditText) view.findViewById(R.id.txt_password_input);
        btn_enter = (Button) view.findViewById(R.id.btn_pass_enter);
        txt_password = (TextView) view.findViewById(R.id.txt_password);

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_password_input.getText().toString().equals(PASSWORD)){
                    SettingsProvider.getInstance().setAdminModeUnlocked(true);
                    Toast.makeText(getContext(), "Admin Mode Unlocked =)", Toast.LENGTH_SHORT).show();
                }else{
                    SettingsProvider.getInstance().setAdminModeUnlocked(false);
                    Toast.makeText(getContext(), "Wrong Password =(", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}