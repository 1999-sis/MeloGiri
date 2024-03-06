package com.example.melogiri.controller;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.melogiri.util.SocketAPI;
import com.example.melogiri.view.LoginActivity;
import com.example.melogiri.view.RegisterActivity;


public class ControllerRegister
{
    SocketAPI socket = new SocketAPI("209.38.244.243",8080);
    public void register(RegisterActivity registerActivity, String nome, String cognome, String data, String email, String password)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run() {
                try
                {
                    String response = socket.register(nome, cognome, data, email, password);
                    Log.d("REGISTER_UTENTE", response);
                    if(response.equalsIgnoreCase("Registration_Successful"))
                    {
                        registerActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Mostra un Toast per indicare la registrazione avvenuta con successo
                                Toast.makeText(registerActivity, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();

                                // Avvia un'Intent per tornare all'Activity precedente
                                Intent intent = new Intent(registerActivity, LoginActivity.class);
                                registerActivity.startActivity(intent);

                                // Chiudi l'Activity corrente
                                registerActivity.finish();
                            }
                        });
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    public void showBiometricPrompt(LoginActivity loginActivity)
    {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setDescription("Please authenticate with your biometrics to continue")
                .setDeviceCredentialAllowed(true)
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(loginActivity,
                ContextCompat.getMainExecutor(loginActivity),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result)
                    {
                        super.onAuthenticationSucceeded(result);
                    }

                    @Override
                    public void onAuthenticationFailed()
                    {
                        super.onAuthenticationFailed();
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
}
