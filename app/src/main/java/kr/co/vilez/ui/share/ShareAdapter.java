package kr.co.vilez.ui.share;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.vilez.R;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.CustomViewHolder>{
    private ArrayList<String> localDataSet;

    private ArrayList<ShareData> shareDatas;

    //----- 생성자 --------------------------------------
    // 생성자를 통해서 데이터를 전달받도록 함
    public ShareAdapter (ArrayList<ShareData> dataSet) {
        shareDatas = dataSet;
    }
    //--------------------------------------------------

    @NonNull
    @Override   // ViewHolder 객체를 생성하여 리턴한다.
    public ShareAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        ShareAdapter.CustomViewHolder viewHolder = new ShareAdapter.CustomViewHolder(view);

        return viewHolder;
    }

    @Override   // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    public void onBindViewHolder(@NonNull ShareAdapter.CustomViewHolder holder, int position) {
        holder.iv_profile.setImageResource(shareDatas.get(position).getIv_profile());
        holder.tv_name.setText(shareDatas.get(position).getTv_name());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curName = holder.tv_name.getText().toString();
                Toast.makeText(view.getContext(), curName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override   // 전체 데이터의 갯수를 리턴한다.
    public int getItemCount() {

        return null != shareDatas ?  shareDatas.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected ImageView iv_profile;
        protected TextView tv_name;

        public CustomViewHolder(
                View itemView) {
            super(itemView);
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_profile);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}