package kr.co.vilez.ui.share;

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
    private int i = 0;
    private ArrayList<ShareData> shareDatas;
    private ShareAdapter shareAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share);

        recyclerView = findViewById(R.id.rv);
        linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        shareDatas = new ArrayList<>();
        shareAdapter = new ShareAdapter(shareDatas);


        Button btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareData shareData = new ShareData(R.drawable.img_default_profile, "test"+i);
                shareDatas.add(shareData);
                shareAdapter.notifyDataSetChanged();
            }
        });
    }
}
