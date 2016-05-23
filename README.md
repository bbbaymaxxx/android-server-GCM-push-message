# android-server-GCM-push-message

Android Server 端透過 HTTP 推播訊息給 GCM

參考 [Google Cloud Messaging (GCM) HTTP Connection Server](https://developers.google.com/cloud-messaging/http#auth)

1. 有 Google 帳號
2. 開啟 GCM 服務
3. 創造一個 Android API Key
4. 跟接收者要 Registration ID

POST request : https://gcm-http.googleapis.com/gcm/send

HTTP header 規定內容：

> httpPost.addHeader("Authorization", "key=" + apiKey);
> 
httpPost.addHeader("Content-Type", "application/json");

將訊息包成 JSON 格式送出，並且要完全符合以下格式

    {
	    "to" : "Registration_ID",
	    "data" : {
		    "data_name_1" : "...",
		    "data_name_2" : "...",
		    ...
	    },
    }

