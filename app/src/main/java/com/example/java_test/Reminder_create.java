package com.example.java_test;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

public class Reminder_create extends AppCompatActivity {
    public static Date today = new Date();;
    private boolean useDate = true;
    private Chip chip1;
    private Chip chip2;
    public static Date finDate;
    public static File Reminders;

    private boolean called = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_create);

        Reminders = new File(this.getFilesDir(), "Reminders;;;");
        Spinner spinner = findViewById(R.id.RemCalDropdown);
        String[] DaysOW = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, DaysOW);
        spinner.setAdapter(adapter);

        EditText edt = findViewById(R.id.RemCalText);
        edt.setText((CharSequence) String.valueOf(0));
        //EditText e1 = (EditText) findViewById(R.id.AssCalDayT);
        //e1.setText((CharSequence) String.valueOf(today.getMonth()));
        //e1.setText((CharSequence) String.valueOf(12));

        TreeMap<String, remsubject> temp = reminder_manager.sublist;
        ChipGroup subChip = findViewById(R.id.reminderClass);
        ChipGroup remChip = findViewById(R.id.reminderRem);
        for (remsubject i : temp.values()) {
            Chip chip = new Chip(this);
            chip.setText(i.name);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("works", "onClick: ");
                    findViewById(R.id.RemError).setVisibility(View.INVISIBLE);
                    Log.d("sux", String.valueOf(view.getId())+" "+subChip.getCheckedChipId());
                    chip1 = (Chip)view;
                    ReplaceColor(subChip, view);
                    remChip.removeAllViews();
                    LinkedList<String> temp = i.options;
                    for (String j : temp) {
                        Chip chips = new Chip(chip.getContext());
                        chips.setText(j);
                        chips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                findViewById(R.id.RemError).setVisibility(View.INVISIBLE);
                                Chip t1 = (Chip) view;
                                ReplaceColor(remChip, view);
                            }
                        });
                        remChip.addView(chips);
                    }
                    Log.d("works2", "onClick: ");
                }
            });
            subChip.addView(chip);
        }


        spinner.setSelection(today.getDay());

        EditText editTextm3 = (EditText) findViewById(R.id.RemCalYearT);
        editTextm3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                MonthTimeChange(called);
            }
        });EditText editTextm2 = (EditText) findViewById(R.id.RemCalMonthT);
        editTextm2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                MonthTimeChange(called);
            }
        });
        EditText editTextm1 = (EditText) findViewById(R.id.RemCalDayT);
        editTextm1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                MonthTimeChange(called);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WeekTimeChange(i+1);}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        EditText editText = (EditText) findViewById(R.id.RemCalText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                Spinner spin = findViewById(R.id.RemCalDropdown);
                WeekTimeChange(spin.getSelectedItemPosition()+1);}
        });

        WeekTimeChange(spinner.getSelectedItemPosition());

    }

    public void ReplaceColor(ChipGroup subChip, View view){
        Chip v1 = findViewById(subChip.getCheckedChipId());
        if(v1!=null){
            v1.setChipBackgroundColorResource(R.color.white);
        }
        Chip v2 = (Chip)view;
        v2.setChipBackgroundColorResource(R.color.topBar);
        subChip.check(view.getId());
    }
    public void WeekTimeChange(int day){
        TextView temp = findViewById(R.id.RemCalText);
        if (temp.getText().toString().equals(""))
            return;
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int d1 = cal.get(Calendar.DAY_OF_WEEK);
        Log.d("try", day+" "+d1);
        Log.d("try",cal.toString());
        d1 = day-d1;
        int d2 = 0;
        try {
            if (!temp.getText().toString().equals("")) {
                d2 = Integer.parseInt(temp.getText().toString());
            }
        }
        catch (Exception e){
            return;
        }
        d1+=(7*d2);
        cal.add(Calendar.DATE, d1);
        EditText t1 = findViewById(R.id.RemCalMonthT);
        //String temp1 = String.valueOf(cal.get(Calendar.MONTH));
        String temp1 = String.valueOf(cal.get(Calendar.MONTH)+1);
        EditText t2 = findViewById(R.id.RemCalDayT);
        String temp2 = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        EditText t3 = findViewById(R.id.RemCalYearT);
        String temp3 = String.valueOf(cal.get(Calendar.YEAR));
        called = false;
        try {
            t1.setText((CharSequence) temp1);
            t2.setText((CharSequence) temp2);
            t3.setText((CharSequence) temp3);
            findViewById(R.id.AssError).setVisibility(View.INVISIBLE);
        }
        catch (Exception e){
            //Log.d("WeekChangeError", temp1+": "+temp2+": "+temp3+": ");
            System.out.println(temp1+": "+temp2+": "+temp3+": ");
        }
        called = true;
    }

    public void MonthTimeChange(boolean called){
        EditText textView = findViewById(R.id.RemCalText);
        if(called) {
            String thing = "";
            CharSequence chars = (CharSequence) thing;
            if (!textView.getText().toString().equals(""))
                textView.setText(chars);
            findViewById(R.id.RemError).setVisibility(View.INVISIBLE);
            return;
        }

    }
    public void AssCreate(View v){
        Log.d("fuk", "1 ");
        String text = "";
        boolean create = true;
        if(chip1 == null){
            text+="no class; ";
            create = false;
        }
        try {
            if(useDate) {
                DateFormat df = new SimpleDateFormat("MM-dd-yy");
                df.setLenient(false);
                TextView t1 = findViewById(R.id.RemCalMonthT);
                TextView t2 = findViewById(R.id.RemCalDayT);
                TextView t3 = findViewById(R.id.RemCalYearT);
                String dateString = t1.getText() + "-" + t2.getText() + "-" + t3.getText();
                finDate = df.parse(dateString);
            }
        }
        catch (Exception e){
            text+="invalid date";
            create = false;
        }
        if(!create){
            if(text.length()<12)
                text = text.substring(0,8);
            TextView Error = findViewById(R.id.RemError);
            Error.setText(text);
            Error.setVisibility(View.VISIBLE);
        }
        reminder newrem = null;
        if(chip1!=null&&useDate){
            TextInputEditText Add = findViewById(R.id.RemAdditionalInfo);
            ChipGroup group = findViewById(R.id.reminderRem);
            Chip temp = findViewById(group.getCheckedChipId());
            String ans = "";
            if(temp!=null) {
                ans = temp.getText().toString();
            }
            newrem = new reminder((String) chip1.getText(), ans , Add.getText().toString(), finDate);
            Log.d("fuk", "2 ");
        }
        if(chip1!=null&&!useDate) {
            TextInputEditText Add = findViewById(R.id.RemAdditionalInfo);
            ChipGroup group = findViewById(R.id.reminderRem);
            Chip temp = findViewById(group.getCheckedChipId());
            String ans = "";
            if (temp != null) {
                ans = temp.getText().toString();
            }
            newrem = new reminder((String) chip1.getText(), ans, Add.getText().toString(), null);
        }
        reminder_manager.add(newrem);
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        String str = format.format(finDate);
        String add = newrem.task+"<<<"+str+";;;";
        WritetoFile(newrem.subject, GetFromFile(newrem.subject)+add);
        Log.d("fuk", "3 ");
        remExit();
    }

    public void WritetoFile(String name, String cont){
        FileOutputStream out;
        try {
            File path = getApplicationContext().getFilesDir();
            out = new FileOutputStream(new File(path, name));
            out.write(cont.getBytes());
            out.close();

        }
        catch (Exception e){
            ////log.d("try", "no u");
        }

    }

    public String GetFromFile(String name){
        FileInputStream in;
        try {
            in = openFileInput(name);
            String temp = "";
            int add = in.read();
            while(add!=-1){
                char val = (char)add;
                temp+=val;
                add = in.read();
            }
            in.close();
            return temp;
        }
        catch (Exception e){
            //log.d("try", "no u");
            return null;
        }

    }
    public void noDate(View view){
        CheckBox check = (CheckBox)view;
        if (check.isChecked()){
            findViewById(R.id.RemCalMonthT).setEnabled(false);
            findViewById(R.id.RemCalDayT).setEnabled(false);
            findViewById(R.id.RemCalYearT).setEnabled(false);
            findViewById(R.id.RemCalText).setEnabled(false);
            findViewById(R.id.RemCalDropdown).setEnabled(false);
            useDate = false;
            findViewById(R.id.RemError).setVisibility(View.INVISIBLE);
        }
        else{
            findViewById(R.id.RemCalMonthT).setEnabled(true);
            findViewById(R.id.RemCalDayT).setEnabled(true);
            findViewById(R.id.RemCalYearT).setEnabled(true);
            findViewById(R.id.RemCalText).setEnabled(true);
            findViewById(R.id.RemCalDropdown).setEnabled(true);
            useDate = true;
        }
    }
    public void remExit(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void Switch(View v){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }


}

class reminder{
    public String subject;
    public String task;
    public Date time;
    public boolean overdue = false;
    public int urgency = 0;
    public reminder(String a, String b, String c, Date t){
        subject = a;
        time = t;
        if(b==null||b.equals("")){
            task = c;
        }
        else {
            task = b + ": " + c+": ";
        }
    }
    public String toString(){
        String str = task;
        if(time!=null){
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            str += "<<<"+format.format(time);
        }
        return str;
    }
    public reminder(String a, String b, Date t){
        subject = a;
        time = t;
        task = b;
    }
}


class remsubject implements Comparable<remsubject> {
    public String name;
    public int ind;
    LinkedList<String> options = new LinkedList<>();
    ArrayList<reminder> list  = new ArrayList<>();
    public remsubject(String a, int i){
        name = a;
        ind = i;
    }

    @Override
    public int compareTo(remsubject o) {
        return Integer.compare(ind, o.ind);
    }
}

class reminder_manager{
    public static TreeMap<String, remsubject> sublist = new TreeMap<>();
    public static ArrayList<reminder> timelist = new ArrayList<>();
    public static LinkedList<reminder> nonTimeList = new LinkedList<>();
    public static boolean add(reminder a){
        Log.d("shit", "add: ");
        Date temp = a.time;
        Calendar cal = Calendar.getInstance();
        cal.setTime(temp);
        cal.add(cal.DATE, 1);
        if(Reminder_create.today.compareTo(cal.getTime())>0){
            Log.d("shit", "add2 ");
            return false;
        }
        Log.d("shit", Reminder_create.today.toString()+";;"+cal.getTime().toString());
        sublist.get(a.subject).list.add(a);
        return true;
        /*if(a.time!= null) {
            int beg = -1;
            int end = timelist.size();
            while (beg - end != 1) {
                int mid = (end - beg) / 2;
                remignment temp = timelist.get(mid);
                int compared = temp.time.compareTo(a.time);
                if (compared == 1)
                    beg = mid;
                if (compared == -1)
                    end = mid;
                else {
                    end = mid;
                    break;
                }
            }
            timelist.add(end, a);
        }
        else
            nonTimeList.add(a);
        for(subject i: sublist){
            if(i.name.equals(a.subject)){
                i.list.add(a);
                break;
            }
        }*/
    }


}
