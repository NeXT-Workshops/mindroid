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

    private OnAdminChanged adminChangedListener;

    private final String PASSWORD = "133742";

    private EditText txt_password_input;
    private Button btn_login_logout;
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
        btn_login_logout = (Button) view.findViewById(R.id.btn_pass_enter);
        if(SettingsProvider.getInstance().isAdminModeUnlocked()){
            btn_login_logout.setText(R.string.btn_txt_logout);
        }else{
            btn_login_logout.setText(R.string.btn_txt_login);
        }


        txt_password = (TextView) view.findViewById(R.id.txt_password);

        txt_password_input.requestFocus();

        btn_login_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsProvider sp = SettingsProvider.getInstance();
                if(sp.isAdminModeUnlocked()) {
                    sp.setAdminModeUnlocked(false);
                    btn_login_logout.setText(R.string.btn_txt_login);
                    adminChangedListener.onAdminChanged(true);
                } else {
                    if (txt_password_input.getText().toString().equals(PASSWORD)) {
                        SettingsProvider.getInstance().setAdminModeUnlocked(true);
                        btn_login_logout.setText(R.string.btn_txt_logout);
                        Toast.makeText(getContext(), "Admin Mode Unlocked =)", Toast.LENGTH_SHORT).show();
                        adminChangedListener.onAdminChanged(true);
                    } else {
                        SettingsProvider.getInstance().setAdminModeUnlocked(false);
                        Toast.makeText(getContext(), "Wrong Password =(", Toast.LENGTH_SHORT).show();
                    }
                }
                txt_password_input.setText("");
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

        if (context instanceof OnAdminChanged) {
            adminChangedListener = (OnAdminChanged) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    public interface OnAdminChanged {
        public void onAdminChanged(boolean AdminChanged);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}