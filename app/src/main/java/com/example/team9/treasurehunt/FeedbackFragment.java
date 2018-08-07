package com.example.team9.treasurehunt;

/**
 * Created by Tolu on 12/04/2017.
 *
 * Api Integration - Edward
 */

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.team9.treasurehunt.ApiClient.ApiClient;
import com.example.team9.treasurehunt.ApiClient.VolleyErrorHelper;
import com.example.team9.treasurehunt.ApiResources.CollectableTreasure;
import com.example.team9.treasurehunt.ApiResources.Feedback;
import com.example.team9.treasurehunt.ApiResources.Team;
import com.example.team9.treasurehunt.Runtime.SessionPlayer;


public class FeedbackFragment extends AppCompatActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("Feedback");
        setContentView(R.layout.fragment_feedback);

        final EditText nameField =(EditText) findViewById(R.id.nameText);
        final Spinner question1Spinner = (Spinner) findViewById(R.id.question_1_dialog);
        final Spinner question2Spinner = (Spinner) findViewById(R.id.question_2_dialog);
        final Spinner question3Spinner = (Spinner) findViewById(R.id.question_3_dialog);
        final Spinner question4Spinner = (Spinner) findViewById(R.id.question_4_dialog);
        final Spinner question5Spinner = (Spinner) findViewById(R.id.question_5_dialog);
        final Spinner question6Spinner = (Spinner) findViewById(R.id.question_6_dialog);
        final Spinner question7Spinner = (Spinner) findViewById(R.id.question_7_dialog);
        final Spinner question8Spinner = (Spinner) findViewById(R.id.question_8_dialog);
        final Spinner question9Spinner = (Spinner) findViewById(R.id.question_9_dialog);
        final Spinner question10Spinner = (Spinner) findViewById(R.id.question_10_dialog);
        final Spinner question11Spinner = (Spinner) findViewById(R.id.question_11_dialog);
        final Spinner question12Spinner = (Spinner) findViewById(R.id.question_12_dialog);
        final EditText question13Text =(EditText) findViewById(R.id.question_13_text);
        final EditText question14Text=(EditText) findViewById(R.id.question_14_text);


        Button sendE = (Button) findViewById(R.id.ButtonSendFeedback);
        sendE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = nameField.getText().toString();

                //check for if the name of the user is empty
                if(TextUtils.isEmpty(name)){
                    nameField.setError("Enter Your Name");
                    nameField.requestFocus();
                    return;
                }

                String question1 = question1Spinner.getSelectedItem().toString();
                String question2 = question2Spinner.getSelectedItem().toString();
                String question3 = question3Spinner.getSelectedItem().toString();
                String question4 = question4Spinner.getSelectedItem().toString();
                String question5 = question5Spinner.getSelectedItem().toString();
                String question6 = question6Spinner.getSelectedItem().toString();
                String question7 = question7Spinner.getSelectedItem().toString();
                String question8 = question8Spinner.getSelectedItem().toString();
                String question9 = question9Spinner.getSelectedItem().toString();
                String question10 = question10Spinner.getSelectedItem().toString();
                String question11 = question11Spinner.getSelectedItem().toString();
                String question12 = question12Spinner.getSelectedItem().toString();
                String question13 = question13Text.getText().toString();
                String question14 = question14Text.getText().toString();

                Feedback feedback = new Feedback();
                feedback.setName(name);
                feedback.setQuestion1(question1);
                feedback.setQuestion2(question1);
                feedback.setQuestion3(question1);
                feedback.setQuestion4(question1);
                feedback.setQuestion5(question1);
                feedback.setQuestion6(question1);
                feedback.setQuestion7(question1);
                feedback.setQuestion8(question1);
                feedback.setQuestion9(question1);
                feedback.setQuestion10(question1);
                feedback.setQuestion11(question1);
                feedback.setQuestion12(question1);
                feedback.setQuestion13(question1);
                feedback.setQuestion14(question1);

                ApiClient.sendFeedback(feedback, getApplicationContext(), new Response.Listener<Feedback>() {

                    @Override
                    public void onResponse(Feedback feedback) {
                        //successfully got team from api

                        showFeedbackSubmitSuccessful();

                    }
                },new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //couldn't get clues from server for some reason

                        NetworkResponse response = error.networkResponse;

                        showFailedToCollectTreasureDialog(VolleyErrorHelper.getServerConnectionError(error, true));

                    }
                });


            }
        });

        //question 1 spinner
        Spinner question1 = (Spinner) findViewById(R.id.question_1_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.gender_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question1.setAdapter(adapter1);

        //question 2 spinner
        Spinner question2 = (Spinner) findViewById(R.id.question_2_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.age_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question2.setAdapter(adapter2);

        //question 3 spinner
        Spinner question3 = (Spinner) findViewById(R.id.question_3_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.general_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question3.setAdapter(adapter3);

        //question 4 spinner
        Spinner question4 = (Spinner) findViewById(R.id.question_4_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,R.array.yes_no_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question4.setAdapter(adapter4);

        //question 5 spinner
        Spinner question5 = (Spinner) findViewById(R.id.question_5_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,R.array.general_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question5.setAdapter(adapter5);

        //question 6 spinner
        Spinner question6 = (Spinner) findViewById(R.id.question_6_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,R.array.general_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question6.setAdapter(adapter6);

        //question 7 spinner
        Spinner question7 = (Spinner) findViewById(R.id.question_7_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(this,R.array.general_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question7.setAdapter(adapter7);

        //question 8 spinner
        Spinner question8 = (Spinner) findViewById(R.id.question_8_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(this,R.array.general_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question8.setAdapter(adapter8);

        //question 9 spinner
        Spinner question9 = (Spinner) findViewById(R.id.question_9_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter9 = ArrayAdapter.createFromResource(this,R.array.general_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question9.setAdapter(adapter9);

        //question 10 spinner
        Spinner question10 = (Spinner) findViewById(R.id.question_10_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter10 = ArrayAdapter.createFromResource(this,R.array.general_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question10.setAdapter(adapter10);

        //question 11 spinner
        Spinner question11 = (Spinner) findViewById(R.id.question_11_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter11 = ArrayAdapter.createFromResource(this,R.array.general_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question11.setAdapter(adapter11);

        //question 12 spinner
        Spinner question12 = (Spinner) findViewById(R.id.question_12_dialog);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter12 = ArrayAdapter.createFromResource(this,R.array.yes_no_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        question12.setAdapter(adapter12);

    }

    private void showFeedbackSubmitSuccessful(){
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(FeedbackFragment.this).create();
        alertDialog.setTitle("Success!");
        alertDialog.setMessage("feedback successfully submitted");
        alertDialog.show();
    }
    private void showFailedToCollectTreasureDialog(String message){
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(FeedbackFragment.this).create();
        alertDialog.setTitle("Fail!");
        alertDialog.setMessage(message);
        alertDialog.show();

    }

}
