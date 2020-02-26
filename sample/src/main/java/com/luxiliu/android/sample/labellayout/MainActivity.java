package com.luxiliu.android.sample.labellayout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;

import com.luxiliu.android.widget.LabelLayout;

public class MainActivity extends AppCompatActivity {
    private LabelLayout mLabelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mLabelLayout = findViewById(R.id.label_layout);

        AppCompatSeekBar distanceSeekBar = findViewById(R.id.distance_seek_bar);
        distanceSeekBar.setProgress(mLabelLayout.getLabelDistance());
        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AppCompatSeekBar heightSeekBar = findViewById(R.id.height_seek_bar);
        heightSeekBar.setProgress(mLabelLayout.getLabelHeight());
        heightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AppCompatSpinner colorSpinner = findViewById(R.id.color_spinner);
        ArrayAdapter<String> colorArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Black", "Red", "Blue"});
        colorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorArrayAdapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AppCompatSpinner gravitySpinner = findViewById(R.id.gravity_spinner);
        ArrayAdapter<LabelLayout.Gravity> gravityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, LabelLayout.Gravity.values());
        gravityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gravitySpinner.setAdapter(gravityArrayAdapter);
        gravitySpinner.setSelection(mLabelLayout.getLabelGravity().ordinal());
        gravitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText editText = findViewById(R.id.edit_text);
        editText.setText(mLabelLayout.getLabelText());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                update();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        AppCompatSeekBar textSizeSeekBar = findViewById(R.id.text_size_seek_bar);
        textSizeSeekBar.setProgress(mLabelLayout.getLabelTextSize());
        textSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                update();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AppCompatSpinner textColorSpinner = findViewById(R.id.text_color_spinner);
        ArrayAdapter<String> textColorArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"White", "Green", "Yellow"});
        textColorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textColorSpinner.setAdapter(textColorArrayAdapter);
        textColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AppCompatSpinner directionSpinner = findViewById(R.id.direction_spinner);
        ArrayAdapter<LabelLayout.TextDirection> directionArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, LabelLayout.TextDirection.values());
        directionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directionSpinner.setAdapter(directionArrayAdapter);
        directionSpinner.setSelection(mLabelLayout.getLabelTextDirection().ordinal());
        directionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void update() {
        AppCompatSeekBar distanceSeekBar = findViewById(R.id.distance_seek_bar);
        AppCompatSeekBar heightSeekBar = findViewById(R.id.height_seek_bar);
        AppCompatSpinner colorSpinner = findViewById(R.id.color_spinner);
        AppCompatSpinner gravitySpinner = findViewById(R.id.gravity_spinner);

        EditText editText = findViewById(R.id.edit_text);
        AppCompatSeekBar textSizeSeekBar = findViewById(R.id.text_size_seek_bar);
        AppCompatSpinner textColorSpinner = findViewById(R.id.text_color_spinner);
        AppCompatSpinner textDirectionSpinner = findViewById(R.id.direction_spinner);

        mLabelLayout.setLabelDistance(distanceSeekBar.getProgress());
        mLabelLayout.setLabelHeight(heightSeekBar.getProgress());
        mLabelLayout.setLabelBackground(new ColorDrawable(Color.parseColor(colorSpinner.getSelectedItem().toString().toLowerCase())));
        mLabelLayout.setLabelGravity((LabelLayout.Gravity) gravitySpinner.getSelectedItem());
        mLabelLayout.setLabelText(editText.getText().toString());
        mLabelLayout.setLabelTextSize(textSizeSeekBar.getProgress());
        mLabelLayout.setLabelTextColor(Color.parseColor(textColorSpinner.getSelectedItem().toString().toLowerCase()));
        mLabelLayout.setLabelTextDirection((LabelLayout.TextDirection) textDirectionSpinner.getSelectedItem());
    }
}
