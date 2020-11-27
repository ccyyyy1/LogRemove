package com.gavin.asmdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TAG", "MainActivity中的onCreate");
        test1();
    }

    public void toSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public void test1() {
        Log.i("111111", "222222" + Log.i("333", "444"));
        View v1;
//        Log.i("aaa", "bbb" + (v1 = this.findViewById(R.id.tv1)));
//        if(v1 != null)
//            v1.requestLayout();
    }
}
