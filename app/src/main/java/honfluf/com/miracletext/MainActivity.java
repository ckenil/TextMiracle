package honfluf.com.miracletext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PICTURE_REQUEST_CODE = 1000;
    public static final int GALLERY_REQUEST_CODE = 1001;

    @BindView(R.id.imgView)
    ImageView imgView;

    @BindView(R.id.btnCamera)
    ImageButton btnTakePicture;

    @BindView(R.id.btnGallery)
    ImageButton btnGallery;

    private Uri selectedImage;

    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnCamera)
    public void onBtnTakePictureClick()
    {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(pictureIntent, TAKE_PICTURE_REQUEST_CODE);
    }

    @OnClick(R.id.btnGallery)
    public void onBtnGalleryClick()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        File filePictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Uri data = Uri.parse(filePictureDirectory.getPath());
        String type = "image/*";
        galleryIntent.setDataAndType(data, type);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == TAKE_PICTURE_REQUEST_CODE)
            {
                Object returnedObject = data.getExtras().get("data");

                if (returnedObject instanceof Bitmap)
                {
                    Bitmap bmpImage = (Bitmap) returnedObject;
                    imgView.setImageBitmap(bmpImage);
                }
            }
            else if (requestCode == GALLERY_REQUEST_CODE)
            {
                selectedImage = data.getData();

                openImage();
            }
        }
    }

    private void openImage()
    {
        try
        {
            InputStream inputStream = getContentResolver().openInputStream(selectedImage);
            image = BitmapFactory.decodeStream(inputStream);
            imgView.setImageBitmap(image);
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(this, "Unable to retrieve image.", Toast.LENGTH_LONG).show();
        }
    }
}
