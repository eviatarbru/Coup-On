package com.shashank.platform.coup_on;

import android.text.Editable;
import android.text.TextWatcher;

public class DateWatcher implements TextWatcher
{

    private static int [] separators = null;
    private static final char SEP = '-';
    private static final String FORMAT = "DD-MM-YYYY";
    private boolean userChange;
    private String text;

    public DateWatcher()
    {
        if (separators == null)
        {
            separators = new int[2];
            separators[0] = FORMAT.indexOf(SEP);
            separators[1] = FORMAT.indexOf(SEP, separators[0] + 1) - 1;
        }
        this.userChange = true;
        this.text = "";
    }

    public static boolean isDate(String date)
    {
        if (date.length() != FORMAT.length())
        {
            return false;
        }
        // For now, should expand the check to the years, months and days
        return true;
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
    public void afterTextChanged(Editable editable)
    {
        if ((this.userChange))
        {
            this.userChange = false;
            this.text = editable.toString().replaceAll("[^\\d.]", "");
            //this.text = Methods.viewString(this.view);
            editable.clear();
            int length = this.text.length();
            if (length > separators[0])
            {
                editable.append(this.text.substring(0, separators[0]) + SEP);
                if (length > separators[1])
                {
                    editable.append(this.text.substring(separators[0], separators[1]) + SEP);
                    editable.append(this.text.substring(separators[1]));
                }
                else
                {
                    editable.append(this.text.substring(separators[0]));
                }
            }
            else
            {
                editable.append(this.text);
            }
            //editable.append(this.text);
            this.userChange = true;
        }
    }
    //byle
}