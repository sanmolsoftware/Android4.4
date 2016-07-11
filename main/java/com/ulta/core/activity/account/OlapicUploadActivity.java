package com.ulta.core.activity.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.widgets.flyin.OnPermissionCheck;
import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.util.caching.UltaDataCache;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ulta.core.util.UltaException.COMMON_ERROR_TITLE;

/*import java.util.List;*/

public class OlapicUploadActivity extends UltaBaseActivity implements OnPermissionCheck {

    private ListView olapicUploadFoldersList;

    private GridView gridView;

    private ImageAdapter mAdapter;

    private ImageView camera;

    private ArrayList<String> folderName = new ArrayList<String>();

    private ArrayList<String> listOfImages = new ArrayList<String>();

    private ArrayList<String> listOfImageIds = new ArrayList<String>();

	/* private ArrayList<String> folderImageCount = new ArrayList<String>(); */

    public static final int CAMERA_REQUEST = 100;

	/* private boolean fromCamera = false; */

    private String caption = "";

    private Button uploadImageBtn;

    private int selectedImagePosition = -1;

    private LinearLayout folderListLayout;

    private LinearLayout imageGalleryLayout;

    private LinearLayout uploadBtnLayout;
    private TextView title;

	/* private int prevPosition = -1; */

	/* private int currentPosition; */

    private int count = 0;

	/* private List<String> ListofSamplesSelected = new ArrayList<String>(); */

    private boolean isUploadClicked = false;
    OlapicUploadFoldersAdapter olapicUploadFoldersAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.olapic_upload_activity);
        setActivity(OlapicUploadActivity.this);
        olapicUploadFoldersList = (ListView) findViewById(R.id.olapic_upload_folders_list);
        camera = (ImageView) findViewById(R.id.camera);
        uploadImageBtn = (Button) findViewById(R.id.uploadImgBtn);
        folderListLayout = (LinearLayout) findViewById(R.id.folderList_lyt);
        imageGalleryLayout = (LinearLayout) findViewById(R.id.imagegallery_lyt);
        uploadBtnLayout = (LinearLayout) findViewById(R.id.uploadBtn_lyt);
        title = (TextView) findViewById(R.id.title);
        olapicUploadFoldersAdapter = new OlapicUploadFoldersAdapter();
        olapicUploadFoldersList.setAdapter(olapicUploadFoldersAdapter);

        //Check external storage permission
        checkForAppPermissions(OlapicUploadActivity.this, WebserviceConstants.PERMISSION_WRITE_EXTERNAL_STORAGE, WebserviceConstants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE, WebserviceConstants.WRITE_EXTERNAL_STORAGE_DIALOG_TITLE, WebserviceConstants.WRITE_EXTERNAL_STORAGE_DIALOG_MESSAGE);

        olapicUploadFoldersList
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        camera.setVisibility(View.GONE);
                        listOfImages = getAllShownImagesPath(
                                OlapicUploadActivity.this, folderName.get(arg2));
                        gridView = (GridView) findViewById(R.id.gridView1);
                        folderListLayout.setVisibility(View.GONE);
                        imageGalleryLayout.setVisibility(View.VISIBLE);
                        uploadBtnLayout.setVisibility(View.VISIBLE);
                        mAdapter = new ImageAdapter();
                        gridView.setAdapter(mAdapter);
                        title.setText(getResources().getString(R.string.photo));

                    }
                });

        camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //check camera permission
                checkForAppPermissions(OlapicUploadActivity.this, WebserviceConstants.PERMISSION_CAMERA, WebserviceConstants.CAMERA_REQUEST_CODE, WebserviceConstants.PERMISSION_CAMERA_DIALOG_TITLE, WebserviceConstants.PERMISSION_CAMERA_DIALOG_MESSAGE);


            }
        });

        uploadImageBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                isUploadClicked = true;
                if (selectedImagePosition == -1) {

                    displayErrorMessage("Please select an image to upload");

                } else {

                    final Dialog captionDialog = new Dialog(
                            OlapicUploadActivity.this);
                    captionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    captionDialog.setContentView(R.layout.caption_popup);
                    captionDialog.setCancelable(false);
                    final EditText captionEdtTxt = (EditText) captionDialog
                            .findViewById(R.id.caption_edtTxt);
                    Button captionOkBtn = (Button) captionDialog
                            .findViewById(R.id.caption_ok);
                    Button captionCancelBtn = (Button) captionDialog
                            .findViewById(R.id.caption_cancel);
                    captionDialog.show();
                    captionOkBtn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            caption = captionEdtTxt.getText().toString();

                            if (caption.equalsIgnoreCase("") || null == caption) {
                                captionEdtTxt
                                        .setError("Please enter some caption");
                            } else {
                                captionDialog.dismiss();
                                Intent intent = new Intent();
                                intent.putExtra("imagePath",
                                        listOfImages.get(selectedImagePosition));
                                intent.putExtra("caption", caption);
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                        }
                    });

                    captionCancelBtn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            isUploadClicked = false;
                            captionDialog.dismiss();
                        }
                    });

                }

            }
        });

    }

    public class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listOfImages.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewGalleryHolder holder;
            /* final ViewGroup parentView = parent; */
            final int pos = position;

            if (view == null) { // If the View is not cached
                Context context = getApplicationContext();
                holder = new ViewGalleryHolder();
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.images_in_gallery, parent,
                        false);
                view.setTag(holder);
            } else {
                holder = (ViewGalleryHolder) view.getTag();
            }
            holder.galleryImageView = (ImageView) view
                    .findViewById(R.id.gallery_image);
            holder.selectedImageCheckBox = (CheckBox) view
                    .findViewById(R.id.chkimageSelected);
            holder.selectedImageCheckBox.setChecked(false);
            count = 0;

            Uri uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer
                            .toString(Integer.parseInt(listOfImageIds
                                    .get(position))));

            new AQuery(holder.galleryImageView).image(loadThumbnailImage(uri
                    .toString()));

            holder.selectedImageCheckBox
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {

                            if (isChecked) {

                                if (count == 0) {
                                    holder.selectedImageCheckBox
                                            .setChecked(true);
                                    selectedImagePosition = pos;
                                    count = 1;
                                } else {
                                    holder.selectedImageCheckBox
                                            .setChecked(false);
                                    displayErrorMessage("You can select only one photo at a time.");
                                }

                            } else {
                                if (!isUploadClicked) {
                                    holder.selectedImageCheckBox
                                            .setChecked(false);
                                    selectedImagePosition = -1;
                                    count = 0;
                                }

                            }
                        }
                    });

            return view;
        }

    }

    protected Bitmap loadThumbnailImage(String url) {
        // Get original image ID
        int originalImageId = Integer.parseInt(url.substring(
                url.lastIndexOf("/") + 1, url.length()));

        // Get (or create upon demand) the mini thumbnail for the original
        // image.
        return MediaStore.Images.Thumbnails.getThumbnail(
                OlapicUploadActivity.this.getContentResolver(),
                originalImageId, MediaStore.Images.Thumbnails.MICRO_KIND, null);

    }

    class ViewGalleryHolder {
        ImageView galleryImageView;
        CheckBox selectedImageCheckBox;
    }

    @SuppressWarnings("deprecation")
    public void getFoldersList() {
        String[] projection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = managedQuery(images, projection, "", null,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        if (cur.moveToFirst()) {
            String bucket = "";
            int bucketColumn = cur
                    .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            do {
                if (!bucket.equalsIgnoreCase(cur.getString(bucketColumn))) {
                    folderName.add(cur.getString(bucketColumn));
                }

                bucket = cur.getString(bucketColumn);

            } while (cur.moveToNext());

        }

    }

    public class OlapicUploadFoldersAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return folderName.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            final ViewHolder holder;
            if (view == null) { // If the View is not cached
                Context context = getApplicationContext();
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.olapic_upload_folders_list,
                        parent, false);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.folderName = (TextView) view.findViewById(R.id.folder_name);
            holder.folderName.setText(folderName.get(position));

            return view;
        }

    }

    class ViewHolder {
        TextView folderName;
    }

    public ArrayList<String> getAllShownImagesPath(Activity activity,
                                                   String folderName) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_index_mediaId;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        String originalImageId = null;
        String absoluteFolder = "";
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media._ID};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        column_index_mediaId = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            absoluteFolder = cursor.getString(column_index_folder_name);
            absolutePathOfImage = cursor.getString(column_index_data);
            originalImageId = cursor.getString(column_index_mediaId);

            if (absoluteFolder.equalsIgnoreCase(folderName)) {
                listOfAllImages.add(absolutePathOfImage);
                listOfImageIds.add(originalImageId);
            }

        }
        return listOfAllImages;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {

                    UltaDataCache.getDataCacheInstance().setCameraCapturedPath(
                            Environment.getExternalStorageDirectory().toString()
                                    + "/Ulta/" + UltaDataCache.getDataCacheInstance().getFilepathToBeUploaded());
                    UltaDataCache.getDataCacheInstance().setCameraCaption(caption);
                    UltaDataCache.getDataCacheInstance().setFromCamera(true);
                    finish();
                }
            break;

        }

    }

    public void displayErrorMessage(String message) {
        final String lblPositiveButton = "OK";
        final String errorTitle = COMMON_ERROR_TITLE;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog alertDialog = new Dialog(OlapicUploadActivity.this,
                R.style.AppCompatAlertDialogStyle);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_dialog);
        TextView headingTV = (TextView) alertDialog.findViewById(R.id.heading);
        TextView messageTV = (TextView) alertDialog.findViewById(R.id.message);
        Button mAgreeButton = (Button) alertDialog.findViewById(R.id.btnAgree);
        Button mDisagreeButton = (Button) alertDialog
                .findViewById(R.id.btnDisagree);
        headingTV.setText(errorTitle);
        messageTV.setText(message);
        mAgreeButton.setText(lblPositiveButton);
        mDisagreeButton.setText("");
        alertDialog.getWindow().setLayout((6 * width) / 7, (height) / 3);

        mDisagreeButton.setVisibility(View.GONE);
        mAgreeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * App permission check result for camera and write external storage
     *
     * @param isSuccess
     * @param permissionRequestCode
     */
    @Override
    public void onPermissionCheckRequest(boolean isSuccess, int permissionRequestCode) {
        if (isSuccess) {

            if (permissionRequestCode == WebserviceConstants.CAMERA_REQUEST_CODE) {
                final Dialog captionDialog = new Dialog(
                        OlapicUploadActivity.this);
                captionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                captionDialog.setContentView(R.layout.caption_popup);
                captionDialog.setCancelable(false);
                final EditText captionEdtTxt = (EditText) captionDialog
                        .findViewById(R.id.caption_edtTxt);
                Button captionOkBtn = (Button) captionDialog
                        .findViewById(R.id.caption_ok);
                Button captionCancelBtn = (Button) captionDialog
                        .findViewById(R.id.caption_cancel);
                captionDialog.show();
                captionOkBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        caption = captionEdtTxt.getText().toString();

                        if (caption.equalsIgnoreCase("") || null == caption) {
                            captionEdtTxt.setError("Please enter some caption");
                        } else {
                            Intent cameraIntent = new Intent(
                                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                            File imagesFolder = new File(Environment
                                    .getExternalStorageDirectory(), "Ulta");
                            if (!imagesFolder.exists()) {
                                imagesFolder.mkdirs();
                            }

                            Date date = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                            String fileName = "Upload_" + dateFormat.format(date) + ".jpeg";
                            UltaDataCache.getDataCacheInstance().setFilepathToBeUploaded(fileName);
                            File image = new File(imagesFolder, fileName);

                            Uri uriSavedImage = Uri.fromFile(image);

                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    uriSavedImage);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            captionDialog.dismiss();
                        }
                    }
                });

                captionCancelBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        captionDialog.dismiss();
                    }
                });
            } else if (permissionRequestCode == WebserviceConstants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
                getFoldersList();
                if (null != olapicUploadFoldersAdapter) {
                    olapicUploadFoldersAdapter.notifyDataSetChanged();
                }
            }
        } else {
            if (permissionRequestCode == WebserviceConstants.WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
                finish();
            }
        }
    }

}
