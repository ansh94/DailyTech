package com.anshdeep.dailytech;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anshdeep.dailytech.api.model.Article;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private List<Article> listOfArticles;
    private Context mContext;


    public ArticlesAdapter(List<Article> articlesList, Context context) {
        this.listOfArticles = articlesList;
        this.mContext = context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news, parent, false);
        return new ArticleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

        //set thumbnail associated with the article
        Glide.with(mContext)
                .load(listOfArticles.get(position).getUrlToImage())
                .error(R.drawable.noimg)
                .thumbnail(0.1f) // if you pass 0.1f as the parameter, Glide will display the original image reduced to 10% of the size. If the original image has 1000x1000 pixels, the thumbnail will have 100x100 pixels
                .crossFade() //animation
                .into(holder.thumbnail);

        //set news title
        holder.title.setText(listOfArticles.get(position).getTitle());

        //set time
        holder.publishedTime.setText(Util.manipulateDateFormat(listOfArticles.get(position).getPublishedAt()));



//        //open full article description in url when title is clicked
//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri webpage = Uri.parse(listOfArticles.get(position).getUrl());
//                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
//                    mContext.startActivity(intent);
//                }
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return listOfArticles.size();
    }

    public void setDataAdapter(List<Article> newArticleList) {
        this.listOfArticles = newArticleList;
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.news_title) TextView title;
        @BindView(R.id.published_time) TextView publishedTime;


        public ArticleViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}
