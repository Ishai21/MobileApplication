package edu.cs478.project1.phoneformat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQ_PHONE_ENTRY = 1001;
    private static final String STATE_LAST_PHONE_DIGITS = "last_phone_digits";

    private Button enterPhoneButton;
    private Button openDialerButton;
    private TextView welcomeText;

    // 10 digits, e.g. "3125551212"
    private String lastPhoneDigits = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        welcomeText = findViewById(R.id.welcomeText);
        enterPhoneButton = findViewById(R.id.enterPhoneButton);
        openDialerButton = findViewById(R.id.openDialerButton);

        if (savedInstanceState != null) {
            lastPhoneDigits = savedInstanceState.getString(STATE_LAST_PHONE_DIGITS);
        }
        updateUiState();

        enterPhoneButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, PhoneEntryActivity.class);
            startActivityForResult(i, REQ_PHONE_ENTRY);
        });

        openDialerButton.setOnClickListener(v -> {
            if (lastPhoneDigits == null) {
                toast(getString(R.string.toast_protocol_enter_first));
                return;
            }
            openDialerWithNumber(lastPhoneDigits);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_LAST_PHONE_DIGITS, lastPhoneDigits);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQ_PHONE_ENTRY) {
            return;
        }

        if (resultCode == RESULT_OK && data != null) {
            String digits = data.getStringExtra(PhoneEntryActivity.EXTRA_PHONE_DIGITS);
            if (digits != null && digits.matches("^\\d{10}$")) {
                lastPhoneDigits = digits;
                toast(getString(R.string.toast_saved_number, lastPhoneDigits));
            } else {
                toast(getString(R.string.toast_invalid_return));
            }
        } else {
            // User backed out or entered an invalid number and chose to return
            toast(getString(R.string.toast_no_number_saved));
        }

        updateUiState();
    }

    private void updateUiState() {
        boolean hasNumber = (lastPhoneDigits != null);
        openDialerButton.setEnabled(hasNumber);
        if (hasNumber) {
            welcomeText.setText(getString(R.string.welcome_with_number, formatDigitsForDisplay(lastPhoneDigits)));
        } else {
            welcomeText.setText(getString(R.string.welcome));
        }
    }

    private void openDialerWithNumber(String digits) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + digits));
        if (dialIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(dialIntent);
        } else {
            toast(getString(R.string.toast_no_phone_app));
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private static String formatDigitsForDisplay(String digits) {
        // digits is 10 digits
        return "(" + digits.substring(0, 3) + ") " + digits.substring(3, 6) + "-" + digits.substring(6);
    }
}

