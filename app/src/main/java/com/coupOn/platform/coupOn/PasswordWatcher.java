package com.coupOn.platform.coupOn;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.shashank.platform.coup_on.R;

public class PasswordWatcher implements TextWatcher
{
    static final int NUMBERS_START = 48;
    static final int NUMBERS_END   = 57;
    static final int UPPERS_START  = 65;
    static final int UPPERS_END    = 90;
    static final int LOWERS_START  = 97;
    static final int LOWERS_END    = 122;
    
    private EditText watched;
    private Context context;

    private Drawable lockRed;
    private Drawable lockYellow;
    private Drawable lockGreen;
    private Drawable lockWhite;

    
    public PasswordWatcher(EditText view, Context context)
    {
        this.watched = view;
        this.context = context;
        this.lockRed = this.context.getDrawable(R.drawable.ic_lock_red_24dp);
        this.lockRed.setBounds(0, 0, 60, 60);
        this.lockYellow = this.context.getDrawable(R.drawable.ic_lock_yellow_24dp);
        this.lockYellow.setBounds(0, 0, 60, 60);
        this.lockGreen =  this.context.getDrawable(R.drawable.ic_lock_green_24db);
        this.lockGreen.setBounds(0, 0, 60, 60);
        this.lockWhite = this.context.getDrawable(R.drawable.ic_lock_white_24dp);
        this.lockWhite.setBounds(0, 0, 60, 60);
    }
    
    
    
    public static int passwordStrength(String password)
    {
        int numbers = 0;
        int uppers  = 0;
        int lowers  = 0;
        int symbols = 0, temp;
        if(password.trim().length() < 6)
            return -1;
        for(char ch: password.toCharArray())
        {
            temp = (int)ch;
            if(temp >= PasswordWatcher.NUMBERS_START && temp <= PasswordWatcher.NUMBERS_END)
                numbers++;
            else if(temp >= PasswordWatcher.UPPERS_START && temp <= PasswordWatcher.UPPERS_END)
                uppers++;
            else if(temp >= PasswordWatcher.LOWERS_START && temp <= PasswordWatcher.LOWERS_END)
                lowers++;
            else
                symbols++;
        }
        boolean numbersFlag = numbers > 0;
        boolean uppersFlag = uppers > 0;
        boolean lowersFlag = lowers > 0;
        boolean symbolsFlag = symbols > 0;
        if (symbolsFlag && !uppersFlag)
        {
            uppersFlag = true;
            symbolsFlag = false;
        }
        else if (symbolsFlag && !lowersFlag)
        {
            lowersFlag = true;
            symbolsFlag = false;
        }

        switch (password.length())
        {//Number Of Letters checker.
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return 0;
            case 5:
                if (uppersFlag && lowersFlag && numbersFlag)
                    return 1;
                return 0;
            case 6:
            case 7:
                if (uppersFlag && lowersFlag)
                    return 1;
                return 0;
            case 8:
                if (uppersFlag || lowersFlag)
                {
                    if (numbersFlag && uppersFlag && lowersFlag && symbolsFlag)
                        return 2;
                    return 1;
                }
                return 0;
            case 9:
                if (numbersFlag && uppersFlag && lowersFlag)
                    return 2;
                return 1;
            case 10:
                if (uppersFlag && lowersFlag)
                    return 2;
                return 1;
            case 11:
            case 12:
            case 13:
            case 14:
                if (uppersFlag || lowersFlag)
                    return 2;
                return 1;
            default:
                return 2;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence sequence, int startIndex, int removed, int added)
    {
    }

    @Override
    public void onTextChanged(CharSequence sequence, int startIndex, int removed, int added)
    {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        switch (passwordStrength(editable.toString()))
        {
            case 0:
                this.watched.setCompoundDrawables(null, null, this.lockRed, null);
                break;
            case 1:
                this.watched.setCompoundDrawables(null, null, this.lockYellow, null);
                break;
            case 2:
                this.watched.setCompoundDrawables(null, null, this.lockGreen, null);
                break;
            default:
                this.watched.setCompoundDrawables(null, null, this.lockWhite, null);
                break;

        }
    }
}
