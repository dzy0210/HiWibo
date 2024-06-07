package com.example.weibo_duzhaoyang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.example.weibo_duzhaoyang.utils.SharedPreferencesUtil;

public class HelloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        boolean privacyPolicy = (Boolean) MyApplication.getSp().getData("privacy_policy", false);
        if (!privacyPolicy) {
            showPrivacyDialog(this);
        } else {
            startActivity(new Intent(HelloActivity.this, MainActivity.class));
            finish();
        }
    }

    private void showPrivacyDialog(Context context) {
        String title = "声明与条款";
        String agreeText = "同意并使用";
        String disagreeText = "不同意";
        String content = "欢迎使用 iH微博 ，我们将严格遵守相关法律和隐私政策保护您的个人隐私，请您阅读并同意《用户协议》与《隐私政策》";
        int index1 = content.indexOf("《");
        int index2 = content.lastIndexOf("《");
        int length1 = "《用户协议》".length();
        int length2 = "《隐私政策》".length();
        SpannableStringBuilder dialogContentStyle = new SpannableStringBuilder(content);
        dialogContentStyle.setSpan(new ForegroundColorSpan(Color.parseColor("#62839a")), index1, index1+length1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        dialogContentStyle.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(HelloActivity.this, "点击了《用户协议》", Toast.LENGTH_SHORT).show();
            }
        }, index1, index1+length1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        dialogContentStyle.setSpan(new ForegroundColorSpan(Color.parseColor("#62839a")), index2, index2+length2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        dialogContentStyle.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(HelloActivity.this, "点击了《隐私政策》", Toast.LENGTH_SHORT).show();
            }
        }, index1, index2+length2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        PrivacyDialog privacyDialog = new PrivacyDialog(this, title, dialogContentStyle, agreeText, disagreeText);
        privacyDialog.setDialogClickListener(new PrivacyDialog.DialogClickListener() {
            @Override
            public void onPositiveButtonClick() {
                privacyDialog.dismiss();
                MyApplication.getSp().putData("privacy_policy", true);

                startActivity(new Intent(HelloActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onNegativeButtonClick() {

                privacyDialog.dismiss();
                finish();
            }
        });
        privacyDialog.show();


    }
}