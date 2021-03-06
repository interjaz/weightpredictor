package app.android.weightpredictor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.android.weightpredictor.R;

public class AddWeightActivity extends AppCompatActivity {

    public static final int REQUEST_DATE = 1;
    public static final int REQUEST_TIME = 2;

    public static final int RESPONSE_SUCCESS = 1;

    public static final String DATA_WEIGHT = "DATA_WEIGHT";
    public static final String DATA_DATE = "DATA_DATE";

    public static final String FORMAT_DATE = "yyyy/MM/dd HH:mm";

    private static final String FORMAT_DATE_UI = "dd MMMM yyyy";

    private SimpleDateFormat mDateFormatUi;
    private SimpleDateFormat mDateFormatData;
    private SimpleDateFormat mTimeFormatUi;

    private TextView mBtnDate;
    private TextView mBtnTime;
    private EditText mTxtWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTimeFormatUi = new SimpleDateFormat("HH:mm");
        mDateFormatData = new SimpleDateFormat(AddWeightActivity.FORMAT_DATE);
        mDateFormatUi = new SimpleDateFormat(AddWeightActivity.FORMAT_DATE_UI);

        mBtnDate = (TextView)findViewById(R.id.addWeight_btnDate);
        mBtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddWeightActivity.this, DateActivity.class);
                startActivityForResult(intent, AddWeightActivity.REQUEST_DATE);
            }
        });

        mBtnTime = (TextView)findViewById(R.id.addWeight_btnTime);
        mBtnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddWeightActivity.this, TimeActivity.class);
                startActivityForResult(intent, AddWeightActivity.REQUEST_TIME);
            }
        });

        mTxtWeight = (EditText) findViewById(R.id.addWeight_txtWeight);

        Date date = Calendar.getInstance().getTime();

        mBtnTime.setText(mTimeFormatUi.format(date));
        mBtnDate.setText(mDateFormatUi.format(date));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_weight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            if(mTxtWeight.getText().toString().equals("")) {
                mTxtWeight.setError("Please provide weight");
                return true;
            }

            saveResult();
            finish();
            return true;
        }

        if(id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveResult() {
        try {
            Intent intent = new Intent();

            String strWeight = mTxtWeight.getText().toString();
            double doubleWeight = Double.parseDouble(strWeight);
            intent.putExtra(AddWeightActivity.DATA_WEIGHT, doubleWeight);

            Calendar calendar = Calendar.getInstance();
            String strDateUi = mBtnDate.getText().toString();
            String strTimeUi = mBtnTime.getText().toString();
            Date date = mDateFormatUi.parse(strDateUi);
            Date time = mTimeFormatUi.parse(strTimeUi);
            calendar.setTime(time);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, hours);
            calendar.add(Calendar.MINUTE, minutes);

            Date dateTime = calendar.getTime();

            String strDateData = mDateFormatData.format(dateTime);
            intent.putExtra(AddWeightActivity.DATA_DATE, strDateData);
            setResult(AddWeightActivity.RESPONSE_SUCCESS, intent);
        }
        catch(ParseException ex){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AddWeightActivity.REQUEST_DATE && resultCode == DateActivity.RESPONSE_SUCCESS) {
            String strDate = data.getStringExtra(DateActivity.DATA);
            SimpleDateFormat parseFormat = new SimpleDateFormat(DateActivity.FORMAT_DATE);

            try {
                Date date = parseFormat.parse(strDate);
                mBtnDate.setText(mDateFormatUi.format(date));
            } catch (ParseException e) {

            }
        }

        if(requestCode == AddWeightActivity.REQUEST_TIME && resultCode == TimeActivity.RESPONSE_SUCCESS) {
            String time = data.getStringExtra(TimeActivity.DATA);
            mBtnTime.setText(time);
        }
    }
}
