package com.example.summerdrawer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RVAdapter2 extends RecyclerView.Adapter<RVAdapter2.ViewHolder>{
    private ArrayList<String> titleList = null;
    private Context context;
    int res;


    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title_magazine;
        ImageView imageView7;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_magazine = itemView.findViewById(R.id.title_magazine);
            imageView7 = itemView.findViewById(R.id.imageView7);

            itemView.setOnClickListener(view->{
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    Intent ma = new Intent(context, MagazineActivity.class);
                    context.startActivity(ma);
                }
            });
        }
    }

    RVAdapter2(Context context, ArrayList list, int res){
        titleList = list;
        this.context = context;
        this.res = res;
    }

    @NonNull
    @Override
    public RVAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.rv_magazine_list, parent, false) ;
        RVAdapter2.ViewHolder vh = new RVAdapter2.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter2.ViewHolder holder, int position) {
        holder.imageView7.setImageResource(res);
        holder.title_magazine.setText(titleList.get(position));

        holder.imageView7.setOnClickListener(view->{
            Intent toM = new Intent(context, MagazineActivity.class);

        });
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

}
