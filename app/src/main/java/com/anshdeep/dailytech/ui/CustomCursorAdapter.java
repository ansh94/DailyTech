package com.anshdeep.dailytech.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anshdeep.dailytech.R;
import com.anshdeep.dailytech.api.model.Article;
import com.anshdeep.dailytech.data.ArticleContract;
import com.anshdeep.dailytech.util.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ANSHDEEP on 23-03-2017.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.FavoriteArticleViewHolder> {


    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;
    private ArrayList<Article> mArticlesList;

    private final CustomCursorAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface CustomCursorAdapterOnClickHandler {
        void onFavoriteArticleClick(Article article);
    }


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public CustomCursorAdapter(Context mContext, CustomCursorAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        mClickHandler = clickHandler;
        mArticlesList = new ArrayList<>();
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public FavoriteArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_news, parent, false);

        return new FavoriteArticleViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(FavoriteArticleViewHolder holder, int position) {

        if (mCursor != null && getItemCount() > 0) {


            int articleImageIndex = mCursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_IMAGE_URL);
            int articleTitleIndex = mCursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_TITLE);
            int articlePublishedIndex = mCursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_TIME);

            mCursor.moveToPosition(position); // get to the right location in the cursor

            String imageUrl = mCursor.getString(articleImageIndex);
            String title = mCursor.getString(articleTitleIndex);
            String publishTime = mCursor.getString(articlePublishedIndex);

            //set thumbnail associated with the article
            Glide.with(mContext)
                    .load(imageUrl)
                    .error(R.drawable.noimg)
                    .into(holder.thumbnail);

            //set news title
            holder.title.setText(title);

            //set time
            holder.publishedTime.setText(CommonUtils.manipulateDateFormat(publishTime));
        }
    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();

    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }


        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned
        this.mArticlesList.clear();

        //check if this is a valid cursor, then update the cursor
        if (c != null && c.moveToFirst()) {
            int articleAuthorIndex = c.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_AUTHOR);
            int articleTitleIndex = c.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_TITLE);
            int articleDescriptionIndex = c.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_DESCRIPTION);
            int articleUrlIndex = c.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_URL);
            int articleUrlImageIndex = c.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_IMAGE_URL);
            int articlePublishedIndex = c.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_TIME);
            int articleSourceIndex = c.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_SOURCE);

            do {

                // Determine the values of the wanted data
                String articleAuthor = c.getString(articleAuthorIndex);
                String articleTitle = c.getString(articleTitleIndex);
                String articleDescription = c.getString(articleDescriptionIndex);
                String articleUrl = c.getString(articleUrlIndex);
                String articleUrlImage = c.getString(articleUrlImageIndex);
                String articlePublished = c.getString(articlePublishedIndex);
                String articleSource = c.getString(articleSourceIndex);

                Article article = new Article();
                article.setAuthor(articleAuthor);
                article.setTitle(articleTitle);
                article.setDescription(articleDescription);
                article.setUrl(articleUrl);
                article.setUrlToImage(articleUrlImage);
                article.setPublishedAt(articlePublished);
                article.setSource(articleSource);
                mArticlesList.add(article);
            }
            while (c.moveToNext());

            this.notifyDataSetChanged();

        }
        return temp;
    }


    // Inner class for creating ViewHolders
    class FavoriteArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.news_title) TextView title;
        @BindView(R.id.published_time) TextView publishedTime;


        public FavoriteArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Article article = mArticlesList.get(clickedPosition);
            mClickHandler.onFavoriteArticleClick(article);
        }
    }
}
