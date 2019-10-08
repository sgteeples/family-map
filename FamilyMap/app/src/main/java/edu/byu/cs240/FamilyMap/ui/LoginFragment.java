package edu.byu.cs240.FamilyMap.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import edu.byu.cs240.FamilyMap.net.ServerProxy;

import java.util.Arrays;

import edu.byu.cs240.FamilyMap.R;
import edu.byu.cs240.FamilyMap.model.*;
import edu.byu.cs240.FamilyMap.utils.Deserializer;

public class LoginFragment extends Fragment {

    private Button signInButton;
    private Button registerButton;

    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;

    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    private void wireUpWidgets(View v) {
        signInButton = v.findViewById(R.id.signInButton);
        registerButton = v.findViewById(R.id.registerButton);
        userNameEditText = v.findViewById(R.id.usernameEditText);
        passwordEditText = v.findViewById(R.id.passwordEditText);
        firstNameEditText = v.findViewById(R.id.firstNameEditText);
        lastNameEditText = v.findViewById(R.id.lastNameEditText);
        emailEditText = v.findViewById(R.id.emailEditText);
        maleRadioButton = v.findViewById(R.id.maleRadioButton);
        genderRadioGroup = v.findViewById(R.id.genderRadioGroup);
        serverHostEditText = v.findViewById(R.id.serverHostEditText);
        serverPortEditText = v.findViewById(R.id.serverPortEditText);
    }

    private void addTextEditListener(EditText widget) {
        widget.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                determineButtonEnableState();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Wire up widgets
        wireUpWidgets(v);

        // The sign in and register buttons start out in disabled state
        signInButton.setEnabled(false);
        registerButton.setEnabled(false);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegisterButtonClick();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignInButtonClick();
            }
        });

        addTextEditListener(userNameEditText);
        addTextEditListener(passwordEditText);
        addTextEditListener(firstNameEditText);
        addTextEditListener(lastNameEditText);
        addTextEditListener(emailEditText);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                determineButtonEnableState();
            }
        });

        return v;
    }

    private void determineButtonEnableState() {
        signInButton.setEnabled(loginButtonShouldBeEnabled());
        registerButton.setEnabled(registerButtonShouldBeEnabled());
    }

    private boolean loginButtonShouldBeEnabled() {
        return !getText(userNameEditText).equals("") &&
                !getText(passwordEditText).equals("");
    }

    private boolean registerButtonShouldBeEnabled() {
        return !getText(userNameEditText).equals("") &&
                !getText(passwordEditText).equals("") &&
                !getText(firstNameEditText).equals("") &&
                !getText(lastNameEditText).equals("") &&
                !getText(emailEditText).equals("") &&
                genderRadioGroup.getCheckedRadioButtonId() != -1;
    }

    private String getText(EditText t) {
        return t.getText().toString();
    }

    private void handleSignInButtonClick() {
        new SignInTask().
                execute(getText(serverHostEditText),
                        getText(serverPortEditText),
                        getText(userNameEditText),
                        getText(passwordEditText));
    }

    private void handleRegisterButtonClick() {
        String gender;

        if (genderRadioGroup.getCheckedRadioButtonId() == maleRadioButton.getId()) {
            gender = "m";
        } else {
            gender = "f";
        }

        new RegisterTask().execute(getText(serverHostEditText),
                getText(serverPortEditText),
                getText(userNameEditText),
                getText(passwordEditText),
                getText(emailEditText),
                getText(firstNameEditText),
                getText(lastNameEditText),
                gender);
    }

    class RegisterTask extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... registerData) {
            String serverHost = registerData[0];
            String serverPort = registerData[1];
            String username = registerData[2];
            String password = registerData[3];
            String email = registerData[4];
            String firstName = registerData[5];
            String lastName = registerData[6];
            String gender = registerData[7];

            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            RegisterResult result = proxy.registerUser(username, password, email,
                    firstName, lastName, gender);

            if (result != null && result.getMessage() == null) {
                // Add information to the model
                Model myModel = Model.getInstance();
                myModel.setAuthToken(result.getAuthToken());
                myModel.setUserID(result.getPersonID());
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                new GetDataTask().execute(getText(serverHostEditText),
                        getText(serverPortEditText),
                        Model.getInstance().getAuthToken());
            } else {
                Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    class SignInTask extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... registerData) {
            String serverHost = registerData[0];
            String serverPort = registerData[1];
            String username = registerData[2];
            String password = registerData[3];

            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            LoginResult result = proxy.loginUser(username, password);

            if (result != null && result.getMessage() == null) {
                Model myModel = Model.getInstance();
                myModel.setAuthToken(result.getAuthToken());
                myModel.setUserID(result.getPersonID());
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                new GetDataTask().execute(getText(serverHostEditText),
                        getText(serverPortEditText),
                        Model.getInstance().getAuthToken());
            } else {
                Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    class GetDataTask extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... requestData) {
            String serverHost = requestData[0];
            String serverPort = requestData[1];
            String authToken = requestData[2];

            ServerProxy proxy = new ServerProxy(serverHost, serverPort);
            Model myModel = Model.getInstance();
            Deserializer d = new Deserializer();

            String userPersonsJson = proxy.getAllUserPersons(authToken);
            Person[] userPersonsArray = ((PersonArray)d.deserialize(userPersonsJson, PersonArray.class)).getData();

            myModel.setUserPersons(Arrays.asList(userPersonsArray));

            String userEventsJson = proxy.getAllUserEvents(authToken);
            Event[] userEventsArray = ((EventArray)d.deserialize(userEventsJson, EventArray.class)).getData();

            myModel.setUserEvents(Arrays.asList(userEventsArray));

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            ((MainActivity)getActivity()).startMapFragment();
        }
    }
}