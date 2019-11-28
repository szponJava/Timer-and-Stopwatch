package com.example.timer_navigationdrawer.ui.stopwatch;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.timer_navigationdrawer.R;

import java.util.ArrayList;
import java.util.List;

public class StopWatchFragment extends Fragment {




    private TextView stopwatchHrsMinSecTextView,stopwatmSecTextView;
    private Button startPauseButton,resetButton, lapSaveButton;
    private ListView lapTimeListView;
    private List<String> listElementsArraylist;
    private ArrayAdapter<String> adapter;
    private Handler handler;

    private AlphaAnimation buttonClick = new AlphaAnimation(1F,0.7F);


    public static int LAPNUMBER;

    private long MillisecondTime, startTime, timeBuff, updateTime = 0L ;
    private int  hours, seconds, minutes, milliSeconds;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        LAPNUMBER=0;


        stopwatchHrsMinSecTextView = root.findViewById(R.id.stopwatchHrsMinSecTextView);
        stopwatmSecTextView = root.findViewById(R.id.stopwatchmSecTextView);

        startPauseButton = root.findViewById(R.id.startPauseButton);
        resetButton = root.findViewById(R.id.resetButton);
        lapSaveButton = root.findViewById(R.id.saveLapButton);

        lapTimeListView = root.findViewById(R.id.lapTimeListView);

        stopwatchHrsMinSecTextView.setText("00:00:00");
        stopwatmSecTextView.setText("000");
        lapSaveButton.setTextColor(getResources().getColor(R.color.lapSaveBtnTextColourDisabled));


        startPauseButton.setText("Start");
        lapSaveButton.setEnabled(false);

        handler = new Handler();


        // implementing ArrayList in Lap Time list view using adapter
        listElementsArraylist = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, listElementsArraylist){

            // setting up Text colour in List View
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(getResources().getColor(R.color.listViewColour));
                textView.setTypeface(Typeface.SERIF);

                return view;
            }
        };

        lapTimeListView.setAdapter(adapter);




        //implementing Start/Pause button
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);

                if(startPauseButton.getText().equals("Start")){

                    startPauseButton.setText("Pause");
                    resetButton.setEnabled(false);
                    resetButton.setTextColor(getResources().getColor(R.color.resetBtnTextColourDisabled));



                    lapSaveButton.setEnabled(true);
                    lapSaveButton.setTextColor(getResources().getColor(R.color.lapSaveBtnTextColourActive));

                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable,0);

                } else {

                    startPauseButton.setText("Start");
                    resetButton.setEnabled(true);
                    resetButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));

                    timeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);

                    lapSaveButton.setEnabled(false);
                    lapSaveButton.setTextColor(getResources().getColor(R.color.lapSaveBtnTextColourDisabled));



                }
            }
        });


        // implementing Pause button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);


                MillisecondTime = 0L ;
                startTime = 0L ;
                timeBuff = 0L ;
                updateTime = 0L ;
                seconds = 0 ;
                minutes = 0 ;
                milliSeconds = 0 ;
                LAPNUMBER=0;

                stopwatchHrsMinSecTextView.setText("00:00:00");
                stopwatmSecTextView.setText("000");

                listElementsArraylist.clear();
                adapter.notifyDataSetChanged();

            }
        });


        // implementing Lap Save button
        lapSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);

                LAPNUMBER++;
                listElementsArraylist.add( "Lap "+String.format("%02d",LAPNUMBER) + " - "+ stopwatchHrsMinSecTextView.getText() + ":" + stopwatmSecTextView.getText());

                adapter.notifyDataSetChanged();

                // scrolls to the bottom of the list after item is added
                lapTimeListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                lapTimeListView.setSelection(adapter.getCount()-1);


            }
        });

        return root;

    }



    // implementing stop watch using Runnable

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {


            MillisecondTime = SystemClock.uptimeMillis()-startTime;
            updateTime = timeBuff + MillisecondTime;
            seconds = (int) (updateTime / 1000);

            hours = seconds /60/60;

            minutes = (seconds  - hours*60*60)/60;

            seconds = seconds % 60;

            milliSeconds = (int) (updateTime % 1000);



            stopwatchHrsMinSecTextView.setText("" +
                    String.format("%02d",hours) + ":"
                    + String.format("%02d", minutes) + ":"
                    + String.format("%02d", seconds));

            stopwatmSecTextView.setText(String.format("%03d", milliSeconds));


            handler.postDelayed(this, 0);





        }
    };



}