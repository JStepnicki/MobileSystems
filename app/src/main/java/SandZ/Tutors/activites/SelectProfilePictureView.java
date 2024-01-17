package SandZ.Tutors.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import SandZ.Tutors.R;
import SandZ.Tutors.database_handlers.FirebaseManager;

public class SelectProfilePictureView extends AppCompatActivity {
    private FirebaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_profile_picture_view);
        manager = new FirebaseManager(this);

        ImageView imageChoose1 = findViewById(R.id.imageChoose1);
        ImageView imageChoose2 = findViewById(R.id.imageChoose2);
        ImageView imageChoose3 = findViewById(R.id.imageChoose3);
        ImageView imageChoose4 = findViewById(R.id.imageChoose4);
        ImageView imageChoose5 = findViewById(R.id.imageChoose5);
        ImageView imageChoose6 = findViewById(R.id.imageChoose6);
        ImageView imageChoose7 = findViewById(R.id.imageChoose7);
        ImageView imageChoose8 = findViewById(R.id.imageChoose8);
        ImageView imageChoose9 = findViewById(R.id.imageChoose9);
        ImageView imageChoose10 = findViewById(R.id.imageChoose10);
        ImageView imageChoose11 = findViewById(R.id.imageChoose11);
        ImageView imageChoose12 = findViewById(R.id.imageChoose12);

        setOnClickListenerForImage(imageChoose1, 1);
        setOnClickListenerForImage(imageChoose2, 2);
        setOnClickListenerForImage(imageChoose3, 3);
        setOnClickListenerForImage(imageChoose4, 4);
        setOnClickListenerForImage(imageChoose5, 5);
        setOnClickListenerForImage(imageChoose6, 6);
        setOnClickListenerForImage(imageChoose7, 7);
        setOnClickListenerForImage(imageChoose8, 8);
        setOnClickListenerForImage(imageChoose9, 9);
        setOnClickListenerForImage(imageChoose10, 10);
        setOnClickListenerForImage(imageChoose11, 11);
        setOnClickListenerForImage(imageChoose12, 12);
    }

    private void setOnClickListenerForImage(ImageView imageView, final int imageNumber) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageResource = getImageResource(imageNumber);
                manager.setImage(manager.getCurrentUser().getUid(), imageResource);
                Toast.makeText(SelectProfilePictureView.this, "image changed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private int getImageResource(int imageNumber) {
        switch (imageNumber) {
            case 1:
                return R.mipmap.avatar;
            case 2:
                return R.mipmap.burn;
            case 3:
                return R.mipmap.cat;
            case 4:
                return R.mipmap.cat3;
            case 5:
                return R.mipmap.cat2;
            case 6:
                return R.mipmap.kid;
            case 7:
                return R.mipmap.men1;
            case 8:
                return R.mipmap.panda;
            case 9:
                return R.mipmap.seal;
            case 10:
                return R.mipmap.smile;
            case 11:
                return R.mipmap.woman1;
            case 12:
                return R.mipmap.woman2;
            default:
                return 0;
        }
    }
}
