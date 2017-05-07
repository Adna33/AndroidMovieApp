package atlant.moviesapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import atlant.moviesapp.R;
import atlant.moviesapp.model.NavItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 03.05.2017..
 */

public class DrawerItemCustomAdapter extends ArrayAdapter<NavItem> {

    @BindView(R.id.imageViewIcon)
    ImageView icon;

    @BindView(R.id.textViewName)
    TextView name;

    Context mContext;
    int layoutResourceId;
    NavItem data[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, NavItem[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
        ButterKnife.bind(this,listItem);


        NavItem folder = data[position];


        icon.setImageResource(folder.icon);
        name.setText(folder.name);

        return listItem;
    }
}

