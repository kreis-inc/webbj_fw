package jp.bj_one.fw.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BJSeqUtil {


	public static synchronized String getBjSeq() {


		//yyyymmddhhmmddssfff + java.util.UUID
		 //現在日時取得
        Calendar c = Calendar.getInstance();
        //フォーマットを指定
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        StringBuffer b = new StringBuffer();
        b.append( sdf.format(c.getTime()));
        b.append("-");
        b.append(java.util.UUID.randomUUID().toString());

		return b.toString();
	}

}
