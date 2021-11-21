package com.hcmus_csc13009.nowwakealarm.ui;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.hcmus_csc13009.nowwakealarm.utils.DayUtil;
import com.hcmus_csc13009.nowwakealarm.utils.TimePickerUtil;
import com.hcmus_csc13009.nowwakealarm.R;
import com.hcmus_csc13009.nowwakealarm.databinding.ActivityAlarmBinding;
import com.hcmus_csc13009.nowwakealarm.models.Alarm;

public class AddAlarmActivity extends AppCompatActivity {

    ActivityAlarmBinding activityAlarmBinding;
    String tone;
    Alarm alarm;
    Ringtone ringtone;
    boolean isVibrate = false;
    boolean isHard = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        activityAlarmBinding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);

        tone = RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_ALARM).toString();
        ringtone = RingtoneManager.getRingtone(this, Uri.parse(tone));
        activityAlarmBinding.setToneNameAlarm.setText(ringtone.getTitle(this));
        if (alarm != null) {
            updateAlarmInfo(alarm);
        }
        activityAlarmBinding.recurringCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activityAlarmBinding.optionsRecurring.setVisibility(View.VISIBLE);
                } else {
                    activityAlarmBinding.optionsRecurring.setVisibility(View.GONE);
                }
            }
        });


        activityAlarmBinding.alarmSoundCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Sound");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) Uri.parse(tone));
                startActivityForResult(intent, 5);
            }
        });

        activityAlarmBinding.checkBoxTryHard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isHard = true;
                } else {
                    isHard = false;
                }
            }
        });

        activityAlarmBinding.checkBoxVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isVibrate = true;
                } else {
                    isVibrate = false;
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*activityAlarmBinding.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                activityAlarmBinding.scheduleAlarmHeading
                        .setText(DayUtil.getDay(TimePickerUtil.getTimePickerHour(timePicker),
                                TimePickerUtil.getTimePickerMinute(timePicker)));
            }
        });*/


    }

    private void updateAlarmInfo(Alarm alarm) {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                if(alarm!=null) {
                    updateAlarm();
                }
                else{
                    scheduleAlarm();
                }
                startActivity(new Intent(AddAlarmActivity.this, MainActivity.class));
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    private void scheduleAlarm() {
    }

    private void updateAlarm() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddAlarmActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
