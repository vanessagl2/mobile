package br.com.redu.redumobile.activities;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import br.com.developer.redu.models.Lecture;
import br.com.developer.redu.models.Subject;
import br.com.redu.redumobile.R;
import br.com.redu.redumobile.util.DownloadHelper;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LectureActivity extends BaseActivity{
	
	private TextView mTvSubject;
	private TextView mTvLecture;
	private ImageButton mBtEdit;
	private ImageButton mBtRemove;
	
	private ImageButton mBtIsDone;
	private ImageButton mBtWall;
	
	private Lecture mLecture;
	private Subject mSubject;
	
	private ProgressDialog mProgressDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture);
		
		//mTvSubject = (TextView) findViewById(R.id.tvSubject);
		mTvLecture = (TextView) findViewById(R.id.tvLecture);
		mBtEdit = (ImageButton) findViewById(R.id.btEdit);
		mBtRemove = (ImageButton) findViewById(R.id.btRemove);
		/*mIvImage = (ImageView) findViewById(R.id.ivImage);
		mTvFileName = (TextView) findViewById(R.id.tvFileName);*/
		
		mBtIsDone = (ImageButton) findViewById(R.id.btIsDone);
		mBtWall = (ImageButton) findViewById(R.id.btWall);
		
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Aguarde...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	
		
		mLecture = (Lecture)getIntent().getExtras().get(Lecture.class.getName());
		mSubject = (Subject)getIntent().getExtras().get(Subject.class.getName());
		LinearLayout layoutLecture;
		if (mLecture.type.equals(Lecture.TYPE_CANVAS) || mLecture.type.equals(Lecture.TYPE_EXERCISE)) {
			layoutLecture = (LinearLayout)findViewById(R.id.llCanvasExercice);
			ImageView ivCanvas = (ImageView) layoutLecture.findViewById(R.id.ivCanvasExercice);
			TextView tvCanvas = (TextView) layoutLecture.findViewById(R.id.tvCanvasExercice);
			ImageView ibCanvas = (ImageView) layoutLecture.findViewById(R.id.ivCanvasExercice);
			tvCanvas.setText("Exercice... ou Canvas...");
			layoutLecture.setVisibility(View.VISIBLE);
		}else if (mLecture.type.equals(Lecture.TYPE_DOCUMENT)) {
			layoutLecture = (LinearLayout)findViewById(R.id.llDocument);
			ImageView ivDocument = (ImageView) layoutLecture.findViewById(R.id.ivDocument);
			ivDocument.setImageResource(R.drawable.ic_doc);
			TextView tvDocument = (TextView) layoutLecture.findViewById(R.id.tvDocument);
			tvDocument.setText(mLecture.name);
			ImageButton ibDocument = (ImageButton) layoutLecture.findViewById(R.id.btAcessarDocument);
			ibDocument.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Lecture[] lecture = {mLecture};
					
					java.io.File f = new java.io.File(DownloadHelper.getLecturePath(), mLecture.getFileName());
					if (f.exists()) {
						Intent it;
						try {
							it = DownloadHelper.loadDocInReader(f);
							startActivity(it);
						} catch (ActivityNotFoundException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						new DownloadFile().execute(lecture);
					}
				}
			});
			layoutLecture.setVisibility(View.VISIBLE);
		}else if (mLecture.type.equals(Lecture.TYPE_MEDIA)) {
			layoutLecture = (LinearLayout)findViewById(R.id.llMedia);
			ImageView ivMedia = (ImageView) layoutLecture.findViewById(R.id.ivMedia);
			ivMedia.setImageResource(R.drawable.ic_video_big);
			TextView tvMedia = (TextView) layoutLecture.findViewById(R.id.tvMedia);
			tvMedia.setText(mLecture.name);
			ImageButton ibMedia = (ImageButton) layoutLecture.findViewById(R.id.ibAcessarMedia);
			layoutLecture.setVisibility(View.VISIBLE);
		}else if (mLecture.type.equals(Lecture.TYPE_PAGE)) {
			layoutLecture = (LinearLayout)findViewById(R.id.llPage);
			TextView tvPage = (TextView) layoutLecture.findViewById(R.id.tvPage);
			tvPage.setText(mLecture.raw);
			layoutLecture.setVisibility(View.VISIBLE);
		}
		
		mTvLecture.setText(mLecture.name);
		//mTvSubject.setText(mSubject.name);
		
		//Log.i("Modulo", mSubject.id);
		Log.i("Aula", Integer.toString(mLecture.id));
		
		mBtEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
		mBtRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		String extension = mLecture.mimetype;
		
		
		
		
		//mTvFileName.setText(mLecture.name+" Tipo:"+mLecture.type);
		
		mBtIsDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
		mBtWall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
	}

	private class DownloadFile extends AsyncTask<Lecture, Integer, java.io.File> {
	    @Override
	    protected java.io.File doInBackground(Lecture... lecture) {
	        try {
	        	
	        	String path = lecture[0].getFilePath();
	            URL url = new URL(path);
	            
	            String[] temp = path.split("\\?")[0].split("/");
	            String fileName = temp[temp.length-1];
	            
	            URLConnection connection = url.openConnection();
	            connection.connect();
	            // this will be useful so that you can show a typical 0-100% progress bar
	            int fileLength = connection.getContentLength();
	            
	            
	            String newFolder = DownloadHelper.getLecturePath();
	    		java.io.File myNewFolder = new java.io.File(newFolder);
	    		if (!myNewFolder.exists())
	    			myNewFolder.mkdirs();
	            
	            // download the file
	            InputStream input = new BufferedInputStream(url.openStream());
	            //java.io.File sdCard = Environment.getExternalStorageDirectory();
	            /*java.io.File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");
	            dir.mkdirs();
	            File file = new File(dir, "filename");*/
	            java.io.File filling = new java.io.File(newFolder, fileName);
	            OutputStream output = new FileOutputStream(filling);

	            byte data[] = new byte[1024];
	            long total = 0;
	            int count;
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                publishProgress((int) (total * 100 / fileLength));
	                output.write(data, 0, count);
	            }
	            
	            output.flush();
	            output.close();
	            input.close();
	            return filling;
	        } catch (Exception e) {
	        }
	        return null;
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressDialog.show();
	    }
	    
	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        mProgressDialog.setProgress(progress[0]);
	    }
	    
	    @Override
	    protected void onPostExecute(java.io.File file) {
	    	super.onPostExecute(file);
	    	mProgressDialog.setProgress(0);
	    	mProgressDialog.dismiss();
	    	if (this != null){
	    		try {
					Intent it = DownloadHelper.loadDocInReader(file);
					startActivity(it);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    		
	    }
	    
	}
	
}
