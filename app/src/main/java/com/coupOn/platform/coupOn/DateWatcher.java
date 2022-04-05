package com.coupOn.platform.coupOn;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateWatcher implements TextWatcher
{

    private static int [] separators = null;
    private static final char SEP = '-';
    private static final String FORMAT = "DD-MM-YYYY";
    private boolean userChange;
    private String text;
    private static final Pattern regex = Pattern.compile("((3[01]|[12][0-9]|0[1-9])-(0[13578]|1[02])-\\d{4})|((30|[12][0-9]|0[1-9])-(0[469]|11)-\\d{4})|(29-02-((\\d{2}([2468][048]|0[48]|[13579][26]))|(([02468][048]|[13579][26])00)))|((0[1-9]|1[0-9]|2[0-8])-02-\\d{4})");

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
        Matcher matcher = regex.matcher(date);
        return matcher.find();
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