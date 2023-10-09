package com.example.doppel_e;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.doppel_e.Stars.Model.StarModel;
import com.example.doppel_e.Stars.Utils.StarsDataBaseHandler;

import java.util.ArrayList;
import java.util.List;

public class PointsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Spinner spinner;
    private TextView text;
    private ArrayAdapter<CharSequence> adapter;
    private StarsDataBaseHandler dbHandler;
    private TextView pointsPlayer1, pointsPlayer2;
    private ProgressBar progressBar1, progressBar2;
    private Button winButton, loseButton;
    private List<StarModel> data = new ArrayList<>();
    private int item = 0;

    private StarsDataBaseHandler starsDBHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_points, container, false);
        this.spinner = this.view.findViewById(R.id.spinner);


        adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.players_array, R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        int code_1 = item;
        int code_2 = (item+1)%2;

        int stars1 = data.get(0).getStars();
        int stars2 = data.get(1).getStars();
        this.pointsPlayer1 = view.findViewById(R.id.text_1);
        this.pointsPlayer1.setText(Integer.toString( stars1));
        this.pointsPlayer2 = view.findViewById(R.id.text_2);
        this.pointsPlayer2.setText(Integer.toString( stars2));

        this.progressBar1 = view.findViewById(R.id.progress_1);
        this.progressBar2 = view.findViewById(R.id.progress_2);
        this.progressBar1.setProgress(stars1*100/20);
        this.progressBar2.setProgress(stars2*100/20);

        this.winButton = view.findViewById(R.id.winButton);

        this.winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.updateStarStars(data.get(code_1).getCode(),true );
                int stars1 = data.get(0).getStars();
                pointsPlayer1.setText(Integer.toString( stars1));
                progressBar1.setProgress(stars1*100/20);
            }
        });
        this.loseButton = view.findViewById(R.id.loseButton);

        this.loseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.updateStarStars(data.get(code_1).getCode(),false);
                int stars1 = data.get(0).getStars();
                pointsPlayer1.setText(Integer.toString( stars1));
                progressBar1.setProgress(stars1*100/20);
            }
        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.text = this.view.findViewById(R.id.player_text);
        this.item = i;
        this.text.setText(adapterView.getItemAtPosition((i+1)%2).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void pushData(List<StarModel> data){
        this.data = data;
    }

    public void setDbHandler(StarsDataBaseHandler dbHandler) {
        this.dbHandler = dbHandler;
    }
}