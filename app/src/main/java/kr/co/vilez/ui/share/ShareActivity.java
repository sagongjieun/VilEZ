package kr.co.vilez.ui.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.vilez.R;


public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "빌리지_ShareActivity";

    private int i = 0;
    private ArrayList<ShareData> shareDatas;
    private ShareAdapter shareAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // 데이터 가져오기
        shareDatas = new ArrayList<ShareData>();
        shareDatas.add(new ShareData(R.drawable.img_default_profile, "test1"));
        shareDatas.add(new ShareData(R.drawable.img_default_profile, "test2"));
        shareDatas.add(new ShareData(R.drawable.img_default_profile, "test3"));

        // 어댑터 생성
        shareAdapter = new ShareAdapter(shareDatas);

        // 리사이클러뷰에 어댑터 등록
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(shareAdapter);

        Log.d(TAG, "onCreate: asdf ShareActivity ");

        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                ShareData shareData = new ShareData(R.drawable.img_default_profile, "test"+i);
                shareDatas.add(shareData);
                shareAdapter.notifyDataSetChanged();
            }
        });
    }


}
