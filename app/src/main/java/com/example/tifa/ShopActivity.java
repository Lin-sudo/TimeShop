package com.example.tifa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ShopActivity extends AppCompatActivity {

    public Button mBtn_learn;
    public ImageButton mBtn_plan, mBtn_entertainment;
    public ImageView mTv_shop;
    public EditText mEt_editMoney;
    public Button mBtn_editMoney;

    public int wantMoney = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        mBtn_learn = findViewById(R.id.btn_learn);
        mBtn_plan = findViewById(R.id.btn_plan);
        mBtn_entertainment = findViewById(R.id.btn_entertainment);
        mTv_shop = findViewById(R.id.iv_shop);
        mEt_editMoney = findViewById(R.id.et_editMoney);
        mBtn_editMoney = findViewById(R.id.btn_editMoney);


        Onclick onclick = new Onclick();
        mBtn_learn.setOnClickListener(onclick);
        mBtn_plan.setOnClickListener(onclick);
        mBtn_entertainment.setOnClickListener(onclick);
        mTv_shop.setOnClickListener(onclick);
        mBtn_editMoney.setOnClickListener(onclick);
    }

    class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_learn:
                    //builder模式
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShopActivity.this);
                    builder.setTitle("完成了学习任务").setMessage("学习了25分钟,金币+1")
                            .setPositiveButton("收下啦", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //shared获取共享参数 (读)
                                    SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                                    int money = sharedPreferences.getInt("money", 0); //如果没有则返回defValue
                                    money = money + 1;
                                    //shared更新共享参数
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove("money");
                                    editor.putInt("money", money);
                                    editor.commit();

                                    //把money传回MainActivity
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("money", Integer.toString(money));
                                    intent.putExtras(bundle);
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();

                                }
                            }).setNegativeButton("点错了", null).show();
                    break;
                case R.id.btn_plan:
                    //builder模式
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ShopActivity.this);
                    builder1.setTitle("完成了计划").setMessage("完成了每周计划,金币+3").setPositiveButton("收下啦", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //shared获取共享参数 (读)
                            SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                            int money = sharedPreferences.getInt("money", 0); //如果没有则返回defValue
                            money = money + 3;
                            //shared更新共享参数
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("money");
                            editor.putInt("money", money);
                            editor.commit();

                            //把money传回MainActivity
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("money", Integer.toString(money));
                            intent.putExtras(bundle);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }).setNegativeButton("点错了", null).show();
                    break;
                case R.id.btn_entertainment:
                    //builder模式
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ShopActivity.this);
                    builder2.setTitle("兑换娱乐时间").setMessage("6金币换40分钟的娱乐时间$v$").setPositiveButton("开搞", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //shared获取共享参数 (读)
                            SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                            int money = sharedPreferences.getInt("money", 0); //如果没有则返回defValue
                            money = money - 6;
                            //shared更新共享参数
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("money");
                            editor.putInt("money", money);
                            editor.commit();

                            //把money传回MainActivity
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("money", Integer.toString(money));
                            intent.putExtras(bundle);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    }).setNegativeButton("就不玩", null).show();
                    break;
                case R.id.iv_shop:
                    ToastUtil.showMsg(ShopActivity.this,"再怎么点也不会给你优惠的");
                    break;
                case R.id.btn_editMoney:
                    //builder模式
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(ShopActivity.this);
                    builder3.setTitle("自定义").setMessage("想要"+mEt_editMoney.getText().toString()+"金币吗？")
                            .setPositiveButton("收下啦", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String temp = mEt_editMoney.getText().toString();
                                    if(!temp.isEmpty()) {
                                        wantMoney = Integer.parseInt(temp);
                                    }
                                    //shared获取共享参数 (读)
                                    SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                                    int money = sharedPreferences.getInt("money", 0); //如果没有则返回defValue
                                    money = money + wantMoney;
                                    //shared更新共享参数
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove("money");
                                    editor.putInt("money", money);
                                    editor.commit();

                                    //把money传回MainActivity
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("money", Integer.toString(money));
                                    intent.putExtras(bundle);
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();

                                }
                            }).setNegativeButton("点错了", null).show();
                    break;
            }
        }
    }
}
