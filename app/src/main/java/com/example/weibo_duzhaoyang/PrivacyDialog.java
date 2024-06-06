package com.example.weibo_duzhaoyang;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.weibo_duzhaoyang.utils.SharedPreferencesUtil;

public class PrivacyDialog extends Dialog implements View.OnClickListener {
    Context context;
    String title;
    SpannableStringBuilder content;

    String agreeText;
    String disagreeText;
    private TextView tvContent;

    private TextView tvTitle;

    private TextView tvAgree;

    private TextView tvDisagree;
    private DialogClickListener dialogClickListener;

    SharedPreferencesUtil spu = MyApplication.getSp();

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_agree:
                agree();
                break;
            case R.id.tv_disagree:
                disagree();
                break;
        }
    }

    private void disagree() {
        dismiss();
    }

    private void agree() {

        spu.putData("privacy_policy", true);
        dismiss();
    }


    public interface DialogClickListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }

    public void setDialogClickListener(DialogClickListener dialogClickListener) {
        this.dialogClickListener = dialogClickListener;
    }
    public PrivacyDialog(@NonNull Context context, String title, SpannableStringBuilder content, String agreeText, String disagreeText)  {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.agreeText = agreeText;
        this.disagreeText = disagreeText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.privacy_dialog, null);
        setContentView(view);

        tvTitle = findViewById(R.id.tv_dialog_title);
        tvContent = findViewById(R.id.tv_dialog_content);
        tvAgree = findViewById(R.id.tv_agree);
        tvDisagree = findViewById(R.id.tv_disagree);
        tvTitle = findViewById(R.id.tv_dialog_title);

        tvTitle.setText(title);
        tvAgree.setText(agreeText);
        tvDisagree.setText(disagreeText);
        tvContent.setText(content);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());

        tvAgree.setOnClickListener(v -> {
            if (dialogClickListener != null) {
                dialogClickListener.onPositiveButtonClick();
            }
        });
        tvDisagree.setOnClickListener(v -> {
            if (dialogClickListener != null) {
                dialogClickListener.onNegativeButtonClick();
            }
        });


    }

}
