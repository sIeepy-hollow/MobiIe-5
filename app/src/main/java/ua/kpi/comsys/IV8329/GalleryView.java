package ua.kpi.comsys.IV8329;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class GalleryView extends RecyclerView.Adapter<GalleryView.ViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<ArrayList<Bitmap>> pictures = new ArrayList<>();
    private int maxWidth;
    private static int curr_img = 0;

    public GalleryView(Context context, int width) {
        this.inflater = LayoutInflater.from(context);
        this.maxWidth = (int) (width - 20*((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gallery_section, parent, false);
        view.getResources();
        return new GalleryView.ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView[] imgs = new ImageView[8];
        ViewHolder(View view){
            super(view);
            imgs[0] = view.findViewById(R.id.img_1);
            imgs[1] = view.findViewById(R.id.img_2);
            imgs[2] = view.findViewById(R.id.img_3);
            imgs[3] = view.findViewById(R.id.img_4);
            imgs[4] = view.findViewById(R.id.img_5);
            imgs[5] = view.findViewById(R.id.img_6);
            imgs[6] = view.findViewById(R.id.img_7);
            imgs[7] = view.findViewById(R.id.img_8);
        }
    }

    @Override
    public void onBindViewHolder(GalleryView.ViewHolder holder, int position) {
        ArrayList<Bitmap> section_pics = pictures.get(position);
        for (int i=0; i<section_pics.size(); i++) {
            holder.imgs[i].setImageBitmap(section_pics.get(i));
            holder.imgs[i].getLayoutParams().width = maxWidth/4;
            holder.imgs[i].getLayoutParams().height = maxWidth/4;
        }
        if (section_pics.size() > 1) {
            holder.imgs[1].getLayoutParams().width = 3*maxWidth/4;
            holder.imgs[1].getLayoutParams().height = 3*maxWidth/4;
        }
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addImg(Bitmap bitmap) {
        if (curr_img == 8 || pictures.size()==0) {
            pictures.add(new ArrayList<>());
            curr_img = 0;
        }
        pictures.get(pictures.size()-1).add(bitmap);
        curr_img++;
        this.notifyDataSetChanged();
    }


    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
}
