package ua.kpi.comsys.IV8329;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ua.kpi.comsys.IV8329.R;
import ua.kpi.comsys.IV8329.GalleryView;

public class GalleryItem extends Fragment {

    private static RecyclerView recyclerView;
    private static GalleryView adapter;
    private static FloatingActionButton addImgBtn;
    private final int GALLERY_REQUEST = 1;
    private static int curr_img;
    private static int screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) requireContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;

        recyclerView = root.findViewById(R.id.gallery);
        if (adapter == null) {
            adapter = new GalleryView(this.getContext(), screenWidth);
        } else {
            adapter.setMaxWidth(screenWidth);
            adapter.notifyDataSetChanged();
        }
        recyclerView.setAdapter(adapter);

        addImgBtn = root.findViewById(R.id.addImgBtn);
        addImgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        adapter.addImg(bitmap);
                    } catch (Exception e) {

                    }
                    break;
            }
    }

}
