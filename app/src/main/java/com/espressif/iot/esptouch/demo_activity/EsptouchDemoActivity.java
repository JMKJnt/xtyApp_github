package com.espressif.iot.esptouch.demo_activity;

import java.net.DatagramPacket;
import java.util.List;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.example.aiden.xtapp.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EsptouchDemoActivity extends Activity implements OnClickListener {

	private static final String TAG = "EsptouchDemoActivity";

	private TextView mTvApSsid;

	private EditText mEdtApPassword;

	private Button mBtnConfirm;
	
//	private Switch mSwitchIsSsidHidden;

	private EspWifiAdminSimple mWifiAdmin;
	
//	private Spinner mSpinnerTaskCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cennect);

		mWifiAdmin = new EspWifiAdminSimple(this);
		mTvApSsid = (TextView) findViewById(R.id.tvApSssidConnected);
		mEdtApPassword = (EditText) findViewById(R.id.edtApPassword);
		mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
//		mSwitchIsSsidHidden = (Switch) findViewById(R.id.switchIsSsidHidden);
		mBtnConfirm.setOnClickListener(this);
//		initSpinner(); lzw 这个连接次数暂时用不到，所以注释掉
	}
	
//	private void initSpinner()  lzw 这个连接次数暂时用不到，所以注释掉
//	{
//		mSpinnerTaskCount = (Spinner) findViewById(R.id.spinnerTaskResultCount);
//		int[] spinnerItemsInt = getResources().getIntArray(R.array.taskResultCount);
//		int length = spinnerItemsInt.length;
//		Integer[] spinnerItemsInteger = new Integer[length];
//		for(int i=0;i<length;i++)
//		{
//			spinnerItemsInteger[i] = spinnerItemsInt[i];
//		}
//		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
//				android.R.layout.simple_list_item_1, spinnerItemsInteger);
//		mSpinnerTaskCount.setAdapter(adapter);
//		mSpinnerTaskCount.setSelection(1);
//	}

	@Override
	protected void onResume() {
		super.onResume();
		// display the connected ap's ssid
		String apSsid = mWifiAdmin.getWifiConnectedSsid();
		if (apSsid != null) {
			mTvApSsid.setText(apSsid);
		} else {
			mTvApSsid.setText("");
		}
		// check whether the wifi is connected
		boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
		mBtnConfirm.setEnabled(!isApSsidEmpty);
	}

	@Override
	public void onClick(View v) {

		if (v == mBtnConfirm) {
			String apSsid = mTvApSsid.getText().toString();
			String apPassword = mEdtApPassword.getText().toString();
			String apBssid = mWifiAdmin.getWifiConnectedBssid();
//			Boolean isSsidHidden = mSwitchIsSsidHidden.isChecked();
			String isSsidHiddenStr = "NO";
//			String taskResultCountStr = Integer.toString(mSpinnerTaskCount
//					.getSelectedItemPosition()); 获取连接次数的，上面注掉了，下面复制了一下，给个固定值 1

			String taskResultCountStr = Integer.toString(1);
//			if (isSsidHidden)
//			{
//				isSsidHiddenStr = "YES";
//			}
			if (__IEsptouchTask.DEBUG) {
				Log.d(TAG, "mBtnConfirm is clicked, mEdtApSsid = " + apSsid
						+ ", " + " mEdtApPassword = " + apPassword);
			}
			new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword,
					isSsidHiddenStr, taskResultCountStr);
		}
	}
	
	private class EsptouchAsyncTask2 extends AsyncTask<String, Void, IEsptouchResult> {

		private ProgressDialog mProgressDialog;

		private IEsptouchTask mEsptouchTask;
		// without the lock, if the user tap confirm and cancel quickly enough,
		// the bug will arise. the reason is follows:
		// 0. task is starting created, but not finished
		// 1. the task is cancel for the task hasn't been created, it do nothing
		// 2. task is created
		// 3. Oops, the task should be cancelled, but it is running
		private final Object mLock = new Object();

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(EsptouchDemoActivity.this);
			mProgressDialog
					.setMessage("Esptouch is configuring, please wait for a moment...");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					synchronized (mLock) {
						if (__IEsptouchTask.DEBUG) {
							Log.i(TAG, "progress dialog is canceled");
						}
						if (mEsptouchTask != null) {
							mEsptouchTask.interrupt();
						}
					}
				}
			});
			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					"Waiting...", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			mProgressDialog.show();
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(false);
		}

		@Override
		protected IEsptouchResult doInBackground(String... params) {
			synchronized (mLock) {
				String apSsid = params[0];
				String apBssid = params[1];
				String apPassword = params[2];
				String isSsidHiddenStr = params[3];
				boolean isSsidHidden = false;
				if (isSsidHiddenStr.equals("YES")) {
					isSsidHidden = true;
				}
				mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,
						isSsidHidden, EsptouchDemoActivity.this);
			}
			IEsptouchResult result = mEsptouchTask.executeForResult();
			return result;
		}

		
		@Override
		protected void onPostExecute(IEsptouchResult result) {
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(true);
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
					"Confirm");
			// it is unnecessary at the moment, add here just to show how to use isCancelled()
			if (!result.isCancelled()) {
				if (result.isSuc()) {
					byte[] receiveBytes = result.getReceiveBytes();
//					byte[] request = new byte[8192];
					DatagramPacket pack = new DatagramPacket(receiveBytes, receiveBytes.length);
					String RecMsg = new String(pack.getData(),0,pack.getLength());
					mProgressDialog.setMessage("Esptouch success, bssid = "
							+ result.getBssid() + ",InetAddress = "
							+ result.getInetAddress().getHostAddress()+"----all----:"+RecMsg);
//					mProgressDialog.setMessage("----all----:"+RecMsg);
				} else {
					mProgressDialog.setMessage("Esptouch fail");
				}
			}
		}
	}
	
	private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				String text = result.getBssid() + " is connected to the wifi";
				Toast.makeText(EsptouchDemoActivity.this, text,
						Toast.LENGTH_LONG).show();
			}

		});
	}

	private IEsptouchListener myListener = new IEsptouchListener() {

		@Override
		public void onEsptouchResultAdded(final IEsptouchResult result) {
			onEsptoucResultAddedPerform(result);
		}
	};
	
	private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

		private ProgressDialog mProgressDialog;

		private IEsptouchTask mEsptouchTask;
		// without the lock, if the user tap confirm and cancel quickly enough,
		// the bug will arise. the reason is follows:
		// 0. task is starting created, but not finished
		// 1. the task is cancel for the task hasn't been created, it do nothing
		// 2. task is created
		// 3. Oops, the task should be cancelled, but it is running
		private final Object mLock = new Object();


		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(EsptouchDemoActivity.this);
			mProgressDialog
					.setMessage("产品网络配置中, 需要一些时间...");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					synchronized (mLock) {
						if (__IEsptouchTask.DEBUG) {
							Log.i(TAG, "progress dialog is canceled");
						}
						if (mEsptouchTask != null) {
							mEsptouchTask.interrupt();
						}
					}
				}
			});
			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					"努力中...", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			mProgressDialog.show();
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(false);
		}

		@Override
		protected List<IEsptouchResult> doInBackground(String... params) {
			int taskResultCount = -1;
			synchronized (mLock) {
				// !!!NOTICE
				String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
				String apBssid = params[1];
				String apPassword = params[2];
				String isSsidHiddenStr = params[3];
				String taskResultCountStr = params[4];
				boolean isSsidHidden = false;
				if (isSsidHiddenStr.equals("YES")) {
					isSsidHidden = true;
				}
				taskResultCount = Integer.parseInt(taskResultCountStr);
				mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword,
						isSsidHidden, EsptouchDemoActivity.this);
				mEsptouchTask.setEsptouchListener(myListener);
			}
			List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
			return resultList;
		}


		@Override
		protected void onPostExecute(List<IEsptouchResult> result) {
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
					.setEnabled(true);
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
					"确定");
			IEsptouchResult firstResult = result.get(0);
			// check whether the task is cancelled and no results received
			if (!firstResult.isCancelled()) {
				int count = 0;
				// max results to be displayed, if it is more than maxDisplayCount,
				// just show the count of redundant ones
				final int maxDisplayCount = 5;
				// the task received some results including cancelled while
				// executing before receiving enough results
				if (firstResult.isSuc()) {

					byte[] receiveBytes = firstResult.getReceiveBytes();
//					byte[] request = new byte[8192];
//					DatagramPacket pack = new DatagramPacket(receiveBytes, receiveBytes.length);
//					String RecMsg = new String(pack.getData(),0,pack.getLength());

					String RecMsg="";
						if (receiveBytes == null || receiveBytes.length <= 0) {

						}else {

							char[] res = new char[receiveBytes.length * 2]; // 每个byte对应两个字符
							final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
							for (int i = 0, j = 0; i < receiveBytes.length; i++) {
								res[j++] = hexDigits[receiveBytes[i] >> 4 & 0x0f]; // 先存byte的高4位
								res[j++] = hexDigits[receiveBytes[i] & 0x0f]; // 再存byte的低4位
							}
							 RecMsg = new String(res);
						}

					StringBuilder sb = new StringBuilder();
					for (IEsptouchResult resultInList : result) {
//						sb.append("Esptouch success, bssid = "
//								+ resultInList.getBssid()
//								+ ",InetAddress = "
//								+ resultInList.getInetAddress()
//										.getHostAddress() + "\n");

//		测试用：设备连接成功后的返回打印
//						sb.append("全部数据："
//								+ RecMsg
//								+ ",byte[]原始长度 = "
//								+ receiveBytes.length
//								+ ",InetAddress = "
//								+ resultInList.getInetAddress()
//								+ "\n");

						sb.append("设备已经连接wifi网络");

						count++;
						if (count >= maxDisplayCount) {
							break;
						}
					}
					if (count < result.size()) {
						sb.append("\nthere's " + (result.size() - count)
								+ " more result(s) without showing\n");
					}
					mProgressDialog.setMessage(sb.toString());
				} else {
					mProgressDialog.setMessage("连接异常，请重试");
				}
			}
		}
	}
}
