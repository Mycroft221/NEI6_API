package com.example.demo;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

public class MyClass
{
	private final List<NameValuePair> params;
	private final HttpPost post;
	private HttpResponse resp;
	private final HttpClient client;
	private int respCode;
	private BufferedReader reader;
	private final StringBuffer result;
	private String line;
	private String[] attributes =
			{
			        "transfer",
                    "temperature",
                    "obesity",
                    "helmet",
                    "sbp",
                    "pulse",
                    "gcs",
                    "fracture",
                    "gunshot",
                    "male",
                    "age",
                    "white",
                    "hispanic",
                    "asian",
                    "black",
                    "pacific_islander",
                    "indian",
                    "other_moi",
                    "stab",
                    "gsw",
                    "fall",
                    "mvc",
                    "motorcycle",
                    "bike",
                    "ped",
                    "dist_0_15",
                    "dist_15_30",
                    "dist_30_",
                    "ground",
                    "helicopter",
                    "other_transfer",
                    "time",
                    "intubated",
                    "king_airway",
                    "lma",
                    "bvm",
                    "blood",
                    "fast",
                    "fast_p",
                    "co2",
                    "base",
                    "skull",
                    "penetrating_head",
                    "lasceration_head",
                    "difficulty_breathing",
                    "pusatile_bleeding_head",
                    "neurologic_deficit",
                    "penetrating_abd",
                    "unstable_pelvis",
                    "evisceration",
                    "flail_chest",
                    "sucking_chest",
                    "open_fracture",
                    "tourniquet",
                    "pusatile_bleeding_ext",
                    "pulseless_ext",
                    "amputation",
                    "fast_na",
                    "fast_p_na",
					"transfer_patient",
					"transfer_blood",
					"or",
					"etco2_na",
					"base_na",
                    "female",
                    "nonbinary",
					"india",
					"arab"
            };

//	public static void main(String[] args) {
//		double[] values = {0, 1540, 22};
//		MyClass myClass = new MyClass(values);
//		myClass.doPost();
//	}

	public MyClass(double[] values, int generated_id, String dag)
	{
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", "F8502827A12E38715ABE4C82413AF5DC"));
		params.add(new BasicNameValuePair("content", "record"));
		params.add(new BasicNameValuePair("format", "json"));
		params.add(new BasicNameValuePair("type", "flat"));
		params.add(new BasicNameValuePair("overwriteBehavior", "normal"));
		params.add(new BasicNameValuePair("forceAutoNumber", "true"));
		String formattedValues = formatData(attributes, values);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
		String timestamp = ts.toString();
		String data = String.format("[{\"record_id\":\"0\"," +
                "\"redcap_data_access_group\":\"%s\"," +
				"\"generated_id\":\"%d\"," +
                "\"timestamp\":\"%s\"," +
				"%s" +
				"\"appdata_complete\":\"2\"}]", dag,
                generated_id, timestamp, formattedValues);
		System.out.println(data);
		params.add(new BasicNameValuePair("data", data));
		params.add(new BasicNameValuePair("returnContent", "count"));
		params.add(new BasicNameValuePair("returnFormat", "json"));

		post = new HttpPost("https://redcap.mcw.edu/api/");
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");

		try
		{
			post.setEntity(new UrlEncodedFormEntity(params));
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		result = new StringBuffer();
		client = HttpClientBuilder.create().build();
		respCode = -1;
		reader = null;
		line = null;
	}

	private String formatData(String[] attributes, double[] values) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < attributes.length; i++) {
			s.append("\""); // "
			s.append(attributes[i]); // column name
			s.append("\":\""); // ":"
			s.append(values[i]); // value
			s.append("\","); // ",
		}
		return s.toString();
	}

	public void doPost()
	{
		resp = null;

		try
		{
			resp = client.execute(post);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		if(resp != null)
		{
			respCode = resp.getStatusLine().getStatusCode();

			try
			{
				reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}

		if(reader != null)
		{
			try
			{
				while ((line = reader.readLine()) != null)
				{
					result.append(line);
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}

		System.out.println("respCode: " + respCode);
		System.out.println("result: " + result.toString());
	}
}
