package com.example.tifa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.OnTwoLevelListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.TwoLevelHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public Button mBtn_jia;
    public TextView mTv_money;
    public ImageView mIv_charater;
    public TextView mTv_dateName,mTv_dateDescription,mTv_dateEncouragement;

    public short times = 0;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn_jia = findViewById(R.id.btn_jia);
        mTv_money = findViewById(R.id.tv_money);
        mIv_charater = findViewById(R.id.iv_charater);
        mTv_dateName = findViewById(R.id.tv_dateName);
        mTv_dateDescription = findViewById(R.id.tv_dateDescription);
        mTv_dateEncouragement = findViewById(R.id.tv_dateEncouragement);
        TwoLevelHeader header = findViewById(R.id.header);
        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);

        //初始化一下money，注意：将getSharedPreferences用在onCreate外会闪退
        shared = this.getSharedPreferences("share", Context.MODE_PRIVATE);

        //----roll
        new checkdate().setText(); //设置二楼的文本、访问数据库、弹窗
        //----

        //更新界面上的money
        int money = shared.getInt("money", 0);
        mTv_money.setText(Integer.toString(money)); //设置money 初值



        mBtn_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        mIv_charater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                times++;
                if(times%3==0){
                    ToastUtil.showMsg(MainActivity.this, "ヾ(•ω•`)o");
                }
                else if(times%3==1){
                    ToastUtil.showMsg(MainActivity.this, "ο(=•ω＜=)ρ⌒☆");
                }
                else if(times%3==2){
                    ToastUtil.showMsg(MainActivity.this, "(╯‵□′)╯︵┻━┻");
                }
                if(times>3)
                    times/=3;
            }
        });

        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener(){
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
            }
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
            }
            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                if (oldState == RefreshState.TwoLevel){
                    findViewById(R.id.second_floor_content).animate().alpha(0).setDuration(1000); //alpha0元件完全透明
                }
            }
        });
        header.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                findViewById(R.id.second_floor_content).animate().alpha(1).setDuration(2000);
                return true;
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            String newmoney = data.getStringExtra("money");
            mTv_money.setText(newmoney);    //更新money
        }
    }

    private class checkdate {

        //SharedPreferences shared = MainActivity.this.getSharedPreferences("share", Context.MODE_PRIVATE);
        int roll = 0;

        boolean judgeDate() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date(System.currentTimeMillis());
            String str_date = simpleDateFormat.format(date);

            String str_date_lib = shared.getString("date", "");
            if(str_date.equals(str_date_lib)){
                return true;
            }
            else {
                //保存新日期到数据库
                SharedPreferences.Editor editor = shared.edit();
                editor.remove("date");
                editor.putString("date", str_date);
                editor.commit();

                //builder模式
                AlertDialog.Builder builder0 = new AlertDialog.Builder(MainActivity.this);
                builder0.setTitle("新的一天开始啦").setMessage("今天是 "+str_date+" 快去二楼看看今天的运气吧^v^")
                        .setPositiveButton("好的",null).show();
                return false;
            }
        }

        void changeMoney(int num){ //修改金额
            int current_money = shared.getInt("money", 0);
            SharedPreferences.Editor editor = shared.edit();
            editor.remove("money");
            editor.putInt("money", current_money + num);
            editor.commit();    //保存到数据库
        }

        void setText(){
            roll = shared.getInt("roll",0);
            if(!judgeDate()) {    //新的日期，重新roll
                roll = (int) (Math.random() * 100);
                SharedPreferences.Editor editor = shared.edit();
                editor.remove("roll");
                editor.putInt("roll", roll);
                editor.commit();    //保存到数据库
                if (roll >= 0 && roll < 40) {                         //40%
                } else if (roll >= 40 && roll < 70) {                        //30%
                    changeMoney(-1);
                } else if (roll >= 70 && roll < 95) {                       //25%
                    changeMoney(+1);
                } else if (roll >= 95 && roll < 97) {
                    changeMoney(-2);
                } else if (roll >= 97 && roll < 99) {
                    changeMoney(+2);
                } else if (roll >= 99) {
                    changeMoney(+3);
                }
            }

            //设置二楼的文本
            if (roll >= 0 && roll < 40) {                         //40%
                mTv_dateName.setText("今天是学习日");
                mTv_dateDescription.setText("+0 金币");
                mTv_dateEncouragement.setText("在最平凡的日子里，也能熠熠生辉");
            } else if (roll >= 40 && roll < 70) {                        //30%
                mTv_dateName.setText("今天是奋斗日");
                mTv_dateDescription.setText("-1 金币");
                mTv_dateEncouragement.setText("学如逆水行舟，不进则退。请记得生于忧患，死于安乐。若是还有机会，就放手一搏吧");
            } else if (roll >= 70 && roll < 95) {                       //25%
                mTv_dateName.setText("今天是幸运日");
                mTv_dateDescription.setText("+1 金币");
                mTv_dateEncouragement.setText("一个人幸运的前提，其实是他有能力改变自己。幸运的你要继续加油啊");
            } else if (roll >= 95 && roll < 97) {
                mTv_dateName.setText("今天是警惕日");
                mTv_dateDescription.setText("-2 金币");
                mTv_dateEncouragement.setText("敲响警钟，铭记使命！");
            } else if (roll >= 97 && roll < 99) {
                mTv_dateName.setText("今天是惊喜日");
                mTv_dateDescription.setText("+2 金币");
                mTv_dateEncouragement.setText("不管过去如何，过去的已经过去，最好的总在未来等着你，继续努力吧");
            } else if (roll >= 99) {
                mTv_dateName.setText("哇哦今天是属于你的日子呀");
                mTv_dateDescription.setText("+3 金币");
                mTv_dateEncouragement.setText("愿你有诗，有梦，有坦荡荡的远方，敬往事一杯酒，过去不回头，未来不将就。");
            }
        }

    }
}
