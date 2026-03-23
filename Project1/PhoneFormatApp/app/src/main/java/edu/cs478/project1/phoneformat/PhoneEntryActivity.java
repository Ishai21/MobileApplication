package edu.cs478.project1.phoneformat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneEntryActivity extends Activity {

    public static final String EXTRA_PHONE_DIGITS = "edu.cs478.project1.phoneformat.EXTRA_PHONE_DIGITS";

    // Legal formats:
    // 1) 3125551212
    private static final String RE_PLAIN_10 = "^\\d{10}$";
    // 2) (312) 555-1212  OR (312)555-1212
    private static final String RE_PARENS = "^\\(\\d{3}\\)\\s?\\d{3}-\\d{4}$";

    private EditText phoneEditText;
    private Button submitButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_entry);

        phoneEditText = findViewById(R.id.phoneEditText);
        submitButton = findViewById(R.id.submitButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Required by assignment: implement TextView.OnEditorActionListener for EditText
        phoneEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptSubmit();
                    return true;
                }
                // Some keyboards send ENTER instead of IME_ACTION_DONE
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    attemptSubmit();
                    return true;
                }
                return false;
            }
        });

        submitButton.setOnClickListener(v -> attemptSubmit());
        cancelButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void attemptSubmit() {
        String input = phoneEditText.getText().toString().trim();

        if (!isLegalPhoneNumber(input)) {
            toast(getString(R.string.toast_invalid_format));
            return;
        }

        String digits = input.replaceAll("\\D", "");
        if (!digits.matches("^\\d{10}$")) {
            toast(getString(R.string.toast_invalid_format));
            return;
        }

        Intent result = new Intent();
        result.putExtra(EXTRA_PHONE_DIGITS, digits);
        setResult(RESULT_OK, result);
        finish();
    }

    private static boolean isLegalPhoneNumber(String s) {
        return s.matches(RE_PLAIN_10) || s.matches(RE_PARENS);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

