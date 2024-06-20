package com.example.bigemassignment1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.bigemassignment1.databinding.ActivityMainBinding;
import com.example.bigemassignment1.models.BeverageCost;
import com.google.android.material.snackbar.Snackbar;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

//    Creating necessary global variables
    double addMilk = 0;
    double addSugar = 0;

//    Giving value to beverage type as a Coffee because it is selected by default and can be changed in application
    String bevType = "Coffee";

    String bevSize;
    double sizePrice = 0;

    String flavorChosen;
    double flavorPrice = 0;

    String regionChosen;
    String regionStoreChosen;

    //    Creating an instance of the generated binding class
    ActivityMainBinding binding;

    //    DatePicker
    DatePickerDialog datePicker;

//    Creating an instance of the BeverageCost Class
    BeverageCost BCost;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);

        //        instantiating the binding view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        Setting for the tea or coffee radio button
        binding.rdCoffee.setChecked(true);
        binding.rdTea.setChecked(false);

        //Setting initial spinner values as coffee
        setFlavorSpinner(R.array.coffee_array);

//        Setting for the addition
        binding.rdSmall.setChecked(false);
        binding.rdMedium.setChecked(false);
        binding.rdLarge.setChecked(false);

        // Setting up AutoCompleteTextView for regions
        setupRegionAutoCompleteTextView();


        //For Date picker
        binding.edtDate.setInputType(InputType.TYPE_NULL);

        //Listeners
        setListeners();


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

//    Setting the listeners
    private void setListeners(){
        binding.chkMilk.setOnClickListener(this);
        binding.chkSugar.setOnClickListener(this);
        binding.rgBeverageType.setOnCheckedChangeListener(this);
        binding.rgBeverageSize.setOnCheckedChangeListener(this);
        binding.acRegion.setOnItemClickListener(this::onRegionItemClick);
        binding.flaSpinner.setOnItemSelectedListener(this);
        binding.storeSpinner.setOnItemSelectedListener(this);
        binding.edtDate.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }


    // Setting up AutoCompleteTextView for regions
    private void setupRegionAutoCompleteTextView() {
        binding.acRegion.setThreshold(2);
        binding.acRegion.setInputType(InputType.TYPE_NULL);

        ArrayAdapter<CharSequence> adapterRegion = ArrayAdapter.createFromResource(this,
                R.array.region_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.acRegion.setAdapter(adapterRegion);
    }

    //Handle region selection
    private void onRegionItemClick(AdapterView<?> parent, View view, int position, long id){
        regionChosen = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(), regionChosen, Toast.LENGTH_LONG).show();
        updateStoreSpinner(regionChosen);
    }

//    Update store spinner based on selected region
    private void updateStoreSpinner(String region){
        int storesArrayId;
        switch(region){
            case "London":
                storesArrayId = R.array.london_array;
                break;
            case "Milton":
                storesArrayId = R.array.milton_array;
                break;
            case "Mississauga":
                storesArrayId = R.array.mississauga_array;
                break;
            default:
                storesArrayId = R.array.waterloo_array;
        }

        ArrayAdapter<CharSequence> adapterSpaceStore = ArrayAdapter.createFromResource(this,
                storesArrayId, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

//        Specifying the layout to use when the list of choices appears
        adapterSpaceStore.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

//        Binding the adapter to the spinner
        binding.storeSpinner.setAdapter(adapterSpaceStore);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // For Flavor added
        if (adapterView.getId() == binding.flaSpinner.getId()) {
            flavorChosen = binding.flaSpinner.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), flavorChosen, Toast.LENGTH_LONG).show();

//            Calling flavor update function
            updateFlavorPrice(flavorChosen);
        }
        // For region store
        else if (adapterView.getId() == binding.storeSpinner.getId()) {
            regionStoreChosen = binding.storeSpinner.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), regionStoreChosen, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View v) {
        //Checking whether the milk and sugar checkbox is ticked or not
        if(v.getId() == binding.chkSugar.getId()){
            addSugar = binding.chkSugar.isChecked() ? 1.00 : 0;
        }else if(v.getId() == binding.chkMilk.getId()){
            addMilk = binding.chkMilk.isChecked() ? 1.25 : 0;
        }else if(v.getId() == R.id.edtDate){
            Calendar cal = Calendar.getInstance();
            int dayOfSales = cal.get(Calendar.DAY_OF_MONTH);
            int monthOfSales = cal.get(Calendar.MONTH);
            int yearOfSales = cal.get(Calendar.YEAR);
            datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day){
//                    Ensuring date is before or the current date
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, day);

                    // Normalize selectedDate to the start of the day
                    selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                    selectedDate.set(Calendar.MINUTE, 0);
                    selectedDate.set(Calendar.SECOND, 0);
                    selectedDate.set(Calendar.MILLISECOND, 0);

                    if(selectedDate.after(cal)){
                        binding.edtDate.setError("Sales date cannot be in the future.");
                    }else{
                        binding.edtDate.setError(null);
                        binding.edtDate.setText(day + "/" + (month+1) + "/" + year);
                    }
                }
            }, yearOfSales, monthOfSales, dayOfSales);

            // Set the max date to today
            datePicker.getDatePicker().setMaxDate(cal.getTimeInMillis());

            datePicker.show();
        }
        else if(v.getId() == R.id.btnSubmit){
            if(formValidated()){
                BCost = new BeverageCost(binding.edtName.getText().toString(), binding.edtNumber.getText().toString(), binding.edtEmail.getText().toString(), bevType, addMilk, addSugar, flavorChosen, flavorPrice, bevSize, sizePrice, regionChosen, regionStoreChosen, binding.edtDate.getText().toString());
                String result = BCost.getBeverageCost();

                //Snackbar
                Snackbar snackbar = Snackbar.make(binding.delBev, result, Snackbar.LENGTH_LONG);
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Okay", new View.OnClickListener(){
                   @Override
                   public void onClick(View v){
                       snackbar.dismiss();
                   }
                });


                // Get the Snackbar's TextView and customize it if necessary
                View snackbarView = snackbar.getView();
                TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setMaxLines(15); // Allow multiple lines if needed

                snackbar.show();
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        regionChosen = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(), regionChosen,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//        Checking whether the tea or coffee is selected for populating the flavor spinner
        if (radioGroup.getId() == binding.rgBeverageType.getId()) {
            if (checkedId == R.id.rdCoffee) {
                bevType = "Coffee";
                setFlavorSpinner(R.array.coffee_array);
            } else if (checkedId == R.id.rdTea) {
                bevType = "Tea";
                setFlavorSpinner(R.array.tea_array);
            }
        }

//        Checking which beverage size is selected for storing the price
        if(radioGroup.getId() == binding.rgBeverageSize.getId()){
            if (binding.rdCoffee.isChecked()) {
                if (checkedId == binding.rdSmall.getId()) {
                    sizePrice = 1.75;
                    bevSize = "Small";
                } else if (checkedId == binding.rdMedium.getId()) {
                    sizePrice = 2.75;
                    bevSize = "Medium";
                } else if (checkedId == binding.rdLarge.getId()) {
                    sizePrice = 3.75;
                    bevSize = "Large";
                }
            } else if (binding.rdTea.isChecked()) {
                if (checkedId == binding.rdSmall.getId()) {
                    sizePrice = 1.50;
                    bevSize = "Small";
                } else if (checkedId == binding.rdMedium.getId()) {
                    sizePrice = 2.50;
                    bevSize = "Medium";
                } else if (checkedId == binding.rdLarge.getId()) {
                    sizePrice = 3.25;
                    bevSize = "Large";
                }
            }
        }
    }

    //Creating setFlavorSpinner method
    private void setFlavorSpinner(int arrayResId) {
        //        Creating an ArrayAdapter to fetch the items from an array
        ArrayAdapter<CharSequence> adapterSpaceCoffeeTea = ArrayAdapter.createFromResource(this,
                arrayResId, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

//        Specifying the layout to use when the list of choices appears
        adapterSpaceCoffeeTea.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

//        Binding the adapter to the spinner
        binding.flaSpinner.setAdapter(adapterSpaceCoffeeTea);
    }

//    Update flavor price function
        private void updateFlavorPrice(String flavor) {
            if (binding.rdCoffee.isChecked()) {
                switch (flavor) {
                    case "None":
                        this.flavorPrice = 0;
                        break;
                    case "Pumpkin Spice":
                        this.flavorPrice = 0.50;
                        break;
                    case "Chocolate":
                        this.flavorPrice = 0.75;
                        break;
                }
            } else if (binding.rdTea.isChecked()) {
                switch (flavor) {
                    case "None":
                        this.flavorPrice = 0;
                        break;
                    case "Lemon":
                        this.flavorPrice = 0.25;
                        break;
                    case "Ginger":
                        this.flavorPrice = 0.75;
                        break;
                }
            }
        }

//    Form Validation
    private boolean formValidated(){
        //Validating Customer Name
        if(binding.edtName.length() == 0){
            binding.edtName.setError("Customer Name is required");
            return false;
        }

        //Validating Customer Email ID
        if(binding.edtEmail.length() == 0){
            binding.edtEmail.setError("Email ID is required");
            return false;
        }  else if(!isValidEmail(binding.edtEmail.getText().toString())) {
           binding.edtEmail.setError("Invalid email address");
            return false;
        }

        //Validating Customer Phone Number
        if(binding.edtNumber.length() == 0){
            binding.edtNumber.setError("Phone number is required");
            return false;
        }

        //        Validating beverage size
        if (binding.rgBeverageSize.getCheckedRadioButtonId() == -1) {
            Toast.makeText(MainActivity.this, "No beverage size selected", Toast.LENGTH_SHORT).show();
            return false;
        }

        //        Validating the region
        if (binding.acRegion.getText().toString().isEmpty() || binding.acRegion.getText().toString().equals("Select Region")) {
            binding.acRegion.setError("Region Name is required");
            return false;
        }

        // Validating the date
        if (binding.edtDate.getText().toString().isEmpty()) {
            binding.edtDate.setError("Date selection is required");
            return false;
        }

        return true;
    }

//    Function to check the valid email
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}