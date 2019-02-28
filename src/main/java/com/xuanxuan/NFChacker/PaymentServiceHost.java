package com.xuanxuan.NFChacker;


import android.content.Context;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class PaymentServiceHost extends HostApduService {

    public PaymentServiceHost(){
        Log.d("PaymentServiceHost", "start");
        ToolsUnit.tv.append("------------------------------------Start------------------------------------\n");
    }
	@Override
	public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.d("PaymentServiceHost", "processCommandApdu: "+ ToolsUnit.bytesToHex(commandApdu));
		return handleApdu(getApplicationContext(), commandApdu);
	}
	@Override
	public void onDeactivated(int reason) {
		Log.d("PaymentServiceHost", "onDeactivated with reason: " + Integer.toString(reason));
        ToolsUnit.tv.append("-------------------------------------End-------------------------------------\n");
	}
	static byte[] handleApdu(Context context, byte[] apdu) {
        ToolsUnit.tv.append("pos:\n"+ToolsUnit.bytesToHex(apdu)+"\n\n");
		Log.d("PaymentServiceHost", "handleApdu");
		String payload =  ToolsUnit.Start(context,apdu);
        ToolsUnit.tv.append("card:\n"+payload+"\n\n");
        return ToolsUnit.hexStringToByteArray(payload);
	}
}