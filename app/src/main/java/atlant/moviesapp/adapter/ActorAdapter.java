package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Review;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 17.04.2017..
 */

public class ActorAdapter   extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder>  {
    private List<Cast> cast;
    private int rowLayout;
    private Context mcontext;
    public static class ActorViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.actor_item)
        LinearLayout reviewLayout;

        @BindView(R.id.actor_image)
        ImageView image;

        @BindView(R.id.actor_name)
        TextView name;

        @BindView(R.id.actor_role)
        TextView role;


        public ActorViewHolder(View v)
        {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public ActorAdapter(List<Cast> cast, int rowLayout, Context mcontext) {
        this.cast = cast;
        this.rowLayout = rowLayout;
        this.mcontext = mcontext;
    }
    @Override
    public ActorAdapter.ActorViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(rowLayout,parent,false);
        return new ActorAdapter.ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActorAdapter.ActorViewHolder holder, final int position)
    {
        holder.name.setText(cast.get(position).getName());
        holder.role.setText(cast.get(position).getCharacter());
        Glide.with(mcontext).load(cast.get(position).getImagePath())
                .crossFade().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);



    }
    @Override
    public int getItemCount()
    {return cast.size();}
}
