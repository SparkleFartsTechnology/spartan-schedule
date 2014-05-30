package com.SpartanTech.schedule;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;


import java.util.Calendar;


public class TimePickerSetting extends DialogPreference{
   private TimePicker picker = null;
   private int lastHour=0;
   private int lastMinute=0;
   private AlarmManager alarmMgr;
   private PendingIntent alarmIntent;


    @Override
    public Context getContext() {
        return super.getContext();
    }

    public static int getHour(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }



    public TimePickerSetting(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText("set");
        setNegativeButtonText("Cancel");
    }


    @Override
    protected View onCreateDialogView(){
        picker = new TimePicker(getContext());

        return(picker);
    }


    @Override
    protected void onBindDialogView(View v){
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult){
        super.onDialogClosed(positiveResult);

        if (positiveResult){
            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();
            String time=String.valueOf(lastHour) + ":" +String.valueOf(lastMinute);

            alarmMgr = (AlarmManager) getContext()
                    .getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, lastHour);
            calendar.set(Calendar.MINUTE, lastMinute);

            if (alarmMgr != null) {
                alarmMgr.cancel(alarmIntent);
            }

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                    alarmIntent);


            if (callChangeListener(time)){
                persistString(time);
            }

        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue){
        String time=null;

        if(restoreValue){
            if (defaultValue==null){
                time = getPersistedString("00:00");
            }else{
                time=getPersistedString(defaultValue.toString());

            }
        }else{
            time=defaultValue.toString();
        }

        lastHour=getHour(time);
        lastMinute=getMinute(time);
    }

}
