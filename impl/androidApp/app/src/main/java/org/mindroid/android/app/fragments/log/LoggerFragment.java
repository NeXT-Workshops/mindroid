package org.mindroid.android.app.fragments.log;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import org.mindroid.android.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.LogRecord;

/**
 *
 * Use the {@link LoggerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoggerFragment extends Fragment {

    private LinearLayout linLayout_logContainer;

    private ArrayList<LogRecord> logs = new ArrayList<LogRecord>();
    private final int WIDTH_DATE = 270;
    private final int WIDTH_SOURCE = 450;
    private final int WIDT_MSG = 1000;

    private Button btnSaveLog;
    private Button btnDeleteLog;

    public LoggerFragment() {
        // Required empty public constructor

    }

    private String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoggerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoggerFragment newInstance() {
        LoggerFragment fragment = new LoggerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_log, container, false);

        linLayout_logContainer = (LinearLayout) view.findViewById(R.id.linLayout_logContainer);
        btnSaveLog = (Button) view.findViewById(R.id.btn_saveLog);
        btnSaveLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GlobalLogger.getInstance().saveLog()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.txt_log_saved), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeleteLog = (Button) view.findViewById(R.id.btn_deleteLog);
        btnDeleteLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GlobalLogger.getInstance().deleteLog()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.txt_log_deleted), Toast.LENGTH_SHORT).show();
                }
            }
        });

        createHeadline();

        //Fetch Logs from GlobalLogger and display
        for (LogRecord log : GlobalLogger.logs) {
            createEntry(log);
        }


        return view;
    }

    private void createHeadline(){
        createLogEntry("Date", "Source","Message");
    }

    /**
     * Creates an Entry of the logs at the UI
     */
    private void createEntry(LogRecord log) {
        createLogEntry(calcDate(log.getMillis()),cutLoggerName(log.getLoggerName()),log.getMessage());
    }

    private String cutLoggerName(String loggerName){
        return loggerName.substring(loggerName.lastIndexOf(".")+1);
    }

    /**
     * Creates a log entry on ui
     * @param date - date
     * @param source  - source
     * @param message - msg
     */
    private void createLogEntry(String date, String source, String message){
        final LinearLayout linLayout_entry = new LinearLayout(getContext());
        linLayout_entry.setOrientation(LinearLayout.HORIZONTAL);

        final TextView txtView_date = new TextView(getContext());
        txtView_date.setWidth(WIDTH_DATE);
        final TextView txtView_logSource = new TextView(getContext());
        txtView_logSource.setWidth(WIDTH_SOURCE);
        final TextView txtView_logMsg = new TextView(getContext());
        txtView_logMsg.setWidth(WIDT_MSG);

        txtView_logSource.setText(source);
        txtView_logMsg.setText(message);
        txtView_date.setText(date);

        linLayout_entry.addView(txtView_date);
        linLayout_entry.addView(txtView_logSource);
        linLayout_entry.addView(txtView_logMsg);

        Runnable updateUI = new Runnable() {
            public void run() {
                linLayout_logContainer.addView(linLayout_entry);
            }
        };
        getActivity().runOnUiThread(updateUI);
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
