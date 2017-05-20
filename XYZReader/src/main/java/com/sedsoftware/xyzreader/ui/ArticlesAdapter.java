package com.sedsoftware.xyzreader.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sedsoftware.xyzreader.R;
import com.sedsoftware.xyzreader.data.model.Article;
import com.sedsoftware.xyzreader.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

  private List<Article> articles = new ArrayList<>();

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_article, parent, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bindTo(articles.get(position));
  }

  @Override
  public int getItemCount() {
    return articles.size();
  }

  @Override
  public long getItemId(int position) {
    return articles.get(position).id();
  }

  public void addArticle(Article article) {
    articles.add(article);
    notifyItemInserted(articles.size() - 1);
  }

  public void clearList() {
    articles.clear();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_thumbnail)
    ImageView thumbnailView;
    @BindView(R.id.item_article_title)
    TextView titleView;
    @BindView(R.id.item_article_subtitle)
    TextView subtitleView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    void bindTo(Article article) {

      String title = article.title();
      String rawDate = article.published_date();
      String author = article.author();

      titleView.setText(title);
      subtitleView.setText(StringUtils.getFormattedSubtitle(rawDate, author));

      String thumbnailUrl = article.thumb();

      Glide.clear(thumbnailView);

      Glide.clear(thumbnailView);
      Glide.with(thumbnailView.getContext())
          .load(thumbnailUrl)
          .fitCenter()
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(thumbnailView);
    }
  }
}
