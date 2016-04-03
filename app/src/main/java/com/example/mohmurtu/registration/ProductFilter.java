package com.example.mohmurtu.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ProductFilter extends AppCompatActivity {

    Button filterButton ;
    RadioGroup visibility, approval ;
    RadioButton visibilitySelected, approvalSelected ;
    final static int FILTER_RESULT_CODE = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Products Filter");
        visibility = (RadioGroup) findViewById(R.id.visibility_group);
        visibility.check(R.id.visibility_all);
        approval = (RadioGroup) findViewById(R.id.approval_group);
        approval.check(R.id.approval_all);
        filterButton = (Button) findViewById(R.id.filter_button);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibilitySelectedId = visibility.getCheckedRadioButtonId();
                int approvalSelectedId = approval.getCheckedRadioButtonId();
                visibilitySelected = (RadioButton) findViewById(visibilitySelectedId);
                approvalSelected = (RadioButton) findViewById(approvalSelectedId);

                String visibilityText = visibilitySelected.getText().toString();
                String approvalText = approvalSelected.getText().toString();
                int visibilityStatus=0, approvalStatus =0 ;
                if(visibilityText.equals("Enabled"))
                    visibilityStatus = 0 ;
                else if(visibilityText.equals("Disabled"))
                    visibilityStatus = 1 ;
                else
                    visibilityStatus = 2 ;

                if(approvalText.equals("Approved Products"))
                    approvalStatus = 0 ;
                else if(approvalText.equals("Rejected Products"))
                    approvalStatus = 2 ;
                else if(approvalText.equals("Pending For Approvals"))
                    approvalStatus = 1 ;
                else
                    approvalStatus = 3 ;

                Intent intent = new Intent();
                intent.putExtra("visibility", visibilityStatus);
                intent.putExtra("approval", approvalStatus);
                setResult(FILTER_RESULT_CODE,intent);
                finish();
            }
        });
    }

    public void onFilterButtonClick(){

    }

}
