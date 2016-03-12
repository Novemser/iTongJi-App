package com.example.aitongji.Section_Information;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aitongji.Home.MainActivity;
import com.example.aitongji.R;
import com.example.aitongji.Service.CardRestNotice;

/**
 * Created by Novemser on 2/25/2016.
 */
public class Card_Rest_Notice extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_card_rest);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SwitchPreference switchPreference = (SwitchPreference) getPreferenceManager().findPreference("service_state");
        final EditTextPreference editTextPreference = (EditTextPreference) getPreferenceManager().findPreference("card_rest_value_text");
        if (!switchPreference.isChecked()) {
            editTextPreference.setEnabled(false);
        } else {
            editTextPreference.setEnabled(true);
        }
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isSelected = Boolean.parseBoolean(newValue.toString());
                if (isSelected) {
                    Intent serviceIntent = new Intent(MainActivity.getContext(), CardRestNotice.class);

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("value", editTextPreference.getText());
                    editor.apply();

                    MainActivity.getContext().startService(serviceIntent);
                    editTextPreference.setEnabled(true);
                } else {
                    editTextPreference.setEnabled(false);
                    CardRestNotice.cleanAllNotification();
                }
                return true;
            }
        });

        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent serviceIntent = new Intent(MainActivity.getContext(), CardRestNotice.class);
                getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit().putString("value", newValue.toString()).apply();
                getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit().putBoolean("FORCE_REFRESH", true).apply();
                MainActivity.getContext().startService(serviceIntent);
                return true;
            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
