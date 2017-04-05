package net.catchpole.B9.devices.rockblock.webservice;

import net.catchpole.B9.lang.HexString;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class MTMessageSender {
    private static String ENDPOINT = "http://core.rock7.com/rockblock/MT";

    public long send(MTMessage mtMessage) throws IOException {
        URL url = new URL(ENDPOINT);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("imei", mtMessage.getImei());
        params.put("username", mtMessage.getUsername());
        params.put("password", mtMessage.getPassword());
        params.put("data", HexString.toHexString(mtMessage.getData()));

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(postDataBytes);
        outputStream.close();

        DataInputStream dataInputStream = new DataInputStream(connection.getInputStream());
        try {
            String line = dataInputStream.readLine();
            String[] data = line.split(",");
            if (data[0].equals("OK")) {
                return Long.parseLong(data[1]);
            }
            throw new IOException(line);
        } finally {
            dataInputStream.close();
        }
    }
}
