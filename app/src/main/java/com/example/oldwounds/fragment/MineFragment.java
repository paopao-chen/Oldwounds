package com.example.oldwounds.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oldwounds.MainActivity;
import com.example.oldwounds.R;
import com.example.oldwounds.ui.AboutActivity;
import com.example.oldwounds.ui.LoginActivity;
import com.example.oldwounds.utils.LogUtil;
import com.example.oldwounds.utils.SharedPreferencesUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Create by Politness Chen on 2019/10/16--14:05
 * desc:   个人界面Fragment
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private static MineFragment mineFragment;
    public MineFragment(){}

    public static Fragment getInstance(){
        if (mineFragment == null)
            mineFragment = new MineFragment();
        return mineFragment;
    }

    private Button btn_exit;
    private CircleImageView profile_image;
    private TextView user_id;
    private TextView user_name;
    private TextView tv_alter;
    private TextView tv_setting;
    private TextView tv_about;
    private TextView tv_version;

    private PopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);

        findView(view);
        return view;
    }

    private void findView(View view) {
        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        user_id = (TextView) view.findViewById(R.id.user_id);
        user_name = (TextView) view.findViewById(R.id.user_name);
        tv_alter = (TextView) view.findViewById(R.id.tv_alter);
        tv_setting = (TextView) view.findViewById(R.id.tv_setting);
        tv_about = (TextView) view.findViewById(R.id.tv_about);
        tv_version = (TextView) view.findViewById(R.id.tv_version);

        profile_image.setOnClickListener(this);
        tv_about.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                //修改登录状态为false
                SharedPreferencesUtil.putBoolean(getActivity(),"isLogin",false);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.profile_image:
                openPopupWindow(v);
                break;
            case R.id.tv_picture:  //打开相册
                openPicture();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.tv_camera:   //打开相机
                openCamera();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;
            case R.id.tv_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
                default:
                    break;
        }
    }

    private static final String IMAGE_FILE_NAME = "output_image.jpg";
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private static final int REQUEST_CROP = 3;
    private File tempFile;
    private Uri imageUri; // 拍照时返回的uri
    private Uri mCutUri;// 图片裁剪时返回的uri

    private void openCamera() {
        File outputImage = new File(getActivity().getExternalCacheDir(),IMAGE_FILE_NAME);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*Uri imageUri;*/
        //在7.0版本中必须要这样实现，否则会报exposed beyond app through ClipData.Item.getUri()错误
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(getActivity(),"com.example.camera.fileprovider",outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    private void openPicture() {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");*/
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case TAKE_PHOTO:
                cropPhoto(imageUri, true);
                LogUtil.e("MineFragment", "onActivityResult: imgUri:REQUEST_TAKE_PHOTO:" + imageUri.toString());
                break;
            case CHOOSE_PHOTO:
                /*tempFile = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
                cropPhoto(Uri.fromFile(tempFile));*/
                cropPhoto(data.getData(), false);
                LogUtil.e("MineFragment", "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                break;
            case REQUEST_CROP:
                profile_image.setImageURI(mCutUri);
                LogUtil.e("MineFragment", "onActivityResult: imgUri:REQUEST_CROP:" + mCutUri.toString());
                break;
            default:
                break;
        }
    }

    //调用系统的图片裁剪
    private void cropPhoto(Uri uri,boolean fromCamera) {
        if (uri == null) {
            Log.e("MineFragment","uri == null");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri,"image/*");
        //设置裁剪
        intent.putExtra("scale", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        // 图片输出格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 若为false则表示不返回数据
        intent.putExtra("return-data", false);

        if (fromCamera) {
            // 如果是使用拍照，那么原先的uri和最终目标的uri一致,注意这里的uri必须是Uri.fromFile生成的
            mCutUri = imageUri;
            // 一定要添加该项权限，否则会提示无法裁剪
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {  // 从相册中选择，那么裁剪的图片保存在take_photo中
            String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
            String fileName = "photo_" + time;
            File mCutFile = new File(Environment.getExternalStorageDirectory() + "/take_photo", fileName + ".jpeg");
//            File mCutFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME); //随便命名一个
            if (!mCutFile.getParentFile().exists()) {
                mCutFile.getParentFile().mkdirs();
            }
            /*try {
                if (mCutFile.exists()){ //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的
                    mCutFile.delete();
                }
                mCutFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            mCutUri = Uri.fromFile(mCutFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCutUri);
        // 以广播方式刷新系统相册，以便能够在相册中找到刚刚所拍摄和裁剪的照片
        Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intentBc.setData(uri);
        getActivity().sendBroadcast(intentBc);
        startActivityForResult(intent,REQUEST_CROP);
    }

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_iamge, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.popupWindow);
        //对存在NavigationBar的手机上，设置其PopupWindow的出现位置
//        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, navigationHeight);
        //对没有NavigationBar的手机上，设置其PopupWindow的出现位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1);
            }
        });
        //设置PopupWindow的View点击事件
        view.findViewById(R.id.tv_camera).setOnClickListener(this);
        view.findViewById(R.id.tv_picture).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);

        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    //设置屏幕背景透明效果
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

}
