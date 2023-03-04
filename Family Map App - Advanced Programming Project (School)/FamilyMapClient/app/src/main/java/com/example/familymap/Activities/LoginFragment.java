package com.example.familymap.Activities;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.familymap.DataContainers.FamilyTree;
import com.example.familymap.DataContainers.MainActivityViewModel;
import com.example.familymap.ServerCom.HttpClient;
import com.example.familymap.R;
import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonLookupResult;
import result.PersonResult;
import result.RegisterResult;


public class LoginFragment extends Fragment {

    private Listener listener;
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String REGISTER = "Register";
    private static final String LOGIN = "Login";
    private static final String PERSONS  = "Persons";
    private static final String EVENTS = "Events";
    private static final String USER = "User";

    private String host;
    private String port;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;



    private Gson gson = new Gson();
    private HttpClient httpClient = new HttpClient();
    private FamilyTree familyTree;





    private MainActivityViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    public interface Listener {
        void notifyLogin( FamilyTree familyTree);
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        Button loginbutton = view.findViewById(R.id.signInButton);
        loginbutton.setEnabled(false);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                if (listener != null) {

                    Handler uiThreadMessageHandler = new Handler() {

                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            String firstname = bundle.getString(FIRST_NAME, null);
                            String lastname = bundle.getString(LAST_NAME,null);
                            String loginOutput = bundle.getString(LOGIN,null);
                            String eventsOutput = bundle.getString(EVENTS,null);
                            String personsOutput = bundle.getString(PERSONS,null);
                            String personSearchOutput = bundle.getString(USER,null);

                            Toast.makeText(getContext(),firstname+" "+lastname,Toast.LENGTH_LONG).show();
                            if (lastname != "") {
                                LoginResult result = gson.fromJson(loginOutput, LoginResult.class);
                                EventResult eventResult = gson.fromJson(eventsOutput, EventResult.class);
                                PersonResult personResult = gson.fromJson(personsOutput, PersonResult.class);
                                PersonLookupResult personLookupResult = gson.fromJson(personSearchOutput, PersonLookupResult.class);
                                Person user = new Person(personLookupResult.getPersonID(),personLookupResult.getAssociatedUsername(),personLookupResult.getFirstName(),personLookupResult.getLastName(),personLookupResult.getGender(),personLookupResult.getFatherID(),personLookupResult.getMotherID(),personLookupResult.getSpouseID());
                                familyTree = FamilyTree.load_Tree(user, eventResult.getData(), personResult.getData(),result.getAuthtoken());
                                listener.notifyLogin(familyTree);
                            }
                        }
                    };
                    LoginTask task = new LoginTask(uiThreadMessageHandler);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);

                }
            }
        });

        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                if (listener != null) {

                    Handler uiThreadMessageHandler = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            String firstname = bundle.getString(FIRST_NAME, null);
                            String lastname = bundle.getString(LAST_NAME,null);
                            String registerOutput = bundle.getString(REGISTER,null);
                            String eventsOutput = bundle.getString(EVENTS,null);
                            String personsOutput = bundle.getString(PERSONS,null);
                            String personSearchOutput = bundle.getString(USER,null);

                            Toast.makeText(getContext(),firstname+" "+lastname,Toast.LENGTH_LONG).show();
                            if (lastname != "") {
                                RegisterResult result = gson.fromJson(registerOutput, RegisterResult.class);
                                EventResult eventResult = gson.fromJson(eventsOutput, EventResult.class);
                                PersonResult personResult = gson.fromJson(personsOutput, PersonResult.class);
                                PersonLookupResult personLookupResult = gson.fromJson(personSearchOutput, PersonLookupResult.class);
                                Person user = new Person(personLookupResult.getPersonID(),personLookupResult.getAssociatedUsername(),personLookupResult.getFirstName(),personLookupResult.getLastName(),personLookupResult.getGender(),personLookupResult.getFatherID(),personLookupResult.getMotherID(),personLookupResult.getSpouseID());
                                familyTree = FamilyTree.load_Tree(user, eventResult.getData(), personResult.getData(),result.getAuthtoken());
                                listener.notifyLogin(familyTree);
                            }
                        }
                    };
                    RegisterTask task = new RegisterTask(uiThreadMessageHandler);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);

                }
            }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                host = ((EditText) getView().findViewById(R.id.serverHostField)).getText().toString();
                port = ((EditText) getView().findViewById(R.id.serverPortField)).getText().toString();
                username = ((EditText) getView().findViewById(R.id.userNameField)).getText().toString();
                password = ((EditText) getView().findViewById(R.id.passwordField)).getText().toString();
                firstname = ((EditText) getView().findViewById(R.id.firstNameField)).getText().toString();
                lastname = ((EditText) getView().findViewById(R.id.lastNameField)).getText().toString();
                email = ((EditText) getView().findViewById(R.id.emailField)).getText().toString();
                checkFilled(view);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        EditText serverHostField = (EditText) view.findViewById(R.id.serverHostField);
        serverHostField.setText(getViewModel().getServerHost());
        serverHostField.addTextChangedListener(textWatcher);

        EditText serverPortField = (EditText) view.findViewById(R.id.serverPortField);
        serverPortField.setText(getViewModel().getServerPort());
        serverPortField.addTextChangedListener(textWatcher);

        EditText userName = (EditText)view.findViewById(R.id.userNameField);
        userName.setText(getViewModel().getUserName());
        userName.addTextChangedListener(textWatcher);

        EditText password = (EditText)view.findViewById(R.id.passwordField);
        password.setText(getViewModel().getPassword());
        password.addTextChangedListener(textWatcher);

        EditText firstName = (EditText)view.findViewById(R.id.firstNameField);
        firstName.setText(getViewModel().getFirstName());
        firstName.addTextChangedListener(textWatcher);

        EditText lastName = (EditText)view.findViewById(R.id.lastNameField);
        lastName.setText(getViewModel().getLastName());
        lastName.addTextChangedListener(textWatcher);

        EditText email = (EditText)view.findViewById(R.id.emailField);
        email.setText(getViewModel().getEmail());
        email.addTextChangedListener(textWatcher);


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton gend = (RadioButton) (getView().findViewById(R.id.radioMale));
                if (gend.isChecked()) {
                    gender = "m";
                }
                else {
                    gender = "f";
                }
                checkFilled(view);
            }
        };

        RadioButton male = (RadioButton) view.findViewById(R.id.radioMale);
        male.setOnClickListener(clickListener);

        RadioButton female = (RadioButton) view.findViewById(R.id.radioFemale);
        female.setOnClickListener(clickListener);

        return view;

    }

    private class LoginTask implements Runnable {
        private Handler messageHandler;
        public LoginTask(Handler messageHandler) {
            this.messageHandler = messageHandler;
        }

        @Override
        public void run() {
            LoginRequest loginRequest = new LoginRequest(username, password);
            String json = gson.toJson(loginRequest);
            String output = httpClient.postURL(host,port,HttpClient.LOGIN_EXTENSION,json);

            String firstname = null;
            String lastname = null;
            String eventOutput = null;
            String personOutput = null;
            String personLookupOutput = null;

            LoginResult result = gson.fromJson(output, LoginResult.class);
            if (result != null) {
                if (result.isSuccess()) {
                    String authToken = result.getAuthtoken();
                    eventOutput = httpClient.getURL(host, port, HttpClient.EVENT_EXTENSION, authToken);
                    personOutput = httpClient.getURL(host, port, HttpClient.PERSON_EXTENSION, authToken);
                    EventResult eventResult = gson.fromJson(eventOutput, EventResult.class);
                    PersonResult personResult = gson.fromJson(personOutput, PersonResult.class);
                    personLookupOutput = httpClient.getURL(host, port, HttpClient.PERSON_EXTENSION + "/" + result.getPersonID(), authToken);
                    PersonLookupResult personLookupResult = gson.fromJson(personLookupOutput, PersonLookupResult.class);
                    firstname = personLookupResult.getFirstName();
                    lastname = personLookupResult.getLastName();

                }
            }
            sendMessage(firstname,lastname,output,eventOutput,personOutput,personLookupOutput);

        }
        private void sendMessage(String firstName, String lastName,String loginOutput, String eventResult, String personResult, String user) {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            if (firstName != null) {
                bundle.putString(FIRST_NAME,firstName);
                bundle.putString(LAST_NAME,lastName);
                bundle.putString(LOGIN, loginOutput);
                bundle.putString(EVENTS, eventResult);
                bundle.putString(PERSONS, personResult);
                bundle.putString(USER, user);
            }
            else {
                bundle.putString(FIRST_NAME,"Login Failed");
                bundle.putString(LAST_NAME,"");
                bundle.putString(LOGIN, "");
                bundle.putString(EVENTS, "");
                bundle.putString(PERSONS, "");
                bundle.putString(USER, "");
            }
            message.setData(bundle);
            messageHandler.sendMessage(message);
            messageHandler.handleMessage(message);

        }
    }


    private class RegisterTask implements Runnable {
        private Handler messageHandler;
        public RegisterTask(Handler messageHandler) {
            this.messageHandler = messageHandler;
        }

        @Override
        public void run() {
            RegisterRequest registerRequest = new RegisterRequest(username, password,email,firstname,lastname,gender);
            String json = gson.toJson(registerRequest);

            String output = httpClient.postURL(host,port,HttpClient.REGISTER_EXTENSION,json);
            String firstname = null;
            String lastname = null;
            String eventOutput = null;
            String personOutput = null;
            String personLookupOutput = null;

            RegisterResult result = (RegisterResult) gson.fromJson(output, RegisterResult.class);
            if(result!=null) {
                if (result.isSuccess()) {
                    String authToken = result.getAuthtoken();
                    eventOutput = httpClient.getURL(host, port, HttpClient.EVENT_EXTENSION, authToken);
                    personOutput = httpClient.getURL(host, port, HttpClient.PERSON_EXTENSION, authToken);
                    EventResult eventResult = gson.fromJson(eventOutput, EventResult.class);
                    PersonResult personResult = gson.fromJson(personOutput, PersonResult.class);
                    personLookupOutput = httpClient.getURL(host, port, HttpClient.PERSON_EXTENSION + "/" + result.getPersonID(), authToken);
                    PersonLookupResult personLookupResult = gson.fromJson(personLookupOutput, PersonLookupResult.class);
                    firstname = personLookupResult.getFirstName();
                    lastname = personLookupResult.getLastName();

                }
            }

            sendMessage(firstname,lastname, output, eventOutput, personOutput, personLookupOutput);
        }
        private void sendMessage(String firstName, String lastName, String loginOutput, String eventResult, String personResult, String user) {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            if (firstName != null) {
                bundle.putString(FIRST_NAME,firstName);
                bundle.putString(LAST_NAME,lastName);
                bundle.putString(REGISTER, loginOutput);
                bundle.putString(EVENTS, eventResult);
                bundle.putString(PERSONS, personResult);
                bundle.putString(USER, user);
            }
            else {
                bundle.putString(FIRST_NAME,"Registration Failed");
                bundle.putString(LAST_NAME,"");
                bundle.putString(REGISTER, "");
                bundle.putString(EVENTS, "");
                bundle.putString(PERSONS, "");
                bundle.putString(USER, "");
            }
            message.setData(bundle);
            messageHandler.sendMessage(message);
            messageHandler.handleMessage(message);

        }
    }


    public boolean checkFilled(View view) {
        EditText serverHostField = (EditText) (view.findViewById(R.id.serverHostField));
        EditText serverPortField = (EditText) (view.findViewById(R.id.serverPortField));
        EditText userName = (EditText)(view.findViewById(R.id.userNameField));
        EditText password = (EditText)(view.findViewById(R.id.passwordField));
        EditText firstName = (EditText)(view.findViewById(R.id.firstNameField));
        EditText lastName = (EditText)(view.findViewById(R.id.lastNameField));
        EditText email = (EditText)(view.findViewById(R.id.emailField));
        RadioButton male = (RadioButton) (view.findViewById(R.id.radioMale));
        RadioButton female = (RadioButton) (view.findViewById(R.id.radioFemale));

        if(!serverHostField.getText().toString().equals("") && !serverPortField.getText().toString().equals("") && !userName.getText().toString().equals("") && !password.getText().toString().equals("") && !firstName.getText().toString().equals("") && !lastName.getText().toString().equals("") && !email.getText().toString().equals("") && (male.isChecked() || female.isChecked())) {
            Button registerButton = (Button)view.findViewById(R.id.registerButton);
            registerButton.setEnabled(true);
            Button loginButton = (Button)view.findViewById(R.id.signInButton);
            loginButton.setEnabled(true);
            return true;
        }
        if(!serverHostField.getText().toString().equals("") && !serverPortField.getText().toString().equals("") && !userName.getText().toString().equals("") && !password.getText().toString().equals("")) {
            Button loginButton = (Button)view.findViewById(R.id.signInButton);
            loginButton.setEnabled(true);
            Button registerButton = (Button)view.findViewById(R.id.registerButton);
            registerButton.setEnabled(false);
            return true;
        }
        Button registerButton = (Button)view.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        Button loginButton = (Button)view.findViewById(R.id.signInButton);
        loginButton.setEnabled(false);
        return false;
    }



}

