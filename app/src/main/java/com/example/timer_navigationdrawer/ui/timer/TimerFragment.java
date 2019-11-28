package com.example.timer_navigationdrawer.ui.timer;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.timer_navigationdrawer.R;

import java.lang.reflect.Field;

public class TimerFragment extends Fragment {


    private TextView timerTextView,hoursTextTextView,minutesTextTextView,secondsTextTextView;
    private CountDownTimer countDownTime;
    private Button startPauseButton,resetButton;
    private NumberPicker hoursNumberPicker, minutesNumberPicker,secondsNumberPicker;
    private static MediaPlayer mediaPlayer;


    private AlphaAnimation buttonClick = new AlphaAnimation(1F,0.7F);



    private int minutes=0;
    private int seconds=0;
    private int hours=0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_timer, container, false);


        timerTextView = root.findViewById(R.id.timerTextView);
        timerTextView.setText("00:00:00");
        startPauseButton = root.findViewById(R.id.StartPausButton);
        resetButton = root.findViewById(R.id.resetButton);
        hoursNumberPicker = root.findViewById(R.id.hoursNumberPicker);
        minutesNumberPicker = root.findViewById(R.id.minutesNumberPicker);
        secondsNumberPicker = root.findViewById(R.id.secondsNumberPicker);


        hoursTextTextView = root.findViewById(R.id.hoursTextTextView);
        minutesTextTextView = root.findViewById(R.id.minutesTextTextView);
        secondsTextTextView = root.findViewById(R.id.secondsTextTextView);



        //sets up picker Text Colour
        setNumberPickerTextColor(hoursNumberPicker,getResources().getColor(R.color.numberPickerColActive));
        setNumberPickerTextColor(minutesNumberPicker,getResources().getColor(R.color.numberPickerColActive));
        setNumberPickerTextColor(secondsNumberPicker,getResources().getColor(R.color.numberPickerColActive));



        // sets dividerColour invisable
        changeDividerColor(hoursNumberPicker,getResources().getColor(R.color.numberPicker_dividerColour));
        changeDividerColor(minutesNumberPicker,getResources().getColor(R.color.numberPicker_dividerColour));
        changeDividerColor(secondsNumberPicker,getResources().getColor(R.color.numberPicker_dividerColour));


        hoursNumberPicker.setMaxValue(99);
        minutesNumberPicker.setMaxValue(59);
        secondsNumberPicker.setMaxValue(59);

        resetButton.setEnabled(false);
        resetButton.setTextColor(getResources().getColor(R.color.resetBtnTextColourDisabled));

        startPauseButton.setEnabled(false);
        startPauseButton.setTextColor(getResources().getColor(R.color.resetBtnTextColourDisabled));


        hoursNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

                hours = view.getValue();

                timeUpdate(hours,minutes,seconds);
                resetButton.setEnabled(true);
                resetButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));

                startPauseButton.setEnabled(true);
                startPauseButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));


            }
        });

        minutesNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

                minutes = view.getValue();

                timeUpdate(hours,minutes,seconds);
                resetButton.setEnabled(true);
                resetButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));

                startPauseButton.setEnabled(true);
                startPauseButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));

            }
        });

        secondsNumberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

                seconds = view.getValue();

                timeUpdate(hours,minutes,seconds);
                resetButton.setEnabled(true);
                resetButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));

                startPauseButton.setEnabled(true);
                startPauseButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));

            }
        });




        // implementing reset Button

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);

                timerTextView.setText("00:00:00");
                hoursNumberPicker.setValue(00);
                minutesNumberPicker.setValue(00);
                secondsNumberPicker.setValue(00);

                hours = 0;
                minutes = 0;
                seconds = 0;

                hoursNumberPicker.setEnabled(true);
                minutesNumberPicker.setEnabled(true);
                secondsNumberPicker.setEnabled(true);


               startPauseButton.setEnabled(false);
                startPauseButton.setTextColor(getResources().getColor(R.color.resetBtnTextColourDisabled));

                if(mediaPlayer!=null){

                    mediaPlayer.stop();
                }



                resetButton.setEnabled(false);
                resetButton.setTextColor(getResources().getColor(R.color.resetBtnTextColourDisabled));



            }
        });



        // implementing start/Pause Button

        startPauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                v.startAnimation(buttonClick);


                if (startPauseButton.getText().equals("Start")) {


                    if(hours!=0 || minutes!=0 || seconds!=0){

                        resetButton.setTextColor(getResources().getColor(R.color.resetBtnTextColourDisabled));
                    }else {
                        resetButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));
                        startPauseButton.setEnabled(false);

                    }


                    startPauseButton.setText("Pause");
                    resetButton.setEnabled(false);
                    hoursNumberPicker.setEnabled(false);
                    minutesNumberPicker.setEnabled(false);
                    secondsNumberPicker.setEnabled(false);


                    setNumberPickerTextColor(hoursNumberPicker,getResources().getColor(R.color.numberPickerColDisable));
                    setNumberPickerTextColor(minutesNumberPicker,getResources().getColor(R.color.numberPickerColDisable));
                    setNumberPickerTextColor(secondsNumberPicker,getResources().getColor(R.color.numberPickerColDisable));





                    long newTime = ((hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000));

                    countDownTime = new CountDownTimer(newTime+100, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                            hours = (int) ((millisUntilFinished / 1000) / 60) / 60;
                            minutes = (int) ((millisUntilFinished / 1000) / 60) - (hours*60);
                            seconds = ((int) (millisUntilFinished / 1000) - (minutes * 60) - (hours*60*60));

                            timeUpdate(hours, minutes, seconds);

                        }

                        @Override
                        public void onFinish() {
                            startPauseButton.setText("Start");


                            hoursNumberPicker.setEnabled(true);
                            minutesNumberPicker.setEnabled(true);
                            secondsNumberPicker.setEnabled(true);


                            setNumberPickerTextColor(hoursNumberPicker,getResources().getColor(R.color.numberPickerColActive));
                            setNumberPickerTextColor(minutesNumberPicker,getResources().getColor(R.color.numberPickerColActive));
                            setNumberPickerTextColor(secondsNumberPicker,getResources().getColor(R.color.numberPickerColActive));

                            startPauseButton.setEnabled(false);
                            startPauseButton.setTextColor(getResources().getColor(R.color.resetBtnTextColourDisabled));


                            resetButton.setEnabled(true);
                            resetButton.setTextColor(getResources().getColor(R.color.stopPauseResetBtnTextColour));


                            mediaPlayer = MediaPlayer.create(getContext(),R.raw.alarm);
                            mediaPlayer.start();


                        }
                    }.start();

                } else {

                    countDownTime.cancel();
                    startPauseButton.setText("Start");
                    resetButton.setEnabled(true);
                    resetButton.setTextColor(Color.parseColor("#3034C9"));


                    setNumberPickerTextColor(hoursNumberPicker,getResources().getColor(R.color.numberPickerColActive));
                    setNumberPickerTextColor(minutesNumberPicker,getResources().getColor(R.color.numberPickerColActive));
                    setNumberPickerTextColor(secondsNumberPicker,getResources().getColor(R.color.numberPickerColActive));


                    hoursNumberPicker.setEnabled(true);
                    minutesNumberPicker.setEnabled(true);
                    secondsNumberPicker.setEnabled(true);


                }

            }
        });

        return root;
    }




    public void timeUpdate(int hours, int minutes, int seconds){


        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;


        String stringHour="";
        String stringMinutes="";
        String stringSeconds="";


        // if's statements to ensure correct time display when hours /minutes/seconds are less than 9
        if(hours<=9){

            stringHour = "0"+hours;
        } else {

            stringHour = String.valueOf(hours);

        }

        if(minutes<=9){

            stringMinutes = "0" + minutes;
        } else {

            stringMinutes = String.valueOf(minutes);
        }

        if(seconds<=9){

            stringSeconds = "0" + seconds;
        }else {

            stringSeconds = String.valueOf(seconds);

        }

        timerTextView.setText(stringHour+":"+stringMinutes +":"+stringSeconds);

    }



    //--------------------- NumberPicker styling methods---------------------------


    public static void setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {

        try{
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
        }
        catch(NoSuchFieldException e){
            Log.w("setNumberPickerTextColor", e);
        }
        catch(IllegalAccessException e){
            Log.w("setNumberPickerTextColor", e);
        }
        catch(IllegalArgumentException e){
            Log.w("setNumberPickerTextColor", e);
        }

        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText)
                ((EditText)child).setTextColor(color);
        }
        numberPicker.invalidate();
    }

    private void changeDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


}