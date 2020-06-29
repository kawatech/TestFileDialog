package com.losteaka.kawatech.testfiledialog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity implements FileSelectionDialog.OnFileSelectListener
{
    // 定数
    private static final int MENUID_FILE                              = 0;// ファイルメニューID
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1; // 外部ストレージ読み込みパーミッション要求時の識別コード
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;     // kawa

    public static final String TAG = "TestFileDialog";

    // メンバー変数  kawa 最初からAndroid/dataにする。
//    private String m_strInitialDir = Environment.getExternalStorageDirectory().getPath();    // 初期フォルダ

// kawa 最初からAndroid/dataにする。
    private String m_strInitialDir = Environment.getExternalStorageDirectory().getPath() + "/Android/data";

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Log.d(TAG, "画面表示");
    }

    // オプションメニュー生成時
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        super.onCreateOptionsMenu( menu );
        menu.add( 0, MENUID_FILE, 0, "Select File..." );

        return true;
    }

    // オプションメニュー選択時
    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
            case MENUID_FILE:
                // ダイアログオブジェクト
                FileSelectionDialog dlg = new FileSelectionDialog( this, this );
                dlg.show( new File( m_strInitialDir ) );
                return true;
        }
        return false;
    }

    // ファイルが選択されたときに呼び出される関数
    public void onFileSelect( File file )
    {
        Log.d(TAG, "ファイル選択");
        Toast.makeText( this, "File Selected : " + file.getPath(), Toast.LENGTH_SHORT ).show();
    //     m_strInitialDir = file.getParent();     // kawa 最初のディレクトリから動かさない

        // 試しに削除してみる

        MediaScannerConnection.scanFile(this, new String[] {file.getPath()}, null, null);

        if (file.exists()) {
        //    if (file.delete()) {
            for(int i=0; i<10; i++) {                       // kawa NGなら10回試す。だめ。
                if (file.delete()) {
                    Log.d(TAG, "ファイル消去成功");
                    break;
                } else {
                    Log.d(TAG, "ファイル消去失敗");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            Log.d(TAG, "ファイルが見つかりません");
        }
    }


    // 初回表示時、および、ポーズからの復帰時
    @Override
    protected void onResume()
    {
        super.onResume();

        // 外部ストレージ読み込みパーミッション要求
        requestReadExternalStoragePermission();

        requestWriteExternalStoragePermission();
    }

    // 外部ストレージ読み込みパーミッション要求
    private void requestReadExternalStoragePermission()
    {
        if( PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE ) )
        {    // パーミッションは付与されている
            return;
        }
        // パーミッションは付与されていない。
        // パーミッションリクエスト
        ActivityCompat.requestPermissions( this,
                new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                REQUEST_PERMISSION_READ_EXTERNAL_STORAGE );
    }

    // パーミッション要求ダイアログの操作結果
    @Override
    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults )
    {
        switch( requestCode )
        {
            case REQUEST_PERMISSION_READ_EXTERNAL_STORAGE:
                if( grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED )
                {
                    // 許可されなかった場合
                    Toast.makeText( this, "Permission denied.", Toast.LENGTH_SHORT ).show();
                    finish();    // アプリ終了宣言
                    return;
                }
                break;
               // kawa
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if( grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED )
                {
                    // 許可されなかった場合
                    Toast.makeText( this, "Permission denied.", Toast.LENGTH_SHORT ).show();
                    finish();    // アプリ終了宣言
                    return;
                }
                break;
            default:
                break;
        }
    }

/* --------------------------------------------------------------------------------------- */
    // 外部ストレージ読み込みパーミッション要求
    private void requestWriteExternalStoragePermission()
    {
        if( PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) )
        {    // パーミッションは付与されている
            return;
        }
        // パーミッションは付与されていない。
        // パーミッションリクエスト
        ActivityCompat.requestPermissions( this,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE );
    }





}
